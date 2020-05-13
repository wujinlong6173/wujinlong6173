package wjl.net.provider;

public class ProviderException extends Exception {
    // 错误类型，区分输入参数错误
    private int errorType;

    public ProviderException(int errorType, String msg) {
        super(msg);
        this.errorType = errorType;
    }
}
