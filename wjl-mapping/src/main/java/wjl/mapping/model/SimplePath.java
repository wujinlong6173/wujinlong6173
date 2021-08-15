package wjl.mapping.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * 属性名和整数下标构成的路径，表示 abc.items[2].name 这样的路径。
 *
 * @author wujinlong
 * @since 2021-8-7
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

    /**
     * 是否存在包含关系。
     *
     * @param sub 被包含的路径
     * @return 相等或包含时返回真
     */
    public boolean contain(SimplePath sub) {
        if (sub == null) {
            return false; // 容错
        } else if (this == sub) {
            return true; // 相同
        } else if (this.nodes.length > sub.nodes.length) {
            return false;
        } else {
            for (int idx = 0; idx < this.nodes.length; idx++) {
                if (!Objects.equals(this.nodes[idx], sub.nodes[idx])) {
                    return false;
                }
            }
            return true; // 相等或包含
        }
    }

    /**
     * 路径的深度，空路径的深度为零。
     *
     * @return 路径的深度
     */
    public int depth() {
        return nodes.length;
    }

    /**
     * 数组下标的个数。
     *
     * @return 数组下标的个数
     */
    public int arrayIndex() {
        int count = 0;
        for (Object node : nodes) {
            if (node instanceof Integer) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof SimplePath) {
            SimplePath otherPath = (SimplePath)other;
            return Arrays.equals(this.nodes, otherPath.nodes);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean withBrace) {
        StringBuilder sb = new StringBuilder();
        if (withBrace) {
            sb.append("${");
        }
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
        if (withBrace) {
            sb.append("}");
        }
        return sb.toString();
    }

    /**
     * 替换路径的前缀。
     *
     * @param path 原始的路径
     * @param oldPrefix 现在的前缀
     * @param newPrefix 新的前缀
     * @return 替换前缀后的新路径
     */
    public static SimplePath replacePrefix(SimplePath path, SimplePath oldPrefix, SimplePath newPrefix) {
        if (oldPrefix.nodes.length == path.nodes.length) {
            return newPrefix;
        }

        Object[] newNodes = new Object[path.nodes.length - oldPrefix.nodes.length + newPrefix.nodes.length];
        System.arraycopy(newPrefix.nodes, 0, newNodes, 0, newPrefix.nodes.length);
        System.arraycopy(path.nodes, newPrefix.nodes.length, newNodes, newPrefix.nodes.length,
            path.nodes.length - newPrefix.nodes.length);
        return new SimplePath(newNodes);
    }
}
