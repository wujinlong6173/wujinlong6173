package com.huawei.physical;

import wjl.datamodel.schema.ObjectSchema;
import wjl.provider.AbsProductProvider;
import wjl.provider.LinkProvider;
import wjl.provider.ProviderException;

import java.util.Map;

/**
 * 创建跨运营商的链路。
 */
public class CrossLinkProvider extends AbsProductProvider implements LinkProvider {
    public CrossLinkProvider(String providerName, String productName) {
        super(providerName, productName);
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return null;
    }

    /**
     * 创建跨不同运营商的物理链路
     *
     * @param idInNms
     * @param srcOuterId 运营商给源端设备的名称
     * @param srcPortName 源物理端口的名称
     * @param srcProvider 源端设备所属运营商的名称
     * @param dstOuterId 运营商给宿端设备的名称
     * @param dstPortName 宿物理端口的名称
     * @param dstProvider 宿端设备所属运营商的名称
     * @param inputs 创建链路的输入参数
     * @return
     * @throws ProviderException
     */
    @Override
    public String create(String idInNms,
                         String srcOuterId, String srcPortName, String srcProvider,
                         String dstOuterId, String dstPortName, String dstProvider,
                         Map<String, Object> inputs) throws ProviderException {
        // 1. 校验源宿设备是否已授权，被引用过算已授权
        // srcOuterId和dstOuterId是RefRouterProvider.create的返回值

        // 2. 向运营商申请源宿端口的授权，确保端口有效

        // 3. 创建并存储物理链路对象
        PhyLinkMgr linkMgr = getInstance(PhyLinkMgr.class);
        PhyLink pl = new PhyLink();
        pl.setId(idInNms);
        pl.setSrcDevice(srcOuterId);
        pl.setSrcPort(srcPortName);
        pl.setDstDevice(dstOuterId);
        pl.setDstPort(dstPortName);
        linkMgr.addLink(pl);
        return idInNms;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {

    }
}
