package wjl.mapping.model;

/**
 * 对函数的调用。
 */
public class FunctionCall extends DataConverter {
    /**
     * 函数的输出。
     */
    private final DataProvider output;

    /**
     * 函数的输入
     */
    private final DataRecipient input;

    /**
     * 生成一个函数调用。
     *
     * @param callName 函数调用的名称
     */
    public FunctionCall(String callName) {
        super(callName);
        this.output = new DataProvider("output");
        this.input = new DataRecipient("input");
    }

    public DataProvider getOutput() {
        return output;
    }

    public DataRecipient getInput() {
        return input;
    }
}
