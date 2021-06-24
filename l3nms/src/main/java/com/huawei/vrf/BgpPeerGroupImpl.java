package com.huawei.vrf;

public class BgpPeerGroupImpl {
    private final String hubRT;
    private final String spokeRT;

    public BgpPeerGroupImpl(String hubRT, String spokeRT) {
        this.hubRT = hubRT;
        this.spokeRT = spokeRT;
    }

    public String getHubRT() {
        return hubRT;
    }

    public String getSpokeRT() {
        return spokeRT;
    }
}
