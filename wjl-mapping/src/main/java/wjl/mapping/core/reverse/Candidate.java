package wjl.mapping.core.reverse;

abstract class Candidate implements Comparable<Candidate> {
    abstract  int getCost();

    @Override
    public int compareTo(Candidate other) {
        return Integer.compare(this.getCost(), other.getCost());
    }
}
