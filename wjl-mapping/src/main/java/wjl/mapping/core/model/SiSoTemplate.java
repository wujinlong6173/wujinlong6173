package wjl.mapping.core.model;

/**
 * Single Input Single Output Template.
 */
public class SiSoTemplate extends Template {
    private static final String DEFAULT_INPUT_NAME = "input";

    public SiSoTemplate() {
        super(DEFAULT_INPUT_NAME);
    }

    public DataProvider getInput() {
        return getInput(DEFAULT_INPUT_NAME);
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
