package wjl.mapping.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据提供者，模板的输入、公式的输出，都是数据提供者。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
public class DataProvider {
    private final String name;

    /**
     * 向外输出数据。
     */
    private final List<DataPorter> outList;

    public DataProvider(String name) {
        this.name = name;
        outList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addOut(DataPorter porter) {
        outList.add(porter);
    }

    public List<DataPorter> getOutList() {
        return outList;
    }
}
