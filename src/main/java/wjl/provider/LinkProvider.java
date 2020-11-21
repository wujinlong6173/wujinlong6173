package wjl.provider;

import wjl.datamodel.schema.ObjectSchema;

import java.util.Map;

public interface LinkProvider {
    /**
     * 创建链路的参数字典
     *
     * @return 参数字典
     */
    ObjectSchema getCreateSchema();

    /**
     * 创建链路
     *
     * @param idInNms L3NMS内部使用的标识，提供者可以忽略
     * @param srcOuterId 源端设备在提供者的标识
     * @param dstOuterId 宿端设备在提供者的标识
     * @param srcProvider 源端设备的提供者的名称
     * @param dstProvider 宿端设备的提供者的名称
     * @param inputs 创建链路的输入参数
     * @return 新链路在提供者的标识
     * @throws ProviderException 创建失败
     */
    String create(String idInNms, 
            String srcOuterId, String srcPortName, String srcProvider,
            String dstOuterId, String dstPortName, String dstProvider,
            Map<String,Object> inputs) throws ProviderException;

    /**
     * 删除链路
     * 
     * @param idInProvider 链路在供应商处的标识
     * @param inputs 或许用于校验
     * @throws ProviderException 删除失败
     */
    void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException;
}
