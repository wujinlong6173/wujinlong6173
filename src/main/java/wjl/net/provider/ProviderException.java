package wjl.net.provider;

import wjl.util.ErrorType;

public class ProviderException extends Exception {
    private static final long serialVersionUID = 1L;
    
    // 错误类型，区分输入参数错误
    private ErrorType errorType;

    public ProviderException(ErrorType errorType, String msg) {
        super(msg);
        this.errorType = errorType;
    }
    
    public ErrorType getErrorType() {
        return errorType;
    }
}
