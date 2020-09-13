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

    public String getAsNumber() {
        return "100";
    }

    /**
     * 添加配置，如果子配置，会根据上次的配置自动找位置。
     *
     * @param cfg
     */
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
            return;
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

        if (line < configs.size() && configs.get(line) == null) {
            configs.set(line, cfg);
        } else {
            configs.add(line, cfg);
        }
        lastLine = line;
        lastLevel = cfgLevel;
    }

    /**
     * 撤销配置，自动撤销子配置。
     *
     * @param cfg
     */
    synchronized public void undoConfig(String... cfg) {
        int cfgLevel = countConfigLevel(cfg);
        if (cfgLevel == 0) {
            lastLine = -1;
            undoConfigL1(cfg);
            return;
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

        undoConfigLx(cfg, cfgLevel);
    }

    private void undoConfigL1(String[] cfg) {
        int line;
        for (line = 0; line < configs.size(); line++) {
            String[] eachCfg = configs.get(line);
            if (eachCfg == null) {
                continue;
            }
            if (Arrays.equals(cfg, configs.get(line))) {
                configs.set(line, null);
                break;
            }
        }

        for (line++; line < configs.size(); line++) {
            String[] eachCfg = configs.get(line);
            if (eachCfg == null) {
                continue;
            }
            if (countConfigLevel(eachCfg) == 0) {
                break;
            }
            configs.set(line, null);
        }
    }

    private void undoConfigLx(String[] cfg, int cfgLevel) {
        int line;
        for (line = lastLine; line < configs.size(); line++) {
            String[] eachCfg = configs.get(line);
            if (eachCfg == null) {
                continue;
            }
            if (Arrays.equals(cfg, configs.get(line))) {
                configs.set(line, null);
                break;
            }
        }

        for (line++; line < configs.size(); line++) {
            String[] eachCfg = configs.get(line);
            if (eachCfg == null) {
                continue;
            }
            if (countConfigLevel(eachCfg) <= cfgLevel) {
                break;
            }
            configs.set(line, null);
        }
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

    private int findConfig(String[] cfg) {
        for (int line = 0; line < configs.size(); line++) {
            if (Arrays.equals(cfg, configs.get(line))) {
                return line;
            }
        }
        return -1;
    }

    /**
     * 获取所有的配置。
     *
     * @return
     */
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
