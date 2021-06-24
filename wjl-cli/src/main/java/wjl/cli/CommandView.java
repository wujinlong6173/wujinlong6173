package wjl.cli;

/**
 * 所有命令视图、子视图，都要实现此接口。
 */
public interface CommandView {
    /**
     * 命令视图、子视图的提示符
     *
     * @return 命令提示符
     */
    String getPrompt();
}
