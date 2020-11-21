package wjl.cli;

/**
 * 创建命令视图的接口，通常会组成一个列表。
 */
public interface CommandViewFactory {
    /**
     * 创建一个命令视图，输入不认识的名称时返回空
     *
     * @param name 命令视图的名称，例如，虚拟路由器在网络意图中的标识
     * @return 命令视图或空
     */
    CommandView build(String name);
}
