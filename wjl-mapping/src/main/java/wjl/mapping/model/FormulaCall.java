package wjl.mapping.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 对公式的调用。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
public class FormulaCall extends DataConverter {
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
     * @param callName 公式调用的名称
     * @param resultName 输出参数的名称
     */
    public FormulaCall(String callName, String resultName, Collection<String> allParams) {
        super(callName);
        this.output = new DataProvider(resultName);
        for (String inputParam : allParams) {
            if (!Objects.equals(resultName, inputParam)) {
                inputs.put(inputParam, new DataRecipient(inputParam));
            }
        }
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

    public DataRecipient getInput(String inputName) {
        return inputs.get(inputName);
    }
}
