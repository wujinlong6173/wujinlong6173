package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.DataRecipient;
import wjl.mapping.core.model.FormulaCall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 公式调用作为候选节点，费用等于所有输入参数的费用，加上公式注册的计算费用。
 * 每个公式最多有两次机会作为候选节点：准备好了 N-1 个参数时，选择剩下的参数
 * 作为输出；准备好了 N 个参数时，选择费用最小的参数作为输出。如果第二次选择
 * 更优，优先队列里会出现两个候选节点，费用较大的那个是失效的。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
class FormulaCallCost extends CandidateCost {
    private final RevFormulaCall revCall;

    FormulaCallCost(RevFormulaCall revCall, int cost) {
        super(cost);
        this.revCall = revCall;
    }

    @Override
    List<DataPorterCost> newCandidate(RevTemplate revTpl) {
        if (revCall.getCost() < getCost()) {
            // 添加到队列后，公式选出了更好的输出参数，本对象已经失效
            return null;
        }

        FormulaCall call = revCall.getCall();
        List<DataPorterCost> nextList = new ArrayList<>();
        if (Objects.equals(call.getResultName(), revCall.getResultName())) {
            // 公式调用以相同的形式出现在正向、反向模板中
            for (DataPorter porter : call.getOutput().getOutList()) {
                nextList.add(new DataPorterCost(porter, false, getCost()));
            }
        } else {
            // 公式调用以相反的形式出现在正向、反向模板中
            DataRecipient callInput = call.getInput(revCall.getResultName());
            for (DataPorter porter : callInput.getInList()) {
                nextList.add(new DataPorterCost(porter, true, getCost()));
            }
        }

        return nextList;
    }
}
