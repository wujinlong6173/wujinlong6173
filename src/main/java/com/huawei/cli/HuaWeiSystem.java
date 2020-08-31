package com.huawei.cli;

import com.huawei.vrf.Vrf;
import com.huawei.vrf.VrfMgr;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;

public class HuaWeiSystem implements PasswordAuthenticator {
    // 这是演示用的程序，没有任何危害
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASSWORD = "123456";

    @Override
    public boolean authenticate(String username, String password, ServerSession session)
            throws PasswordChangeRequiredException, AsyncAuthException {
        if (ADMIN_NAME.equals(username)) {
            return ADMIN_PASSWORD.equals(password);
        }

        Vrf vrf = VrfMgr.getVrf(password);
        return vrf != null && username.equals(vrf.getIdInNms());
    }

    /**
     * 获取某个路由器的命令行视图
     *
     * @param username
     * @return 命令行视图
     */
    public static Object getCommandView(String username) {
        if (ADMIN_NAME.equals(username)) {
            return new AdminView();
        }
        return new RouterView();
    }
}
