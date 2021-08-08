package wjl.mapping.core.reverse;

/**
 * 最短路网算法，候选节点的费用，所有候选节点放在一个优先队列中。
 *
 * @author wujinlong
 * @since 2021-8-7
 */
abstract class CandidateCost implements Comparable<CandidateCost> {
    // 计算数据的费用，越小越优
    private final int cost;

    CandidateCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(CandidateCost other) {
        return Integer.compare(this.cost, other.cost);
    }

    public int getCost() {
        return cost;
    }
}
