package com.huawei.inventory;

import org.apache.commons.lang3.StringUtils;

public class InvalidRouterCfg extends RuntimeException {
    public InvalidRouterCfg(String...cfg) {
        super(StringUtils.join(cfg, ' '));
    }
}
