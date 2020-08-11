package com.huawei.vlan;

/**
 * 一条VLan链路包含两个VLan子接口
 */
public class VLanLink {
    private String id;
    private String srcSubIf;
    private String dstSubIf;

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getSrcSubIf() {
        return srcSubIf;
    }

    public void setSrcSubIf(String srcSubIf) {
        this.srcSubIf = srcSubIf;
    }

    public String getDstSubIf() {
        return dstSubIf;
    }

    public void setDstSubIf(String dstSubIf) {
        this.dstSubIf = dstSubIf;
    }
}
