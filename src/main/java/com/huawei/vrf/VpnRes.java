package com.huawei.vrf;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理、分配VPN相关的资源，例如RT值。
 */
public class VpnRes {
    // RT资源需要全局唯一
    private static int nextRT;

    // 给BGP Peer Group分配的RT
    private static final Map<String, BgpPeerGroupImpl> peerGroups = new HashMap<>();

    /**
     * 简单粗暴地分配RT，正常情况会规划每个区段的作用。
     *
     * @return 分配到得RT
     */
    synchronized public static int allocRT() {
        return ++nextRT;
    }

    synchronized public static BgpPeerGroupImpl getOrCreatePeerGroup(String asNumber, String name) {
        BgpPeerGroupImpl group = peerGroups.get(name);
        if (group == null) {
            int hub = ++nextRT;
            int spoke = ++nextRT;
            group = new BgpPeerGroupImpl(
                    String.format("%s:%d", asNumber, hub),
                    String.format("%s:%d", asNumber, spoke));
            peerGroups.put(name, group);
        }
        return group;
    }
}
