package wjl.provider;

import java.util.Map;

/**
 * 跨运营商创建链路的场景，运营商向互联公司提供创建端口的接口。
 * PortProvider只给LinkProvider调用，禁止以其它方式调用。
 * 在运营商内部，也可以让PortProvider配合LinkProvider完成链路创建过程。
 */
public interface PortProvider extends ProductProvider {
    /**
     * 创建一个端口
     *
     * @param deviceId 新创建的端口绑定到这台设备
     * @param portName 端口的逻辑名称
     * @param inputs 要求的输入参数
     * @return 端口标识，作为删除端口的依据
     * @throws ProviderException
     */
    String create(String deviceId, String portName, Map<String,Object> inputs) throws ProviderException;

    /**
     * 删除一个端口
     *
     * @param deviceId 创建端口时指定的设备
     * @param portId 创建接口返回的端口标识
     * @throws ProviderException
     */
    void delete(String deviceId, String portId) throws ProviderException;
}
