package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataGate;
import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.DataProvider;
import wjl.mapping.core.model.DataRecipient;
import wjl.mapping.core.model.FormulaCall;
import wjl.mapping.core.model.FormulaRegister;
import wjl.mapping.core.model.SiSoTemplate;
import wjl.mapping.core.model.SimplePath;
import wjl.mapping.core.model.Template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

class RevTemplate {
    private final Map<RevFormulaCall, FormulaCall> formulaRevMap;
    private final Map<DataGate, RevFormulaCall> formulaOfDataGates;
    private final List<DataPorterCost> bestForTplInput;
    private final Set<DataPorter> doneInTplInput;
    private final FormulaRegister register;

    RevTemplate(Template tpl, FormulaRegister register) {
        this.register = register;
        formulaRevMap = new HashMap<>();
        formulaOfDataGates = new HashMap<>();
        bestForTplInput = new ArrayList<>();
        doneInTplInput = new HashSet<>();

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
            if (!isBestPorter(porter, dataCost)) {
                return null;
            }
            for (DataPorter each : tplInput.getOutList()) {
                if (doneInTplInput.contains(each)) {
                    // 已经用过该搬运工的反方向，不能再使用正方向
                    continue;
                }
                // TODO 应该返回一个列表
                if (each != porter  && porter.getSrcPath().contain(each.getSrcPath())) {
                    return new DataPorterCost(each, false, dataCost);
                }
            }
        }
        return null;
    }

    Template build() {
        SiSoTemplate result = new SiSoTemplate();
        Map<RevFormulaCall, FormulaCall> resultCall = new HashMap<>();
        Queue<DataPorterCost> queue = new LinkedList<>();
        for (DataPorterCost start : bestForTplInput) {
            queue.offer(start);
        }

        while (!queue.isEmpty()) {
            DataPorterCost seed = queue.poll();
            DataPorter porter = seed.getPorter();
            DataGate pro = seed.isReverse() ? porter.getRecipient() : porter.getProvider();
            DataGate rec = seed.isReverse() ? porter.getProvider() : porter.getRecipient();
            RevFormulaCall proRevCall = formulaOfDataGates.get(pro);
            RevFormulaCall recRevCall = formulaOfDataGates.get(rec);
            DataProvider dataProvider = getProvider(result, resultCall, proRevCall, queue);
            DataRecipient dataRecipient = getRecipient(result, resultCall, recRevCall, rec.getName(),queue);
            result.addDataPorter(dataProvider, dataRecipient,
                seed.isReverse() ? porter.getDstPath() : porter.getSrcPath(),
                seed.isReverse() ? porter.getSrcPath() : porter.getDstPath());
        }

        return result;
    }

    private DataProvider getProvider(SiSoTemplate result, Map<RevFormulaCall, FormulaCall> cache,
        RevFormulaCall revCall, Queue<DataPorterCost> queue) {
        if (revCall == null) {
            return result.getInput();
        }
        FormulaCall call = makeFormulaCall(result, revCall, cache, queue);
        return call.getOutput();
    }

    private DataRecipient getRecipient(Template result, Map<RevFormulaCall, FormulaCall> cache,
        RevFormulaCall revCall, String dataName, Queue<DataPorterCost> queue) {
        if (revCall == null) {
            return result.getOutput();
        }
        FormulaCall call = makeFormulaCall(result, revCall, cache, queue);
        return call.getInput(dataName);
    }

    private FormulaCall makeFormulaCall(Template tpl, RevFormulaCall revCall, Map<RevFormulaCall, FormulaCall> cache,
        Queue<DataPorterCost> queue) {
        FormulaCall call = cache.get(revCall);
        if (call == null) {
            call = register.makeNewCall(revCall.getFormulaName(), revCall.getResultName());
            cache.put(revCall, call);
            tpl.addFormulaCall(call);

            FormulaCall original = formulaRevMap.get(revCall);
            if (Objects.equals(original.getResultName(), revCall.getResultName())) {
                // 没有反转这个公式
                for (DataRecipient input : original.getInputs().values()) {
                    call.getInput(input.getName()).setConstant(input.getConstant());
                    for (DataPorter inputPorter : input.getInList()) {
                        queue.offer(new DataPorterCost(inputPorter, false, 0));
                    }
                }
            } else {
                // 反转了这个公式
                for (DataPorter outputPorter : original.getOutput().getOutList()) {
                    queue.offer(new DataPorterCost(outputPorter, true, 0));
                }

                for (DataRecipient input : original.getInputs().values()) {
                    if (Objects.equals(input.getName(), revCall.getResultName())) {
                        continue;
                    }
                    call.getInput(input.getName()).setConstant(input.getConstant());
                    for (DataPorter inputPorter : input.getInList()) {
                        queue.offer(new DataPorterCost(inputPorter, false, 0));
                    }
                }
            }
        }
        return call;
    }

    private boolean isBestPorter(DataPorter porter, int dataCost) {
        doneInTplInput.add(porter);
        SimplePath newPath = porter.getSrcPath();
        for (DataPorterCost exist : bestForTplInput) {
            SimplePath existPath = exist.getPorter().getSrcPath();
            if (existPath.contain(newPath)) {
                // 情况1：同一个数据，已经存在更好的
                // 情况2：newPath属于existPath的子集
                return false;
            }
        }

        // 情况3：newPath和existPath没有任何关系，是最好的
        // 情况4：newPath包含existPath，虽然existPath差一点，但带来更多的数据
        bestForTplInput.add(new DataPorterCost(porter, true, dataCost));
        return true;
    }
}
