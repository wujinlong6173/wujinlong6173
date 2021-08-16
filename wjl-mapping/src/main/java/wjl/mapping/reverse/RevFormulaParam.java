package wjl.mapping.reverse;

import wjl.mapping.model.DataProvider;
import wjl.mapping.model.DataRecipient;

import java.util.ArrayList;
import java.util.List;

/**
 * 在反转算法表示公式调用的一个参数，记录需要的数据和已到达的数据。
 * 当需要的数据全部到达后，就说该参数已经准备好了。
 *
 * @author wujinlong
 * @since 2021-8-8
 */
class RevFormulaParam {
    // 参数名称
    private final String name;

    // 公式的一个输入参数，可能需要多个搬运工提供数据
    private final int required;

    // 已经到达的搬运工总数
    private final List<DataPorterCost> provided = new ArrayList<>();

    // 输入参数的总费用
    private int cost;

    RevFormulaParam(DataRecipient data) {
        this.name = data.getName();
        this.required = data.getInList().size();
    }

    RevFormulaParam(DataProvider data) {
        this.name = data.getName();
        this.required = data.getOutList().size();
    }

    /**
     * 有一份数据准备好了。
     *
     * @param dpc 提供数据的搬运工
     * @return 该参数有没有准备好
     */
    boolean dataReady(DataPorterCost dpc) {
        provided.add(dpc);
        cost += dpc.getCost();
        return provided.size() >= required;
    }

    /**
     * 如果所有数据都准备好了，则该参数准备好了。
     *
     * @return 该参数有没有准备好
     */
    boolean isReady() {
        return provided.size() >= required;
    }

    int getCost() {
        return cost;
    }

    String getName() {
        return name;
    }

    List<DataPorterCost> getProvided() {
        return provided;
    }
}
