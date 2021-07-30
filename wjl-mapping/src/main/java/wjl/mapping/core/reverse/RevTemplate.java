package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataGate;
import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.DataProvider;
import wjl.mapping.core.model.DataRecipient;
import wjl.mapping.core.model.FormulaCall;
import wjl.mapping.core.model.FormulaRegister;
import wjl.mapping.core.model.Template;

import java.util.HashMap;
import java.util.Map;

class RevTemplate {
    private final Map<RevFormulaCall, FormulaCall> formulaRevMap;
    private final Map<DataGate, RevFormulaCall> formulaOfDataGates;

    RevTemplate(Template tpl, FormulaRegister register) {
        formulaRevMap = new HashMap<>();
        formulaOfDataGates = new HashMap<>();

        for (FormulaCall call : tpl.getFormulas()) {
            RevFormulaCall revCall = new RevFormulaCall(call, register.getParamsCost(call.getFormulaName()));
            formulaRevMap.put(revCall, call);
            formulaOfDataGates.put(call.getOutput(), revCall);
            for (DataRecipient callInput : call.getInputs().values()) {
                formulaOfDataGates.put(callInput, revCall);
            }
        }
    }

    Candidate dataReady(DataPorter porter, boolean reverse, int dataCost) {
        DataGate data = reverse ? porter.getProvider() : porter.getRecipient();
        RevFormulaCall revCall = formulaOfDataGates.get(data);
        if (revCall != null) {
            if (revCall.dataReady(porter, reverse, dataCost)) {
                return new FormulaCallCost(formulaRevMap.get(revCall), revCall, revCall.getCost());
            }
        } else if (data instanceof DataProvider) {
            // 肯定是模板的输入
            DataProvider tplInput = (DataProvider)data;
            for (DataPorter each : tplInput.getOutList()) {
                // TODO 应该返回一个列表
                if (each != porter  && porter.getSrcPath().contain(each.getSrcPath())) {
                    return new DataPorterCost(each, false, dataCost);
                }
            }
        }
        return null;
    }
}
