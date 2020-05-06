package wjl.net.provider;

import wjl.net.schema.ObjectSchema;

import java.util.Map;

public interface DeviceProvider {
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
     * @param inputs 输入参数
     * @return 新设备在提供商内部的标识
     */
    String create(Map<String,Object> inputs);
}
