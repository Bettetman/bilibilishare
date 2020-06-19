package cn.sicnu.ming.shiro.realm;

import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author frank ming
 * @createTime 2020061717 21:51
 * @description shiro实现
 */
public class MyRealm extends AuthorizingRealm {
    @Autowired
    public UserService userService;

    /**
     *授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }
    /**
     * 权限认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = authenticationToken.getPrincipal().toString();
        User user = userService.findByUserName(username);
        if (user==null){
            return  null;
        }else{
            AuthenticationInfo info = new SimpleAuthenticationInfo(user.getUserName(),user.getPassword(),"xx");
            return info;
        }
    }
}
