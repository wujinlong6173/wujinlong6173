package com.huawei.physical;

/**
 * 模拟真实的物理路由器。
 */
public class PhyRouter extends PhyDevice {
    private String asNumber = "100";

    public String getAsNumber() {
        return asNumber;
    }

    public void setAsNumber(String asNumber) {
        this.asNumber = asNumber;
    }
}
