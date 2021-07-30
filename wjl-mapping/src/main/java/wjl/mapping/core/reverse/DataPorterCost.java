package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.SimplePath;

class DataPorterCost extends Candidate {
    private final DataPorter porter; // 数据搬运工
    private final boolean reverse; // 正向搬运，或反向搬运
    private final int cost; // 搬运到目的地的费用

    DataPorterCost(DataPorter porter, boolean reverse, int baseCost) {
        this.porter = porter;
        this.reverse = reverse;
        this.cost = baseCost + porterCost(porter, reverse);
    }

    public DataPorter getPorter() {
        return porter;
    }

    public boolean isReverse() {
        return reverse;
    }

    @Override
    public int getCost() {
        return cost;
    }

    private static int porterCost(DataPorter porter, boolean reverse) {
        return 1 + pathCost(reverse ? porter.getDstPath() : porter.getSrcPath());
    }

    private static int pathCost(SimplePath path) {
        return path.depth() + 10 * path.arrayIndex();
    }
}
