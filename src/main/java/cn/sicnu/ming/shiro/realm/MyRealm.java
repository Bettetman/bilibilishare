package cn.sicnu.ming.shiro.realm;

import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

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
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.findByUserName(userName);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        if("管理员".equals(user.getRoleName())){
            roles.add("管理员");
            authorizationInfo.addStringPermission("进入管理界面");
            authorizationInfo.addStringPermission("根据id查询资源类型实体");
            authorizationInfo.addStringPermission("添加或修改资源类型信息");
            authorizationInfo.addStringPermission("删除资源类型信息");
            authorizationInfo.addStringPermission("分页查询资源信息列表");

        }
        authorizationInfo.setRoles(roles);
        return authorizationInfo ;
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
