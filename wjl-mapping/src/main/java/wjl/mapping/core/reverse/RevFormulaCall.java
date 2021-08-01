package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataGate;
import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.DataRecipient;
import wjl.mapping.core.model.FormulaCall;

import java.util.HashMap;
import java.util.Map;

class RevFormulaCall {
    // 公式的名称
    private final String formulaName;

    // 公式的所有参数，包括输入和输出
    private final Map<DataGate, RevFormulaParam> allParams;

    // 公式计算每个参数的费用，费用为零表示没法计算此参数
    private final Map<String, Integer> formulaCost;

    // 选中的输出参数的名称
    private String resultName;

    // 计算输出参数的费用，等于公式本身的费用，加上所有输入参数的费用
    private int cost;

    RevFormulaCall(FormulaCall call, Map<String, Integer> formulaCost) {
        this.formulaName = call.getFormulaName();
        this.formulaCost = formulaCost;
        allParams = new HashMap<>();
        allParams.put(call.getOutput(),
            new RevFormulaParam(call.getResultName(), call.getOutput()));
        for (Map.Entry<String, DataRecipient> input : call.getInputs().entrySet()) {
            allParams.put(input.getValue(),
                new RevFormulaParam(input.getKey(), input.getValue()));
        }
    }

    boolean dataReady(DataPorter porter, boolean reverse, int dataCost) {
        DataGate data = reverse ? porter.getProvider() : porter.getRecipient();
        RevFormulaParam param = allParams.get(data);
        if (param == null) {
            return false;
        }

        if (!param.dataReady(data, dataCost)) {
            return false;
        }

        int sum = 0;
        RevFormulaParam notReady = null;
        for (RevFormulaParam each : allParams.values()) {
            if (each.isReady()) {
                sum += each.getCost();
            } else if (notReady != null) {
                // 还有多个参数没准备好
                return false;
            } else {
                // 发现有一个参数没准备好
                notReady = each;
            }
        }

        if (notReady != null) {
            // 只有一个参数没准备好的情况
            Integer calcCost = formulaCost.get(notReady.getName());
            if (calcCost == null || calcCost == 0) {
                // 不支持计算此参数
                return false;
            }
            resultName = notReady.getName();
            this.cost = sum + calcCost;
            return true;
        }

        // 所有参数都准备好了，挑选费用最小的作为输出
        int min = Integer.MAX_VALUE;
        for (RevFormulaParam each : allParams.values()) {
            Integer calcCost = formulaCost.get(each.getName());
            if (calcCost == null || calcCost == 0) {
                // 不支持计算此参数
                continue;
            }
            int tempCost = sum - each.getCost() + calcCost;
            if (tempCost < min) {
                min = tempCost;
                resultName = each.getName();
            }
        }

        if (cost == 0 || min < cost) {
            cost = min;
            return true;
        }

        // 前面已经选过输出参数，新选的输出参数没有优势
        return false;
    }

    public String getFormulaName() {
        return formulaName;
    }

    String getResultName() {
        return resultName;
    }

    int getCost() {
        return cost;
    }
}
