package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.SimplePath;

public final class CostHelper {
    /**
     * 搬运的费用 = 1 + 取数据的费用，取数据的费用衡量数据的可信程度。
     *
     * @param porter 搬运工
     * @param reverse 正向搬运，或反向搬运
     * @return 搬运的费用
     */
    static int porterCost(DataPorter porter, boolean reverse) {
        return 1 + pathCost(reverse ? porter.getDstPath() : porter.getSrcPath());
    }

    private static int pathCost(SimplePath path) {
        return path.depth() + 10 * path.arrayIndex();
    }
}
