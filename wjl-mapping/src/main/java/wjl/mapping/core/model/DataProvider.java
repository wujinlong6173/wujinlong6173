package wjl.mapping.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据提供者，模板的输入、公式的输出，都是数据提供者。
 */
public class DataProvider {
    /**
     * 向外输出数据。
     */
    private final List<DataPorter> outList = new ArrayList<>();

    void addOut(DataPorter porter) {
        outList.add(porter);
    }
}
