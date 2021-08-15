package wjl.mapping.reverse;

import wjl.mapping.model.DataPorter;
import wjl.mapping.model.DataRecipient;
import wjl.mapping.model.FormulaRegister;
import wjl.mapping.model.Template;

import java.util.List;
import java.util.PriorityQueue;

/**
 * 为一个模板生成反向模板的算法。想象一个网络，以公式调用为节点，以数据搬运工为边，
 * 数据在网络中流动。对于反向模板，数据从模板的输出参数流出，沿着边流入节点；到达
 * 节点后不会立即流出，需要汇聚足够的数据，再从节点流出；数据到达模板的输入参数后，
 * 会沿着边再次流入网络。通过非零非负的费用，保证得到最优方案，保证不会出现环路。
 *
 * @author wujinlong
 * @since 2021-8-8
 */
public final class ReverseArithmetic {
    private final FormulaRegister register;

    public ReverseArithmetic(FormulaRegister register) {
        this.register = register;
    }

    /**
     * 生成反向转换模板
     *
     * @param tpl 正向转换模板
     * @return 反向转换模板
     */
    public Template reverse(Template tpl) {
        RevTemplate revTpl = new RevTemplate(tpl, register);

        // 生成初始的候选节点
        final PriorityQueue<CandidateCost> candidateQueue = new PriorityQueue<>();
        for (DataRecipient tplOutput : tpl.getOutputs().values()) {
            for (DataPorter porter : tplOutput.getInList()) {
                candidateQueue.offer(new DataPorterCost(porter, true, 0));
            }
        }

        // 取费用最低的候选节点，生成更多的候选节点
        while (!candidateQueue.isEmpty()) {
            CandidateCost seed = candidateQueue.poll();
            List<? extends CandidateCost> nextList = seed.newCandidate(revTpl);
            if (nextList != null) {
                candidateQueue.addAll(nextList);
            }
        }

        // 生成反向转换模板
        return revTpl.build();
    }
}
