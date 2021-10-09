package wjl.mapping.model;

/**
 * 数据转换器的基类，函数调用、公式调用、模板调用都是转换器的实现。
 */
public abstract class DataConverter {
    private final String name;

    /**
     *
     * @param name 调用的名称，函数名或公式名加上编号
     */
    public DataConverter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
