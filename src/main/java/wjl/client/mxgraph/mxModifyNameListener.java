package wjl.client.mxgraph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import wjl.net.NetworkApi;

public class mxModifyNameListener implements mxEventSource.mxIEventListener {
    private final NetworkApi net;

    public mxModifyNameListener(NetworkApi net) {
        this.net = net;
    }

    @Override
    public void invoke(Object sender, mxEventObject evt) {
        Object chg = evt.getProperties().get("change");
        if (chg instanceof mxGraphModel.mxValueChange) {
            mxGraphModel.mxValueChange chgValue = (mxGraphModel.mxValueChange)chg;
            mxCell cell = (mxCell)chgValue.getCell();
            if (!onValueChange(cell)) {
                // 如果不允许修改名称，则撤销对mxCell的修改
                cell.setValue(chgValue.getPrevious());
            }
        }
    }

    private boolean onValueChange(mxCell cell) {
        if (cell instanceof mxCellDevice) {
            return net.modifyDeviceName(cell.getId(), (String)cell.getValue());
        } else if (cell instanceof mxCellPort) {
            return net.modifyPortName(cell.getId(), (String)cell.getValue());
        } else if (cell instanceof mxCellLink) {
            return net.modifyLinkName(cell.getId(), (String)cell.getValue());
        } else {
            return true;
        }
    }
}
