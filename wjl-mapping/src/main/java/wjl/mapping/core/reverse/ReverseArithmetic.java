package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.DataRecipient;
import wjl.mapping.core.model.FormulaRegister;
import wjl.mapping.core.model.Template;

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
public class ReverseArithmetic {
    private final PriorityQueue<CandidateCost> candidateQueue = new PriorityQueue<>();

    public Template reverse(Template tpl, FormulaRegister register) {
        RevTemplate revTpl = new RevTemplate(tpl, register);

        for (DataRecipient tplOutput : tpl.getOutputs().values()) {
            for (DataPorter porter : tplOutput.getInList()) {
                candidateQueue.offer(new DataPorterCost(porter, true, 0));
            }
        }

        while (!candidateQueue.isEmpty()) {
            CandidateCost seed = candidateQueue.poll();
            if (seed instanceof DataPorterCost) {
                DataPorterCost dpc = (DataPorterCost)seed;
                RevFormulaCall revCall = revTpl.findRevCall(dpc);
                if (revCall != null) {
                    if (revCall.dataReady(dpc.getPorter(), dpc.isReverse(), dpc.getCost())) {
                        candidateQueue.offer(new FormulaCallCost(
                            revTpl.getCall(revCall), revCall, revCall.getCost()));
                    }
                } else {
                    CandidateCost next = revTpl.dataReady(dpc.getPorter(), dpc.isReverse(), dpc.getCost());
                    if (next != null) {
                        candidateQueue.offer(next);
                    }
                }
            } else if (seed instanceof FormulaCallCost) {
                FormulaCallCost fcc = (FormulaCallCost)seed;
                candidateQueue.addAll(fcc.newCandidate());
            }
        }

        return revTpl.build();
    }
}
