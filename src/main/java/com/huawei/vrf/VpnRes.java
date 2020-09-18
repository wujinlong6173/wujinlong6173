package com.huawei.vrf;

/**
 * 管理、分配VPN相关的资源，例如RT值。
 */
public class VpnRes {
    // RT资源需要全局唯一
    private static int nextRT;

    /**
     * 简单粗暴地分配RT，正常情况会规划每个区段的作用。
     *
     * @return 分配到得RT
     */
    public static int allocRT() {
        return ++nextRT;
    }
}
