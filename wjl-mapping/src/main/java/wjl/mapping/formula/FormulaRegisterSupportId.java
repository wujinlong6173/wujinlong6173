package wjl.mapping.formula;

import java.util.Map;

/**
 * 在调用公式时，支持name.id这种格式的公式名称，其中name是公式名称，
 * id是公式调用的编号。给所有公式调用编号后，很容易比较两个模板是否
 * 相同，判断反转算法生成的模板和期望的模板是否相同。
 *
 * @author wujinlong
 * @since 2021-8-1
 */
public class FormulaRegisterSupportId extends DefaultFormulaRegister {
    @Override
    public Map<String, Integer> getParamsCost(String nameAndId) {
        int ptr = nameAndId.indexOf('.');
        String formulaName = ptr >= 0 ? nameAndId.substring(0, ptr) : nameAndId;
        return super.getParamsCost(formulaName);
    }
}
