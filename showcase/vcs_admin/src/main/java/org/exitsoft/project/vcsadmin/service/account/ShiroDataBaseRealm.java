package org.exitsoft.project.vcsadmin.service.account;

import java.util.List;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.exitsoft.common.utils.CollectionUtils;
import org.exitsoft.project.vcsadmin.common.enumeration.entity.ResourceType;
import org.exitsoft.project.vcsadmin.entity.account.Resource;
import org.exitsoft.project.vcsadmin.entity.account.User;
import org.exitsoft.project.vcsadmin.model.SecurityModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * apache shiro 的授权接口
 * 
 * @author vincent
 *
 */
@Component
public class ShiroDataBaseRealm extends AuthorizingRealm{
	
	@Autowired
	private AccountManager accountManager;

	/**
	 * 
	 * 当用户进行访问链接时的授权方法
	 * 
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
        if (principals == null) {
            throw new AuthorizationException("Principal对象不能为空");
        }
        
        SecurityModel model = (SecurityModel) principals.fromRealm(getName()).iterator().next();
        List<String> permissions = CollectionUtils.extractToList(model.getAuthorizationInfo(), "permission",true);
		
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);
        
        return info;
	}
	
	/**
	 * 用户登录的验证方法
	 * 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        String username = usernamePasswordToken.getUsername();
        
        if (username == null) {
            throw new AccountException("用户名不能为空");
        }
        
        User user = accountManager.getUserByUsername(username);
        
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        
        List<Resource> authorizationInfo = accountManager.getAllResourcesByUserId(user.getId());
        List<Resource> resourcesList = accountManager.mergeResourcesToParent(authorizationInfo, ResourceType.Security);
        SecurityModel model = new SecurityModel(user,user.getGroupsList(),authorizationInfo,resourcesList);
        //SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(model,DigestUtils.md5Hex(user.getPassword()), getName());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(model,user.getPassword(), getName());
        
		return info;
	}
}
