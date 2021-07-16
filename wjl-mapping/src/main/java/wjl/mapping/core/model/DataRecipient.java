package wjl.mapping.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据接收者，模板的输出、公式的每个输入参数，都是数据接收者。
 */
public class DataRecipient {
    /**
     * 接收的常量数据，模板输出的常量部分，公式输入参数的常量部分。
     */
    private Object constant;

    /**
     * 接收外部的数据。
     */
    private final List<DataPorter> inList = new ArrayList<>();

    public Object getConstant() {
        return constant;
    }

    public void setConstant(Object constant) {
        this.constant = constant;
    }

    void addIn(DataPorter porter) {
        inList.add(porter);
    }

    public List<DataPorter> getInList() {
        return inList;
    }
}
