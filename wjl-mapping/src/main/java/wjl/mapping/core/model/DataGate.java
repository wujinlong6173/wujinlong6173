package wjl.mapping.core.model;

public interface DataGate {
    /**
     * 唯一性要求：如果属于公式，则在一个公式内唯一；如果是模板的输入输出，
     * 则在模板内唯一。
     *
     * @return 名称
     */
    String getName();
}
