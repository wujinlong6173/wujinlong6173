package com.huawei.vrf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理所有的VRF
 */
public final class VrfMgr {
    private static final Map<String, Vrf> allVrf = new ConcurrentHashMap<>();
    
    /**
     * 新建一个VRF
     * 
     * @param vrf
     */
    public static void createVrf(Vrf vrf) {
        if (vrf == null || vrf.getId() == null) {
            return;
        }
        
        allVrf.put(vrf.getId(), vrf);
    }
    
    public static Vrf getVrf(String id) {
        return allVrf.get(id);
    }
    
    /**
     * 获取VRF在哪个物理网元上
     * 
     * @param vrfId
     * @return
     */
    public static String getHostOfVrf(String vrfId) {
        Vrf vrf = allVrf.get(vrfId);
        if (vrf != null) {
            return vrf.getHost();
        } else {
            return null;
        }
    }
}
