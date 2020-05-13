package wjl.net.provider;

import wjl.net.schema.ObjectSchema;

import java.util.Map;

public interface DeviceProvider {
    /**
     * 提供者的名称，不得重复
     *
     * @return 唯一名称
     */
    String getName();

    /**
     * 创建设备的参数字典
     *
     * @return 参数字典
     */
    ObjectSchema getCreateSchema();

    /**
     * 创建设备后，设备内业务配置的字典
     *
     * @return 配置字典
     */
    ObjectSchema getConfigSchema();

    /**
     * 创建设备
     *
     * @param idInNms L3NMS内部使用的标识，提供者可以忽略
     * @param inputs 输入参数
     * @return 新设备在提供者的标识
     * @throws ProviderException 创建失败
     */
    String create(String idInNms, Map<String,Object> inputs) throws ProviderException;
}
