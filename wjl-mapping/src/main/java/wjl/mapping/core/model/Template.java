package wjl.mapping.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据转换模板，从数据提供者获取数据，经过函数、公式转换后得到新的数据，
 * 将新数据传给数据接收者。
 *
 * @author wujinlong
 * @since 2021-8-1
 */
public class Template {
    /**
     * 一个或多个数据提供者。
     */
    private final Map<String, DataProvider> inputs;

    /**
     * 一个或多个数据接收者。
     */
    private final Map<String, DataRecipient> outputs;

    /**
     * 模板里面包含的公式调用。
     */
    private final List<FormulaCall> formulas;

    /**
     * 创建一个数据转换模板，只有一个数据提供者，和一个数据接受者的情况。
     *
     * @param inputName 数据提供者的名称
     * @param outputName 数据接收者的名称
     */
    public Template(String inputName, String outputName) {
        formulas = new ArrayList<>();
        inputs = new HashMap<>();
        outputs = new HashMap<>();
        inputs.put(inputName, new DataProvider(inputName));
        outputs.put(outputName, new DataRecipient(outputName));
    }

    /**
     * 创建一个数据转换模板，模板至少有一个数据提供者，和一个数据接受者。
     * 支持多个数据提供者，和多个数据接收者。
     *
     * @param inputNames 数据提供者的名称
     * @param outputNames 数据接收者的名称
     */
    public Template(Collection<String> inputNames, Collection<String> outputNames) {
        formulas = new ArrayList<>();
        inputs = new HashMap<>();
        outputs = new HashMap<>();
        for (String inputName : inputNames) {
            inputs.put(inputName, new DataProvider(inputName));
        }
        for (String outputName : outputNames) {
            outputs.put(outputName, new DataRecipient(outputName));
        }
    }

    public Map<String, DataProvider> getInputs() {
        return Collections.unmodifiableMap(inputs);
    }

    public DataProvider getInput(String inputName) {
        return inputs.get(inputName);
    }

    public Map<String, DataRecipient> getOutputs() {
        return Collections.unmodifiableMap(outputs);
    }

    public DataRecipient getOutput(String outputName) {
        return outputs.get(outputName);
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
