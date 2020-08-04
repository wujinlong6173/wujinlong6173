package wjl.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wjl.net.intent.Network;
import wjl.net.intent.Port;
import wjl.net.inventory.DeviceMapper;
import wjl.net.inventory.LinkMapper;
import wjl.net.inventory.MapperMgr;
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
    
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    /**
     * 创建设备的意图
     * 
     * @param devId 设备的标识
     * @param provider 设备的供应商
     * @param inputs 供应商需要的参数
     */
    void createDevice(String devId, DeviceProvider provider, Map<String,Object> inputs) {
        ErrorCollector error = new ErrorCollector();
        CreateValidator.checkObject(provider.getCreateSchema(), inputs, error);
        if (error.getErrors() != null) {
            LOGGER.error("创建设备的输入参数错误 {}", error.getErrors());
            return;
        }

        try {
            String outerId = provider.create(devId, inputs);
            DeviceMapper mapper = new DeviceMapper(devId, outerId, provider.getName(), inputs);
            MapperMgr.addDeviceMapper(mapper);
        } catch (ProviderException err) {
            LOGGER.error("创建设备失败", err);
        }
    }

    /**
     * 创建端口的意图
     * 
     * @param devId 设备的标识
     * @param name 端口的名称，如果在设备内重复则报错
     * @param desc 端口的描述
     */
    void createPort(String devId, String name, String desc) {
        Port pt = new Port();
        pt.setDevId(devId);
        pt.setId(UUID.randomUUID().toString());
        pt.setName(name);
        pt.setDescription(desc);
        net.addPort(pt);
    }
    
    /**
     * 创建链路的意图
     * 
     * @param srcDevId
     * @param dstDevId
     * @param provider
     * @param inputs
     */
    void createLink(String srcDevId, String dstDevId, LinkProvider provider, Map<String,Object> inputs) {
        DeviceMapper srcMapper = MapperMgr.getDeviceMapper(srcDevId);
        DeviceMapper dstMapper = MapperMgr.getDeviceMapper(dstDevId);
        if (srcMapper == null || dstMapper == null) {
            LOGGER.error("链路两端的设备不存在 {}-{}", srcDevId, dstDevId);
            return;
        }

        ErrorCollector error = new ErrorCollector();
        CreateValidator.checkObject(provider.getCreateSchema(), inputs, error);
        if (error.getErrors() != null) {
            LOGGER.error("创建链路的输入参数错误 {}", error.getErrors());
            return;
        }

        try {
            String linkId = UUID.randomUUID().toString();
            String outerId = provider.create(linkId,
                    srcMapper.getOuterId(), "eth0", srcMapper.getProvider(),
                    dstMapper.getOuterId(), "eth1", dstMapper.getProvider(), 
                    inputs);
            LinkMapper mapper = new LinkMapper(linkId, outerId, provider.getName(), inputs);
            MapperMgr.addLinkMapper(mapper);
        } catch (ProviderException err) {
            LOGGER.error("创建链路失败", err);
        }
    }

    Network getNet() {
        return net;
    }
}
