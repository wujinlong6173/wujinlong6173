package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.DataRecipient;
import wjl.mapping.core.model.FormulaRegister;
import wjl.mapping.core.model.Template;

import java.util.Objects;
import java.util.PriorityQueue;

public class ReverseArithmetic {
    private final PriorityQueue<Candidate> candidateQueue = new PriorityQueue<>();

    public Template reverse(Template tpl, FormulaRegister register) {
        RevTemplate revTpl = new RevTemplate(tpl, register);

        for (DataRecipient tplOutput : tpl.getOutputs().values()) {
            for (DataPorter porter : tplOutput.getInList()) {
                candidateQueue.offer(new DataPorterCost(porter, true, 0));
            }
        }

        while (!candidateQueue.isEmpty()) {
            Candidate seed = candidateQueue.poll();
            if (seed instanceof DataPorterCost) {
                DataPorterCost dpc = (DataPorterCost)seed;
                Candidate next = revTpl.dataReady(dpc.getPorter(), dpc.isReverse(), dpc.getCost());
                if (next != null) {
                    candidateQueue.offer(next);
                }
            } else if (seed instanceof FormulaCallCost) {
                handleCandidate((FormulaCallCost)seed);
            }
        }

        return revTpl.build();
    }

    private void handleCandidate(FormulaCallCost fcc) {
        if (!fcc.isFinalChoice()) {
            return; // 添加到队列后，公式选出了更好的输出参数
        }

        if (Objects.equals(fcc.getCall().getResultName(), fcc.getRevCall().getResultName())) {
            for (DataPorter porter : fcc.getCall().getOutput().getOutList()) {
                candidateQueue.offer(new DataPorterCost(porter, false, fcc.getCost()));
            }
        } else {
            DataRecipient callInput = fcc.getCall().getInput(fcc.getRevCall().getResultName());
            for (DataPorter porter : callInput.getInList()) {
                candidateQueue.offer(new DataPorterCost(porter, true, fcc.getCost()));
            }
        }
    }
}
