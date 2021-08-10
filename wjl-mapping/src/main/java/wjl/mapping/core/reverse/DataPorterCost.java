package wjl.mapping.core.reverse;

import wjl.mapping.core.model.DataPorter;
import wjl.mapping.core.model.SimplePath;

import java.util.Collections;
import java.util.List;

/**
 * 数据搬运工作为候选节点，费用等于源数据费用加搬运费用，搬运费用由
 * 源路径的复杂性决定。每个搬运工有两次机会称为候选节点，正向搬运，
 * 和反向搬运。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
class DataPorterCost extends CandidateCost {
    private final DataPorter porter;
    private final boolean reverse;

    /**
     * 数据搬运工作为候选节点。
     *
     * @param porter 原模板中的数据搬运工
     * @param reverse 标记正向搬运，或反向搬运
     * @param srcDataCost 源数据的费用
     */
    DataPorterCost(DataPorter porter, boolean reverse, int srcDataCost) {
        super(srcDataCost + porterCost(porter, reverse));
        this.porter = porter;
        this.reverse = reverse;
    }

    public DataPorter getPorter() {
        return porter;
    }

    public boolean isReverse() {
        return reverse;
    }

    private static int porterCost(DataPorter porter, boolean reverse) {
        return 1 + pathCost(reverse ? porter.getDstPath() : porter.getSrcPath());
    }

    private static int pathCost(SimplePath path) {
        return path.depth() + 10 * path.arrayIndex();
    }

    @Override
    List<? extends CandidateCost> newCandidate(RevTemplate revTpl) {
        RevFormulaCall revCall = revTpl.findRevCall(this);
        if (revCall == null) {
            return revTpl.dataReady(porter, reverse, getCost());
        } else if (revCall.dataReady(porter, reverse, getCost())) {
            FormulaCallCost fcc = new FormulaCallCost(revCall, revCall.getCost());
            return Collections.singletonList(fcc);
        } else {
            return null;
        }
    }
}
