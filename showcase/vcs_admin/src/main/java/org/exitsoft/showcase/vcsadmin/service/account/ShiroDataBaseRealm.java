package org.exitsoft.showcase.vcsadmin.service.account;

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
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.ResourceType;
import org.exitsoft.showcase.vcsadmin.common.model.CommonVariableModel;
import org.exitsoft.showcase.vcsadmin.entity.account.Group;
import org.exitsoft.showcase.vcsadmin.entity.account.Resource;
import org.exitsoft.showcase.vcsadmin.entity.account.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * apache shiro 的授权和认证接口
 * 
 * @author vincent
 *
 */
public class ShiroDataBaseRealm extends AuthorizingRealm{
	
	@Autowired
	private AccountManager accountManager;

	/**
	 * 
	 * 当用户进行访问链接时的授权方法
	 * 
	 */
	
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
        if (principals == null) {
            throw new AuthorizationException("Principal对象不能为空");
        }
        
        CommonVariableModel model = (CommonVariableModel) principals.fromRealm(getName()).iterator().next();
        
        String id = model.getUser().getId();
        
        //加载用户的组信息和资源信息
        List<Resource> authorizationInfo = accountManager.getUserResourcesByUserId(id);
        List<Group> groupsList = accountManager.getUserGroupsByUserId(id);
        List<Resource> resourcesList = accountManager.mergeResourcesToParent(authorizationInfo, ResourceType.Security);
        
        model.setAuthorizationInfo(authorizationInfo);
        model.setGroupsList(groupsList);
        model.setMenusList(resourcesList);
        
        List<String> permissions = CollectionUtils.extractToList(model.getAuthorizationInfo(), "permission",true);
        
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        
        info.addStringPermissions(permissions);
        
        return info;
	}
	
	/**
	 * 用户登录的认证方法
	 * 
	 */
	
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
        
        CommonVariableModel model = new CommonVariableModel(user);
        
        return new SimpleAuthenticationInfo(model,user.getPassword(),getName());
	}
}
