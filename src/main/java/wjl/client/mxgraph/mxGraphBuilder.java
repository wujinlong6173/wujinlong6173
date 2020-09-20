package wjl.client.mxgraph;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import wjl.net.NetworkApi;
import wjl.net.intent.Device;
import wjl.net.intent.Link;
import wjl.net.intent.Port;

import java.util.HashMap;
import java.util.Map;

public class mxGraphBuilder {
    /**
     * 根据网络数据构建mxGraph对象，为重建界面拓扑图做准备。
     *
     * @param net 网络数据
     * @return 拓扑图对象
     */
    public static mxGraph build(NetworkApi net) {
        mxGraph graph = new mxGraph();
        Map<String, mxCell> cellIndex = new HashMap<>();

        for (Device dev : net.getAllDevices()) {
            mxCellDevice mxDev = new mxCellDevice(dev.getName(), dev.getDipX(), dev.getDipY());
            mxDev.setId(dev.getId());
            graph.addCell(mxDev);
            cellIndex.put(dev.getId(), mxDev);
        }

        for (Port port : net.getAllPorts()) {
            int dir = getDirByPortName(port.getName());
            mxCell mxPort = new mxCellPort(port.getName(),
                    mxCellDevice.getPositionX(dir), mxCellDevice.getPositionY(dir));
            mxPort.setId(port.getId());
            graph.addCell(port, cellIndex.get(port.getDevId()));
            cellIndex.put(port.getId(), mxPort);
        }

        for (Link link : net.getAllLinks()) {
            if (link.getPorts().size() == 2) {
                Port src = link.getPorts().get(0);
                Port dst = link.getPorts().get(1);
                mxCellPort p1 = (mxCellPort)cellIndex.get(src.getId());
                mxCellPort p2 = (mxCellPort)cellIndex.get(dst.getId());
                mxCellLink mxLink = new mxCellLink(link.getName(), p1, p2);
                mxLink.setId(link.getId());
                graph.addEdge(link, null, p1, p2, null);
            }
        }

        return graph;
    }

    private static int getDirByPortName(String name) {
        return Integer.parseInt(name.substring(1));
    }
}
