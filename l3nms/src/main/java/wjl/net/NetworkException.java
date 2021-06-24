package wjl.net;

import wjl.util.ErrorType;

/**
 * 操作网络过程遇到的各种异常
 */
public class NetworkException extends Exception {
    private static final long serialVersionUID = 1L;
    
    // 错误类型，区分输入参数错误
    private ErrorType errorType;

    public NetworkException(ErrorType errorType, String msg) {
        super(msg);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
