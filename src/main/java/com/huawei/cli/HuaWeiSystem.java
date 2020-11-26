package com.huawei.cli;

import com.huawei.physical.PhyRouter;
import com.huawei.physical.PhyDeviceMgr;
import com.huawei.physical.PhyRouterView;
import com.huawei.vrf.Vrf;
import com.huawei.vrf.VrfMgr;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;
import wjl.cli.CommandView;
import wjl.cli.CommandViewFactory;
import wjl.docker.AbstractMember;

public class HuaWeiSystem extends AbstractMember implements PasswordAuthenticator, CommandViewFactory {
    @Override
    public boolean authenticate(String username, String password, ServerSession session)
            throws PasswordChangeRequiredException, AsyncAuthException {
        // password是VRF在供应商侧的标识，或物理路由器的名称
        String idInProvider = password;
        VrfMgr vrfMgr = getInstance(VrfMgr.class);
        Vrf vrf = vrfMgr.getVrf(idInProvider);
        if (vrf != null) {
            return username.equals(vrf.getIdInNms());
        }

        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouter(idInProvider);
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
        // 鉴权时已经确保VRF存在
        VrfMgr vrfMgr = getInstance(VrfMgr.class);
        Vrf vrf = vrfMgr.getVrfByNmsId(viewName);
        if (vrf != null) {
            VirRouterView view = new VirRouterView(vrf);
            view.setContainer(this);
            return view;
        }

        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouterByNmsId(viewName);
        if (pr != null) {
            return new PhyRouterView(pr);
        }

        return null;
    }
}
