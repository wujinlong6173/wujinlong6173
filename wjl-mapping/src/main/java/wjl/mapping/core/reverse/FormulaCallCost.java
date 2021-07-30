package wjl.mapping.core.reverse;

import wjl.mapping.core.model.FormulaCall;

class FormulaCallCost extends Candidate {
    private final FormulaCall call;
    private final RevFormulaCall revCall;
    private final int cost;

    FormulaCallCost(FormulaCall call, RevFormulaCall revCall, int cost) {
        this.call = call;
        this.revCall = revCall;
        this.cost = cost;
    }

    FormulaCall getCall() {
        return call;
    }

    RevFormulaCall getRevCall() {
        return revCall;
    }

    @Override
    int getCost() {
        return cost;
    }

    boolean isFinalChoice() {
        return revCall.getCost() == cost;
    }
}
