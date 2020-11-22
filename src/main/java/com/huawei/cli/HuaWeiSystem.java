package com.huawei.cli;

import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import com.huawei.vrf.Vrf;
import com.huawei.vrf.VrfMgr;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;
import wjl.cli.CommandView;
import wjl.cli.CommandViewFactory;

public class HuaWeiSystem implements PasswordAuthenticator, CommandViewFactory {
    // 这是演示用的程序，没有任何危害
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASSWORD = "123456";

    @Override
    public boolean authenticate(String username, String password, ServerSession session)
            throws PasswordChangeRequiredException, AsyncAuthException {
        if (ADMIN_NAME.equals(username)) {
            return ADMIN_PASSWORD.equals(password);
        }

        // password是VRF在供应商侧的标识，或物理路由器的名称
        String idInProvider = password;
        Vrf vrf = VrfMgr.getVrf(idInProvider);
        if (vrf != null) {
            return username.equals(vrf.getIdInNms());
        }

        PhyRouter pr = PhyRouterMgr.getRouter(idInProvider);
        return pr != null;
    }

    /**
     * 获取某个路由器的命令行视图
     *
     * @param viewName 设备在网络意图中的标识
     * @return 命令行视图
     */
    @Override
    public CommandView build(String viewName) {
        if (ADMIN_NAME.equals(viewName)) {
            return new AdminView();
        }

        // 鉴权时已经确保VRF存在
        Vrf vrf = VrfMgr.getVrfByNmsId(viewName);
        if (vrf != null) {
            return new VirRouterView(vrf);
        }

        PhyRouter pr = PhyRouterMgr.getRouterByNmsId(viewName);
        if (pr != null) {
            return new RouterView(pr);
        }

        return null;
    }
}
