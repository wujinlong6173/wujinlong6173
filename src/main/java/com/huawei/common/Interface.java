package com.huawei.common;

/**
 * 所有可以配置业务的接口
 */
public abstract class Interface {
    private String id;
    private String host;
    private String bindService;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBindService() {
        return bindService;
    }

    public void setBindService(String bindService) {
        this.bindService = bindService;
    }

    /**
     * 接口在物理网元中的唯一名称，通过此名称下发配置参数。
     * 
     * @return 接口名称
     */
    public abstract String getInterfaceName();
}
