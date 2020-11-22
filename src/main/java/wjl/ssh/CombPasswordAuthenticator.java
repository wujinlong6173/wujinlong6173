package wjl.ssh;

import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;

import java.util.HashSet;
import java.util.Set;

/**
 * 组合的鉴权器，只要通过一个检查器则成功。
 */
public class CombPasswordAuthenticator implements PasswordAuthenticator {
    private final Set<PasswordAuthenticator> members = new HashSet<>();

    @Override
    public boolean authenticate(String username, String password, ServerSession session)
            throws PasswordChangeRequiredException, AsyncAuthException {
        for (PasswordAuthenticator authenticator : members) {
            if (authenticator.authenticate(username, password, session)) {
                return true;
            }
        }
        return false;
    }

    public void addMember(PasswordAuthenticator member) {
        if (member != null) {
            members.add(member);
        }
    }
}
