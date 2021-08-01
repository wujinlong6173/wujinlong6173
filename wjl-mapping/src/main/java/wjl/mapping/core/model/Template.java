package wjl.mapping.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据转换模板。
 */
public class Template {
    /**
     * 模板的数据源，支持多个数据源。
     */
    private final Map<String, DataProvider> inputs;

    /**
     * 模板的输出数据。
     */
    private final DataRecipient output;

    /**
     * 模板里面包含的公式调用。
     */
    private final List<FormulaCall> formulas;

    public Template(String... inputNames) {
        output = new DataRecipient("output");
        formulas = new ArrayList<>();
        inputs = new HashMap<>();
        for (String inputName : inputNames) {
            inputs.put(inputName, new DataProvider(inputName));
        }
    }

    public Map<String, DataProvider> getInputs() {
        return Collections.unmodifiableMap(inputs);
    }

    public DataProvider getInput(String inputName) {
        return inputs.get(inputName);
    }

    public DataRecipient getOutput() {
        return output;
    }

    public void addFormulaCall(FormulaCall call) {
        formulas.add(call);
    }

    public List<FormulaCall> getFormulas() {
        return Collections.unmodifiableList(formulas);
    }

    /**
     * 添加一条复制数据的路线。
     *
     * @param provider 数据提供者
     * @param recipient 数据接收者
     * @param srcPath 源路径
     * @param dstPath 目标路径
     * @return 数据搬运工
     */
    public DataPorter addDataPorter(DataProvider provider, DataRecipient recipient, SimplePath srcPath, SimplePath dstPath) {
        return new DataPorter(provider, recipient, srcPath, dstPath);
    }
}
