package wjl.net.provider;

import wjl.net.schema.ObjectSchema;

import java.util.Map;

public interface LinkProvider {
    /**
     * 提供者的名称，不得重复
     *
     * @return 唯一名称
     */
    String getName();

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
    String create(String idInNms, String srcOuterId, String dstOuterId,
                  String srcProvider, String dstProvider,
                  Map<String,Object> inputs) throws ProviderException;
}
