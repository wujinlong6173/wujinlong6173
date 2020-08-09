package com.huawei.vrf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.huawei.common.Interface;

/**
 * 管理所有的VRF
 */
public final class VrfMgr {
    private static final Map<String, Vrf> allVrf = new ConcurrentHashMap<>();
    
    /**
     * 新建一个VRF
     * 
     * @param vrf 待创建的VRF
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
    
    /**
     * 将接口绑定到VRF
     * 
     * @param vrfId VRF的标识
     * @param portName 接口在虚拟路由器中的名称
     * @param inf 被绑定的接口 
     */
    public static void vrfBindInterface(String vrfId, String portName, Interface inf) {
        Vrf vrf = allVrf.get(vrfId);
        if (vrf != null) {
            vrf.bindInterface(portName, inf);
        }
    }

    public static void deleteVrf(String vrfId) {
        allVrf.remove(vrfId);        
    }
}
