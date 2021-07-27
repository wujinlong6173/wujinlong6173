package wjl.mapping.core.reverse;

public class DataCost implements Comparable<DataCost> {

    private int cost;

    @Override
    public int compareTo(DataCost other) {
        return Integer.compare(this.cost, other.cost);
    }
}
