package com.huawei.vrf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.huawei.common.CLI;
import com.huawei.common.Interface;
import com.huawei.physical.PhyRouter;
import com.huawei.physical.PhyDeviceMgr;
import wjl.docker.AbstractMember;

/**
 * 管理所有的VRF
 */
public final class VrfMgr extends AbstractMember {
    private final Map<String, Vrf> allVrf = new ConcurrentHashMap<>();
    
    /**
     * 新建一个VRF
     * 
     * @param vrf 待创建的VRF
     */
    public void createVrf(Vrf vrf) {
        if (vrf == null || vrf.getId() == null) {
            return;
        }
        
        allVrf.put(vrf.getId(), vrf);
    }
    
    public Vrf getVrf(String id) {
        return allVrf.get(id);
    }
    
    /**
     * 获取VRF在哪个物理网元上
     * 
     * @param vrfId
     * @return
     */
    public String getHostOfVrf(String vrfId) {
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
    public void bindInterface(String vrfId, String portName, Interface inf) {
        Vrf vrf = allVrf.get(vrfId);
        if (vrf == null) {
            return;
        }

        vrf.bindInterface(portName, inf);
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouter(vrf.getHost());
        if (pr != null) {
            pr.addConfig(CLI.INTERFACE, inf.getInterfaceName());
            pr.addConfig(CLI.__, CLI.IP, CLI.BINDING, CLI.VPN_INSTANCE, vrf.getName());
        }
    }

    /**
     * 解除接口和VRF的绑定
     *
     * @param inf 接口
     */
    public void unBindInterface(Interface inf) {
        if (inf == null || inf.getBindService() == null) {
            return;
        }
        Vrf vrf = allVrf.get(inf.getBindService());
        if (vrf == null) {
            return;
        }

        vrf.unBindInterface(inf);
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouter(vrf.getHost());
        if (pr != null) {
            pr.addConfig(CLI.INTERFACE, inf.getInterfaceName());
            pr.undoConfig(CLI.__, CLI.IP, CLI.BINDING, CLI.VPN_INSTANCE, vrf.getName());
        }
    }

    public void deleteVrf(String vrfId) {
        allVrf.remove(vrfId);        
    }

    public Vrf getVrfByBgpRouterId(String ip) {
        for (Vrf vrf : allVrf.values()) {
            if (ip.equals(vrf.getBgpRouterId())) {
                return vrf;
            }
        }
        return null;
    }
}
