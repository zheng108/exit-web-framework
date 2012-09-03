package org.exitsoft.showcase.vcsadmin.service.account;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.exitsoft.showcase.vcsadmin.common.model.CommonVariableModel;
import org.exitsoft.showcase.vcsadmin.entity.account.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * apache shiro 的身份验证类
 * 
 * @author vincent
 *
 */
public class JdbcAuthenticationRealm extends AuthenticatingRealm{
	
	public static String NAME = "jdbcAuthentication";
	
	@Autowired
	private AccountManager accountManager;

	/**
	 * 用户登录的身份验证方法
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
        
        return new SimpleAuthenticationInfo(model,user.getPassword(),NAME);
	}
	

}
