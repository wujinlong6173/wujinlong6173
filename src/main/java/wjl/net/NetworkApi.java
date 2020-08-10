package wjl.net;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import wjl.net.impl.DeviceImpl;
import wjl.net.impl.LinkImpl;
import wjl.net.impl.NetworkImpl;
import wjl.net.intent.Device;
import wjl.net.intent.Link;
import wjl.net.intent.Network;
import wjl.net.intent.Port;
import wjl.provider.DeviceProvider;
import wjl.provider.LinkProvider;
import wjl.provider.ProviderException;
import wjl.provider.ProviderLoader;
import wjl.net.validator.CreateValidator;
import wjl.util.ErrorCollector;
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
        
        if (srcPort.getLinkId() != null) {
            throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                    String.format("source port %s belongs to link %s.", srcPortId, srcPort.getLinkId()));
        } else if (dstPort.getLinkId() != null) {
            throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                    String.format("destination port %s belongs to link %s.", dstPortId, dstPort.getLinkId()));
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
    
    /**
     * 部署一台设备
     * 
     * @param devId 设备唯一标示
     * @param provider 设备供应商的名称
     * @param inputs 供应商需要的参数
     * @throws NetworkException 设备不存在或已部署，供应商名称错误，输入参数错误
     * @throws ProviderException 透传供应商的错误
     */
    public void deployDevice(String devId, String provider, Map<String,Object> inputs)
            throws NetworkException, ProviderException {
        
        Device dev = intent.getDevice(devId); 
        if (dev == null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    String.format("device %s does not exist.", devId));
        }

        DeviceImpl devImpl = impl.getDeviceImpl(devId);
        if (devImpl != null) {
            dev.setDeploy(true);
            throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                    String.format("device %s is already deployed.", devId));
        }

        DeviceProvider dp = ProviderLoader.getDeviceProvider(provider);
        if (dp == null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    String.format("%s is not a device provider.", provider));
        }
        
        ErrorCollector error = new ErrorCollector();
        CreateValidator.checkObject(dp.getCreateSchema(), inputs, error);
        if (error.getErrors() != null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    error.getErrors().toString());
        }

        String outerId = dp.create(devId, inputs);
        DeviceImpl mapper = new DeviceImpl(devId, outerId, provider, inputs);
        impl.addDeviceImpl(mapper);
        dev.setDeploy(true);
    }
    
    /**
     * 去部署一台设备
     * 
     * @param devId 设备唯一标识
     * @throws NetworkException 供应商不见了
     * @throws ProviderException 透传供应商的错误
     */
    public void undeployDevice(String devId) throws NetworkException, ProviderException {
        Device dev = intent.getDevice(devId); 
        if (dev == null) {
            return;
        }

        DeviceImpl devImpl = impl.getDeviceImpl(devId);
        if (devImpl == null) {
            dev.setDeploy(false);
            return;
        }

        for (Port pt : dev.getPorts()) {
            if (pt.getLinkId() == null) {
                continue;
            }
            Link lk = intent.getLink(pt.getLinkId());
            if (lk != null && lk.isDeploy()) {
                throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                        String.format("undeploy link %s first.", lk.getId()));
            }
        }

        DeviceProvider dp = ProviderLoader.getDeviceProvider(devImpl.getProvider());
        if (dp == null) {
            throw new NetworkException(ErrorType.SYSTEM_ERROR,
                    String.format("missing device provider %s.", devImpl.getProvider()));
        }
        
        dp.delete(devImpl.getOuterId(), devImpl.getInputs());
        impl.delDeviceImpl(devId);
        dev.setDeploy(false);
    }
    
    /**
     * 部署一条链路
     * 
     * @param linkId 链路唯一标识
     * @param provider 链路供应商的名称
     * @param inputs 供应商需要的参数
     * @throws NetworkException 链路不存在或已部署，供应商名称错误，输入参数错误
     * @throws ProviderException
     */
    public void deployLink(String linkId, String provider, Map<String,Object> inputs)
            throws NetworkException, ProviderException {
        
        Link lk = intent.getLink(linkId);
        if (lk == null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    String.format("link %s does not exist.", linkId));
        }
        
        LinkImpl lkImpl = impl.getLinkImpl(linkId);
        if (lkImpl != null) {
            lk.setDeploy(true);
            throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                    String.format("link %s is already deployed.", linkId));
        }
        

        LinkProvider dp = ProviderLoader.getLinkProvider(provider);
        if (dp == null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    String.format("%s is not a link provider.", provider));
        }
        
        ErrorCollector error = new ErrorCollector();
        CreateValidator.checkObject(dp.getCreateSchema(), inputs, error);
        if (error.getErrors() != null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    error.getErrors().toString());
        }
        
        // 到目前为止，肯定是两个端口
        List<Port> ports = lk.getPorts();
        Port src = ports.get(0);
        Port dst = ports.get(1);
        DeviceImpl srcDevImpl = impl.getDeviceImpl(src.getDevId());
        DeviceImpl dstDevImpl = impl.getDeviceImpl(dst.getDevId());
        if (srcDevImpl == null || dstDevImpl == null) {
            throw new NetworkException(ErrorType.SERVICE_CONSTRAIN,
                    String.format("deploy devices of link %s first.", linkId));
        }
        
        String outerId = dp.create(linkId,
                srcDevImpl.getOuterId(), src.getName(), srcDevImpl.getProvider(),
                dstDevImpl.getOuterId(), dst.getName(), dstDevImpl.getProvider(), 
                inputs);
        LinkImpl mapper = new LinkImpl(linkId, outerId, provider, inputs);
        impl.addLinkImpl(mapper);
        lk.setDeploy(true);
    }
    
    /**
     * 去部署一条链路
     * 
     * @param linkId 链路唯一标识
     * @throws NetworkException 供应商不见了
     * @throws ProviderException 透传供应商的错误
     */
    public void undeployLink(String linkId) throws NetworkException, ProviderException {
        
        Link lk = intent.getLink(linkId);
        if (lk == null) {
            return;
        }
        
        LinkImpl lkImpl = impl.getLinkImpl(linkId);
        if (lkImpl == null) {
            lk.setDeploy(false);
            return;
        }
        

        LinkProvider dp = ProviderLoader.getLinkProvider(lkImpl.getProvider());
        if (dp == null) {
            throw new NetworkException(ErrorType.INPUT_ERROR,
                    String.format("missing link provider %s.", lkImpl.getProvider()));
        }
        
        dp.delete(lkImpl.getOuterId(), lkImpl.getInputs());
        impl.delLinkImpl(linkId);
        lk.setDeploy(false);
    }
}
