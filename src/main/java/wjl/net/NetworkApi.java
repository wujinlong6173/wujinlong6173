package wjl.net;

import java.util.UUID;

import wjl.net.impl.NetworkImpl;
import wjl.net.intent.Device;
import wjl.net.intent.Link;
import wjl.net.intent.Network;
import wjl.net.intent.Port;
import wjl.util.ErrorType;

/**
 * 网络对外的接口
 */
public class NetworkApi {
    private final Network intent;
    private final NetworkImpl impl;
    
    public NetworkApi() {
        intent = new Network();
        impl = new NetworkImpl();
    }
    
    /**
     * 创建一个设备意图
     * 
     * @param name 设备名称
     * @return 设备唯一标识
     */
    public String createDevice(String name) {
        String devId = UUID.randomUUID().toString();
        Device dev = new Device();
        dev.setId(devId);
        dev.setName(name);
        intent.addDevice(dev);
        return devId;
    }
    
    /**
     * 创建一个端口意图
     * 
     * @param devId 设备唯一标识
     * @param portName 端口名称
     * @param portDesc 端口描述，可选
     * @return 端口唯一标识
     * @throws NetworkException 设备不存在
     */
    public String createPort(String devId, String portName, String portDesc)
            throws NetworkException {

        Device dev = intent.getDevice(devId);
        if (dev == null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    String.format("device %s does not exist.", devId));
        }
        
        if (dev.getPort(portName) != null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    String.format("device %s already has port %s.", devId, portName));
        }
        
        Port pt = new Port();
        pt.setDevId(devId);
        pt.setId(UUID.randomUUID().toString());
        pt.setName(portName);
        pt.setDescription(portDesc);
        intent.addPort(pt);
        dev.addPort(pt);
        return pt.getId();
    }
    
    /**
     * 创建一条链路意图
     * 
     * @param name 链路名称
     * @param srcPortId 源端口唯一标识
     * @param dstPortId 目标端口唯一标识
     * @return 链路唯一标识
     * @throws NetworkException 端口不存在
     */
    public String createLink(String name, String srcPortId, String dstPortId) 
            throws NetworkException {

        Port srcPort = intent.getPort(srcPortId);
        Port dstPort = intent.getPort(dstPortId);
        if (srcPort == null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    String.format("source port %s does not exist.", srcPortId));
        } else if (dstPort == null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    String.format("destination port %s does not exist.", dstPortId));
        }
        
        String linkId = UUID.randomUUID().toString();
        Link lk = new Link();
        lk.setId(linkId);
        lk.setName(name);
        lk.addPort(srcPort);
        lk.addPort(dstPort);
        intent.addLink(lk);
        return linkId;
    }
    
    /**
     * 删除一条链路意图，支持重复删除
     * 
     * @param linkId 链路唯一标识
     * @throws NetworkException 不允许删除已部署的链路
     */
    public void deleteLink(String linkId) throws NetworkException {
        Link lk = intent.getLink(linkId);
        if (lk == null) {
            return;
        }
        
        if (lk.isDeploy()) {
            throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                    String.format("link %s is deployed, undeploy it first.", linkId));
        }
        
        lk.removeAllPorts();
        intent.delLink(lk.getId());
    }

    /**
     * 删除一个端口意图，支持重复删除
     * 
     * @param portId 端口唯一标识
     * @throws NetworkException 不允许删除绑定了链路的端口
     */
    public void deletePort(String portId) throws NetworkException {
        Port pt = intent.getPort(portId);
        if (pt == null) {
            return;
        }
        
        if (pt.getLinkId() != null) {
            throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                    String.format("port %s belong to link %s, delete link first.", portId, pt.getLinkId()));            
        }
        
        Device dev = intent.getDevice(pt.getDevId());
        if (dev != null) {
            dev.removePort(pt.getName());
        }
        
        intent.delPort(portId);
    }
    
    /**
     * 删除一个设备意图，支持重复删除
     * 
     * @param devId 设备唯一标识
     * @throws NetworkException 不允许删除有链路或已部署的设备
     */
    public void deleteDevice(String devId) throws NetworkException {
        Device dev = intent.getDevice(devId); 
        if (dev == null) {
            return;
        }

        if (dev.hasLinks()) {
            throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                    String.format("device %s has link, delete link first.", devId));
        }

        if (dev.isDeploy()) {
            throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                    String.format("device %s is deployed, undeploy it first.", devId));
        }
        
        for (Port pt : dev.getPorts()) {
            intent.delPort(pt.getId());
        }
        
        intent.delDevice(devId);
    }
}
