package wjl.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wjl.net.impl.DeviceImpl;
import wjl.net.impl.LinkImpl;
import wjl.net.impl.NetworkImpl;
import wjl.net.intent.Device;
import wjl.net.intent.Link;
import wjl.net.intent.Network;
import wjl.net.intent.Port;
import wjl.net.provider.DeviceProvider;
import wjl.net.provider.LinkProvider;
import wjl.net.provider.ProviderException;
import wjl.net.validator.CreateValidator;
import wjl.util.ErrorCollector;

import java.util.Map;
import java.util.UUID;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private final Network net = new Network();
    private final NetworkImpl netImpl = new NetworkImpl();

    /**
     * 创建设备的意图
     * 
     * @param name 设备的名称
     * @param provider 设备的供应商
     * @param inputs 供应商需要的参数
     * @return 设备的唯一标识
     */
    String createDevice(String name, DeviceProvider provider, Map<String,Object> inputs) {
        ErrorCollector error = new ErrorCollector();
        CreateValidator.checkObject(provider.getCreateSchema(), inputs, error);
        if (error.getErrors() != null) {
            LOGGER.error("创建设备的输入参数错误 {}", error.getErrors());
            return null;
        }

        try {
            String devId = UUID.randomUUID().toString();
            String outerId = provider.create(devId, inputs);
            DeviceImpl mapper = new DeviceImpl(devId, outerId, provider.getName(), inputs);
            netImpl.addDeviceImpl(mapper);
            Device dev = new Device();
            dev.setId(devId);
            dev.setName(name);
            net.addDevice(dev);
            return devId;
        } catch (ProviderException err) {
            LOGGER.error("创建设备失败", err);
            return null;
        }
    }

    /**
     * 创建端口的意图
     * 
     * @param devId 设备的标识
     * @param name 端口的名称，如果在设备内重复则报错
     * @param desc 端口的描述
     * @return 新端口的唯一标识
     */
    String createPort(String devId, String name, String desc) {
        Device dev = net.getDevice(devId);
        if (dev == null) {
            LOGGER.error("设备{}不存在。", devId);
            return null;
        }
        
        Port pt = new Port();
        pt.setDevId(devId);
        pt.setId(UUID.randomUUID().toString());
        pt.setName(name);
        pt.setDescription(desc);
        net.addPort(pt);
        dev.addPort(pt);
        return pt.getId();
    }
    
    /**
     * 创建链路的意图
     * 
     * @param srcPortId 源端口
     * @param dstPortId 目标端口
     * @param provider 链路的供应商
     * @param inputs 供应商需要的参数
     * @return 新链路的唯一标识
     */
    String createLink(String name, String srcPortId, String dstPortId, 
            LinkProvider provider, Map<String,Object> inputs) {
        Port srcPort = net.getPort(srcPortId);
        Port dstPort = net.getPort(dstPortId);
        if (srcPort == null || dstPort == null) {
            LOGGER.error("链路的端口不存在 {}-{}", srcPortId, dstPortId);
            return null;
        }
        
        DeviceImpl srcMapper = netImpl.getDeviceImpl(srcPort.getDevId());
        DeviceImpl dstMapper = netImpl.getDeviceImpl(dstPort.getDevId());

        ErrorCollector error = new ErrorCollector();
        CreateValidator.checkObject(provider.getCreateSchema(), inputs, error);
        if (error.getErrors() != null) {
            LOGGER.error("创建链路的输入参数错误 {}", error.getErrors());
            return null;
        }

        try {
            String linkId = UUID.randomUUID().toString();
            String outerId = provider.create(linkId,
                    srcMapper.getOuterId(), srcPort.getName(), srcMapper.getProvider(),
                    dstMapper.getOuterId(), dstPort.getName(), dstMapper.getProvider(), 
                    inputs);
            LinkImpl mapper = new LinkImpl(linkId, outerId, provider.getName(), inputs);
            netImpl.addLinkImpl(mapper);
            Link lk = new Link();
            lk.setId(linkId);
            lk.setName(name);
            lk.addPort(srcPort);
            lk.addPort(dstPort);
            net.addLink(lk);
            return linkId;
        } catch (ProviderException err) {
            LOGGER.error("创建链路失败", err);
            return null;
        }
    }

    Network getNet() {
        return net;
    }
}
