package wjl.mapping.core.model;

/**
 * 属性名和整数下标构成的路径，表示 abc.items[2].name 这样的路径。
 */
public class SimplePath {
    public static final SimplePath EMPTY = new SimplePath();

    private final Object[] nodes;

    /**
     *
     * @param nodes 字符串和整数组成的数组
     */
    public SimplePath(Object ... nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("${");
        boolean requireDot = false;
        for (Object node : nodes) {
            if (node instanceof Integer) {
                sb.append("[").append(node).append("]");
            } else if (node instanceof String) {
                String str = (String)node;
                if (str.contains(".")) {
                    sb.append("[\"").append(node).append("\"]");
                } else {
                    if (requireDot) {
                        sb.append(".");
                    }
                    sb.append(node);
                }
            }
            requireDot = true;
        }
        sb.append("}");
        return sb.toString();
    }
}
