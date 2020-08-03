package wjl.net.provider;

public class ProviderException extends Exception {
    public static final int OBJECT_NOT_EXIST = 4001;
    public static final int NO_USABLE_RESOURCE = 5001;
    
    // 错误类型，区分输入参数错误
    private int errorType;

    public ProviderException(int errorType, String msg) {
        super(msg);
        this.errorType = errorType;
    }
}
