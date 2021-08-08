package wjl.mapping.core.model;

/**
 * Single Input Single Output Template.
 *
 * @author wujinlong
 * @since 2021-8-7
 */
public class SiSoTemplate extends Template {
    private static final String DEFAULT_INPUT_NAME = "input";
    private static final String DEFAULT_OUTPUT_NAME = "output";

    public SiSoTemplate() {
        super(DEFAULT_INPUT_NAME, DEFAULT_OUTPUT_NAME);
    }

    public DataProvider getInput() {
        return getInput(DEFAULT_INPUT_NAME);
    }

    public DataRecipient getOutput() {
        return getOutput(DEFAULT_OUTPUT_NAME);
    }

    /**
     * 从模板输入复制数据到模板输出。
     *
     * @param srcPath 源路径
     * @param dstPath 目标路径
     * @return 数据搬运工
     */
    public DataPorter addDataPorter(SimplePath srcPath, SimplePath dstPath) {
        return new DataPorter(getInput(DEFAULT_INPUT_NAME), getOutput(), srcPath, dstPath);
    }
}
