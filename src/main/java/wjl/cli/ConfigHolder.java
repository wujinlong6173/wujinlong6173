package wjl.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 保存应用在某个对象上的所有配置命令。
 */
public class ConfigHolder {
    private static final String COMMAND_TAB = "   ";
    private static final char COMMAND_SEP = ' ';

    private final String[] rootCommand;
    private final List<String[]> subCommands = new ArrayList<>();
    private final List<ConfigHolder> subHolders = new ArrayList<>();

    public ConfigHolder(String... rootCommand) {
        this.rootCommand = rootCommand;
    }

    /**
     * 添加带有子视图的命令。
     *
     * @param cfg 命令行
     * @return 子视图
     */
    public ConfigHolder addHolder(String... cfg) {
        for (ConfigHolder sub : subHolders) {
            if (sub.isMatch(cfg)) {
                return sub;
            }
        }

        ConfigHolder added = new ConfigHolder(cfg);
        subHolders.add(added);
        return added;
    }

    public ConfigHolder findHolder(String... cfg) {
        for (ConfigHolder sub : subHolders) {
            if (sub.isMatch(cfg)) {
                return sub;
            }
        }
        return null;
    }

    /**
     * 添加简单的命令。
     *
     * @param cfg 命令行
     */
    public void addCommand(String... cfg) {
        for (String[] cmd : subCommands) {
            if (Arrays.equals(cmd, cfg)) {
                return;
            }
        }
        subCommands.add(cfg);
    }

    public boolean checkConfig(String... cfg) {
        for (String[] cmd : subCommands) {
            if (Arrays.equals(cmd, cfg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 撤销一个命令，包括子命令。
     *
     * @param cfg 要撤销的命令行
     */
    public void undo(String... cfg) {
        Iterator<String[]> itCmd = subCommands.iterator();
        while (itCmd.hasNext()) {
            String[] cmd = itCmd.next();
            if (Arrays.equals(cmd, cfg)) {
                itCmd.remove();
                return;
            }
        }

        Iterator<ConfigHolder> itHolder = subHolders.iterator();
        while (itHolder.hasNext()) {
            ConfigHolder holder = itHolder.next();
            if (holder.isMatch(cfg)) {
                itHolder.remove();
                return;
            }
        }
    }

    private boolean isMatch(String... cfg) {
        return Arrays.equals(rootCommand, cfg);
    }

    synchronized public List<String> getConfigs() {
        List<String> ret = new ArrayList<>();
        getConfigs(ret, 0);
        return ret;
    }

    private void getConfigs(List<String> output, int level) {
        boolean flag;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(COMMAND_TAB);
        }

        int save = sb.length();
        if (rootCommand != null && rootCommand.length > 0) {
            flag = false;
            for (String word : rootCommand) {
                if (flag) {
                    sb.append(COMMAND_SEP);
                } else {
                    flag = true;
                }
                sb.append(word);
            }
            output.add(sb.toString());
            sb.delete(save, sb.length());
            level ++;
            sb.append(COMMAND_TAB);
            save = sb.length();
        }

        for (String[] cmd : subCommands) {
            flag = false;
            for (String word : cmd) {
                if (flag) {
                    sb.append(COMMAND_SEP);
                } else {
                    flag = true;
                }
                sb.append(word);
            }
            output.add(sb.toString());
            sb.delete(save, sb.length());
        }

        for (ConfigHolder holder : subHolders) {
            holder.getConfigs(output, level);
        }
    }
}
