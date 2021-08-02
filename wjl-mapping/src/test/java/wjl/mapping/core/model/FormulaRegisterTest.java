package wjl.mapping.core.model;

import java.util.Map;

/**
 * 用于单元测试的公式注册器，支持name.id这种格式的公式名称，
 * 其中name是公式名称，id是公式调用的编号。给所有公式调用编
 * 号后，很容易比较两个模板是否相同，判断反转算法生成的模板
 * 和期望的模板是否相同。
 *
 * @author wujinlong
 * @since 2021-8-1
 */
public class FormulaRegisterTest extends FormulaRegister {
    @Override
    public Map<String, Integer> getParamsCost(String nameAndId) {
        String formulaName = nameAndId.substring(0, nameAndId.indexOf('.'));
        return super.getParamsCost(formulaName);
    }
}
