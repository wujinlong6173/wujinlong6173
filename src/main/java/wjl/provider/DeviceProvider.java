package wjl.provider;

import wjl.datamodel.schema.ObjectSchema;

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
    
    /**
     * 删除设备
     * 
     * @param idInProvider 设备在供应商处的标识
     * @param inputs 或许用于校验
     * @throws ProviderException 删除失败
     */
    void delete(String idInProvider, Map<String,Object> inputs) throws ProviderException;
}