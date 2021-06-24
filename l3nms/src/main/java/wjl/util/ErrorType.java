package wjl.util;

public enum ErrorType {
    OPER_ERROR(1, "用户操作错误"),
    INPUT_ERROR(2, "输入参数错误"),
    SYSTEM_LIMMIT(3, "系统实现局限"),
    SYSTEM_ERROR(4, "系统内部错误"),
    SERVICE_CONSTRAIN(5, "业务约束"),
    NO_USABLE_RESOURCE(6, "网络资源不够");
    
    private final int value;
    private final String desc;

    private ErrorType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
