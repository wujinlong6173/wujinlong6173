package wjl.mapping.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据转换模板。
 */
public class Template {
    /**
     * 模板的输入数据。
     */
    private final DataProvider input = new DataProvider();

    /**
     * 模板的输出数据。
     */
    private final DataRecipient output = new DataRecipient();

    private final List<FormulaCall> formulas = new ArrayList<>();

    public DataProvider getInput() {
        return input;
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
     * 从模板输入复制数据到模板输出。
     *
     * @param srcPath 源路径
     * @param dstPath 目标路径
     * @return 数据搬运工
     */
    public DataPorter addDataPorter(SimplePath srcPath, SimplePath dstPath) {
        return new DataPorter(input, output, srcPath, dstPath);
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
