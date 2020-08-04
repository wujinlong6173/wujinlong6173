package com.huawei.common;

/**
 * 所有可以配置业务的接口
 */
public interface Interface {
    /**
     * 接口在物理网元中的唯一名称，通过此名称下发配置参数。
     * 
     * @return 接口名称
     */
    String getInterfaceName();
}
