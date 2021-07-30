package wjl.mapping.core.model;

import java.util.Map;

/**
 * 公式的执行器。
 */
public interface FormulaExecutor {
    /**
     * 根据公式计算一个参数的值。
     *
     * @param formulaName 公式名称
     * @param resultName 需要计算的参数名
     * @param inputs 其它参数的值
     * @return 执行结果
     */
    Object invoke(String formulaName, String resultName, Map<String, Object> inputs);
}
