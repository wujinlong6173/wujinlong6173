package com.huawei.physical;

import wjl.datamodel.schema.ObjectSchema;
import wjl.provider.AbsProductProvider;
import wjl.provider.DeviceProvider;

import java.util.Map;

/**
 * 模拟物理交换机的供应商，交换机用于CE和PE之间，更贴近真实网络。
 */
public class PhySwitchProvider extends AbsProductProvider implements DeviceProvider {
    public PhySwitchProvider(String providerName, String productName) {
        super(providerName, productName);
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return null;
    }

    @Override
    public ObjectSchema getConfigSchema() {
        return null;
    }

    @Override
    public String getIcon() {
        return "/images/network_alt.png;";
    }

    @Override
    public String create(String idInNms, Map<String, Object> inputs) {
        // 物理交换机的名称也是唯一的
        String name = (String)inputs.get("name");
        PhySwitch sw = new PhySwitch();
        sw.setName(name);
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        deviceMgr.addSwitch(sw);
        return name;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) {
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        deviceMgr.delSwitch(idInProvider);
    }
}
