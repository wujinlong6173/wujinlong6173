package wjl.mapping.reverse;

import wjl.mapping.model.DataPorter;
import wjl.mapping.model.DataRecipient;
import wjl.mapping.model.FormulaCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 在反转算法表示一个公式调用，和模板中的公式调用一一对应。
 *
 * @author wujinlong
 * @since 2021-8-8
 */
class RevFormulaCall {
    private final FormulaCall call;

    // 公式的所有参数，包括输入和输出，键值必须是参数名
    private final Map<String, RevFormulaParam> allParams;

    // 公式计算每个参数的费用，费用为零表示没法计算此参数
    private final Map<String, Integer> formulaCost;

    // 选中的输出参数的名称
    private String resultName;

    // 计算输出参数的费用，等于公式本身的费用，加上所有输入参数的费用
    private int cost;

    RevFormulaCall(FormulaCall call, Map<String, Integer> formulaCost) {
        this.call = call;
        this.formulaCost = formulaCost;
        allParams = new HashMap<>();
        allParams.put(call.getOutput().getName(), new RevFormulaParam(call.getOutput()));
        for (Map.Entry<String, DataRecipient> input : call.getInputs().entrySet()) {
            allParams.put(input.getKey(), new RevFormulaParam(input.getValue()));
        }
    }

    boolean dataReady(DataPorterCost dpc) {
        RevFormulaParam param = findParamByPorter(dpc.getPorter(), dpc.isReverse());
        if (param == null) {
            return false;
        }

        if (!param.dataReady(dpc)) {
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

    private RevFormulaParam findParamByPorter(DataPorter porter, boolean reverse) {
        if (reverse) {
            return allParams.get(porter.getProvider().getName());
        } else {
            return allParams.get(porter.getRecipient().getName());
        }
    }

    FormulaCall getCall() {
        return call;
    }

    String getFormulaName() {
        return call.getName();
    }

    String getResultName() {
        return resultName;
    }

    int getCost() {
        return cost;
    }

    List<RevFormulaParam> getInputParams() {
        List<RevFormulaParam> inputOnly = new ArrayList<>(allParams.size());
        for (RevFormulaParam param : allParams.values()) {
            if (!Objects.equals(param.getName(), resultName)) {
                inputOnly.add(param);
            }
        }
        return inputOnly;
    }
}
