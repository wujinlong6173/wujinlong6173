package wjl.mapping.formula;

import wjl.mapping.model.FormulaCall;
import wjl.mapping.model.FormulaRegister;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 默认的公式管理器。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
public class DefaultFormulaRegister implements FormulaRegister {
    /**
     * 公式名称，参数名称，计算该参数需要的费用。
     * 费用为零表示没法计算此参数，费用不可能小于零。
     */
    private final Map<String, Map<String, Integer>> allFormula = new HashMap<>();

    /**
     * 注册一个公式。
     *
     * @param name 公式的名称
     * @param paramsCost 公式的所有参数名，和计算每个参数的费用。
     */
    public void register(String name, Map<String, Integer> paramsCost) {
        allFormula.put(name, paramsCost);
    }

    /**
     * 创建一个公式调用，必须是已注册的函数。
     *
     * @param formulaName 公式名称
     * @param resultName 输出参数的名称
     * @return 新建的公式调用
     * @throws IllegalArgumentException 公式名称或输出参数有误
     */
    @Override
    public FormulaCall createCall(String formulaName, String resultName) throws IllegalArgumentException {
        Map<String, Integer> paramsCost = getParamsCost(formulaName);
        if (paramsCost == null) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH,
                    "invalid formula name %s.", formulaName));
        }

        Integer cost = paramsCost.get(resultName);
        if (cost == null) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH,
                    "invalid parameter name %s(%s).", formulaName, resultName));
        } else if (cost <= 0) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH,
                    "parameter can not be result %s(%s).", formulaName, resultName));
        } else {
            return new FormulaCall(formulaName, resultName, paramsCost.keySet());
        }
    }

    /**
     * 获取计算公式参数需要的费用。
     *
     * @param formulaName 公式名称
     * @return 计算每个参数需要的费用，不可修改
     */
    @Override
    public Map<String, Integer> getParamsCost(String formulaName) {
        Map<String, Integer> paramsCost = allFormula.get(formulaName);
        if (paramsCost == null) {
            return null;
        }
        return Collections.unmodifiableMap(paramsCost);
    }
}
