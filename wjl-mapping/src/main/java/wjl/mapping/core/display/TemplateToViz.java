package wjl.mapping.core.display;

import wjl.mapping.core.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 使用GraphViz工具显示转换模板，主要显示数据流向。
 * 这里只输出字符串，在其它地方转换成图像。
 */
public class TemplateToViz {
    private final StringBuffer sb = new StringBuffer();
    private int allocClusterId = 0;
    private Map<Object, Integer> clusterMapping;
    private int allocNodeId = 0;
    private Map<String, Integer> nodeMapping;
    private Map<Integer, Integer> anyNodeInCluster;

    public String display(Template tpl) {
        sb.delete(0, sb.length());
        clusterMapping = new HashMap<>();
        nodeMapping = new HashMap<>();
        anyNodeInCluster = new HashMap<>();

        sb.append("digraph {\n" +
            "compound=true;\n" +
            "splines=curved;\n" +
            "graph[rankdir=LR];\n" +
            "node[shape=record,style=filled,color=white];\n");
        showTemplate(tpl);
        sb.append("}\n");
        return sb.toString();
    }

    private void showTemplate(Template tpl) {
        showDataProvider("input", tpl.getInput());
        showDataRecipient("output", tpl.getOutput());
        for (Map.Entry<Integer, FormulaCall> call : tpl.getFormulas().entrySet()) {
            showFormulaCall(call.getKey(), call.getValue());
        }
        for (DataPorter porter : tpl.getInput().getOutList()) {
            showDataPorter(porter);
        }
        for (FormulaCall call : tpl.getFormulas().values()) {
            for (DataPorter porter : call.getOutput().getOutList()) {
                showDataPorter(porter);
            }
        }
    }

    private int showDataProvider(String label, DataProvider provider) {
        int cid = getCluster(provider);
        sb.append("subgraph cluster").append(cid).append("{\n");
        sb.append("label=").append(label).append("\n");
        sb.append("color=lightgrey;style=filled;\n");

        int nodeId = 0;
        for (DataPorter porter : provider.getOutList()) {
            String path = porter.getSrcPath().toString(false);
            nodeId = getNodeId(cid, path);
            sb.append("N").append(nodeId)
                .append(" [label=\"").append(path).append("\"]\n");
        }

        sb.append("}\n");
        anyNodeInCluster.put(cid, nodeId);
        return cid;
    }

    private int showDataRecipient(String label, DataRecipient recipient) {
        int cid = getCluster(recipient);
        sb.append("subgraph cluster").append(cid).append("{\n");
        sb.append("label=").append(label).append("\n");
        sb.append("color=lightgrey;\nstyle=filled;\n");

        int nodeId = 0;
        for (DataPorter porter : recipient.getInList()) {
            String path = porter.getDstPath().toString(false);
            nodeId = getNodeId(cid, path);
            sb.append("N").append(nodeId)
                .append(" [label=\"").append(path).append("\"]\n");
        }

        if (nodeId == 0) {
            nodeId = getNodeId(cid, "const");
            String cons = showConstant(recipient.getConstant());
            sb.append("N").append(nodeId)
                .append(" [label=\"").append(cons).append("\"]\n");
        }

        sb.append("}\n");
        anyNodeInCluster.put(cid, nodeId);
        return cid;
    }

    private void showFormulaCall(Integer id, FormulaCall call) {
        sb.append("C").append(id)
            .append(" [shape=ellipse,color=greenyellow,label=\"")
            .append(call.getFormulaName())
            .append("\"]\n");
        int cid = showDataProvider(call.getResultName(), call.getOutput());
        sb.append("C").append(id)
            .append(" -> N").append(anyNodeInCluster.get(cid))
            .append(" [lhead=cluster").append(cid)
            .append(",arrowhead=vee,weight=10]\n");
        for (Map.Entry<String, DataRecipient> input : call.getInputs().entrySet()) {
            cid = showDataRecipient(input.getKey(), input.getValue());
            sb.append("N").append(anyNodeInCluster.get(cid))
                .append(" -> C").append(id)
                .append(" [ltail=cluster").append(cid)
                .append(",arrowhead=vee,weight=10]\n");
        }
    }

    private void showDataPorter(DataPorter porter) {
        int srcCid = getCluster(porter.getProvider());
        int dstCid = getCluster(porter.getRecipient());
        int srcNode = getNodeId(srcCid, porter.getSrcPath().toString(false));
        int dstNode = getNodeId(dstCid, porter.getDstPath().toString(false));
        sb.append("N").append(srcNode)
            .append(" -> N").append(dstNode)
            .append(" [arrowhead=vee,weight=1]\n");
    }

    private String showConstant(Object constant) {
        if (constant == null) {
            return "null";
        } else if (constant instanceof Map) {
            return "{...}";
        } else if (constant instanceof List) {
            return "[...]";
        } else {
            return constant.toString();
        }
    }

    private int getCluster(Object obj) {
        Integer cid = clusterMapping.get(obj);
        if (cid != null) {
            return cid;
        }

        ++allocClusterId;
        clusterMapping.put(obj, allocClusterId);
        return allocClusterId;
    }

    private int getNodeId(int cid, String path) {
        String nodeKey = String.format(Locale.ENGLISH, "%d-%s", cid, path);
        Integer nid = nodeMapping.get(nodeKey);
        if (nid != null) {
            return nid;
        }

        ++allocNodeId;
        nodeMapping.put(nodeKey, allocNodeId);
        return allocNodeId;
    }
}
