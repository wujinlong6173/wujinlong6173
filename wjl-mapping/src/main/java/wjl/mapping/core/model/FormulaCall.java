package wjl.mapping.core.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * 对公式的调用。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
public class FormulaCall {
    /**
     * 公式的名称。
     */
    private final String formulaName;

    /**
     * 公式的输出。
     */
    private final DataProvider output;

    /**
     * 公式的所有输入参数。
     */
    private final Map<String, DataRecipient> inputs = new HashMap<>();

    /**
     * 生成一个公式调用。
     *
     * @param formulaName 公式的名称
     * @param resultName 输出参数的名称
     */
    public FormulaCall(String formulaName, String resultName, Collection<String> allParams) {
        this.formulaName = formulaName;
        this.output = new DataProvider(resultName);
        for (String inputParam : allParams) {
            if (!Objects.equals(resultName, inputParam)) {
                inputs.put(inputParam, new DataRecipient(inputParam));
            }
        }
    }

    public String getFormulaName() {
        return formulaName;
    }

    public String getResultName() {
        return output.getName();
    }

    public DataProvider getOutput() {
        return output;
    }

    public Map<String, DataRecipient> getInputs() {
        return Collections.unmodifiableMap(inputs);
    }

    public DataRecipient getInput(String inputName) throws IllegalArgumentException {
        DataRecipient ret = inputs.get(inputName);
        if (ret == null) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH,
                    "invalid input parameter name %s(%s).", formulaName, inputName));
        }
        return ret;
    }
}
