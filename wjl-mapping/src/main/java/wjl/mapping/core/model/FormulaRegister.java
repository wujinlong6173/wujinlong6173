package wjl.mapping.core.model;

import java.util.Map;

/**
 * 公式管理器，负责注册公式。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
public interface FormulaRegister {
    /**
     * 创建一个公式调用。
     *
     * @param formulaName 公式名称
     * @param resultName 输出参数的名称
     * @return 新建的公式调用
     * @throws IllegalArgumentException 公式没有注册，或不支持计算输出参数
     */
    FormulaCall createCall(String formulaName, String resultName) throws IllegalArgumentException;

    /**
     * 获取计算公式参数需要的费用。
     *
     * @param formulaName 公式名称
     * @return 计算每个参数需要的费用，不可修改
     */
    Map<String, Integer> getParamsCost(String formulaName);
}
