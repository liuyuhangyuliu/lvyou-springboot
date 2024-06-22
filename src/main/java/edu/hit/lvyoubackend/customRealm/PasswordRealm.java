package edu.hit.lvyoubackend.customRealm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.hit.lvyoubackend.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.stereotype.Component;


@Component
public class PasswordRealm extends AuthorizingRealm {
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        User queryUser = new User();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String username = (String) authenticationToken.getPrincipal();
        User user = queryUser.selectOne(queryWrapper.eq("username",username));
        if(user != null){
            return new SimpleAuthenticationInfo(
                    username,
                    user.getPassword(),
                    new SimpleByteSource(user.getSalt()),
                    this.getName()
            );
        }

        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken var1) {
        return var1 instanceof UsernamePasswordToken;
    }
}
