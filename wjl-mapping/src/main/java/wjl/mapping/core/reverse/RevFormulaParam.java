package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataGate;
import wjl.mapping.core.model.DataProvider;
import wjl.mapping.core.model.DataRecipient;

public class RevFormulaParam {
    // 参数名称
    private final String name;

    // 公式的一个输入参数，可能需要多个搬运工提供数据
    private final int required;

    // 已经到达的搬运工总数
    private int provided;

    // 输入参数的总费用
    private int cost;

    public RevFormulaParam(String name, DataRecipient data) {
        this.name = name;
        this.required = data.getInList().size();
    }

    public RevFormulaParam(String name, DataProvider data) {
        this.name = name;
        this.required = data.getOutList().size();
    }

    public boolean dataReady(DataGate dataGate, int dataCost) {
        provided++;
        this.cost += dataCost;
        return provided >= required;
    }

    public boolean isReady() {
        return provided >= required;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }
}
