package com.huawei.inventory;

import com.huawei.common.CLI;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 物理路由器
 */
public class PhyRouter {
    private String name;

    /**
     * 删除命令时，将数值元素设为空，而没有删除元素
     */
    private List<String[]> configs = new ArrayList<>();
    private int lastLevel = 0;
    private int lastLine = -1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    synchronized public void addConfig(String... cfg) {
        int cfgLevel = countConfigLevel(cfg);
        if (cfgLevel == 0) {
            // 从根视图开始配置
            lastLevel = 0;
            lastLine = findConfig(cfg);
            if (lastLine < 0) {
                lastLine = configs.size();
                configs.add(cfg);
            }
        } else if (invalidLastLine()) {
            // 没法定位上次配置的视图
            throw new InvalidRouterCfg(cfg);
        } else if (cfgLevel > lastLevel + 1) {
            // 必须逐级进入下级视图，不能跳级
            throw new InvalidRouterCfg(cfg);
        }

        while (cfgLevel <= lastLevel) {
            // 回退到上次配置位置的上级视图，使cfgLevel等于lastLevel + 1
            moveToParent();
        }

        insertConfig(cfg, cfgLevel);
    }

    synchronized public void undoConfig(String... cfg) {

    }

    private int countConfigLevel(String[] cfg) {
        int level;
        for (level = 0; level < cfg.length; level++) {
            if (!CLI.__.equals(cfg[level])) {
                break;
            }
        }
        return level;
    }

    private boolean invalidLastLine() {
        return lastLine < 0
                || lastLine >= configs.size()
                || configs.get(lastLine) == null;
    }

    private void moveToParent() {
        lastLevel--;
        for (; lastLine >= 0; lastLine--) {
            String[] eachCfg = configs.get(lastLine);
            if (eachCfg == null) {
                continue;
            }
            if (countConfigLevel(eachCfg) == lastLevel) {
                return;
            }
        }
    }

    private void insertConfig(String[] cfg, int cfgLevel) {
        int line;
        for (line = lastLine + 1; line < configs.size(); line++) {
            String[] eachCfg = configs.get(line);
            if (eachCfg == null) {
                continue;
            }

            if (Arrays.equals(cfg, eachCfg)) {
                lastLine = line;
                lastLevel = cfgLevel;
                return;
            }

            if (countConfigLevel(eachCfg) < cfgLevel) {
                break;
            }
        }

        configs.add(line, cfg);
        lastLine = line;
        lastLevel = cfgLevel;
    }

    private int findConfig(String[] cfg) {
        for (int line = 0; line < configs.size(); line++) {
            if (Arrays.equals(cfg, configs.get(line))) {
                return line;
            }
        }
        return -1;
    }

    synchronized public List<String> getConfigs() {
        List<String> ret = new ArrayList<>(configs.size());
        for (String[] eachCfg : configs) {
            if (eachCfg != null) {
                ret.add(StringUtils.join(eachCfg, ' '));
            }
        }
        return ret;
    }
}
