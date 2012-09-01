package org.exitsoft.showcase.vcsadmin.service.account;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
	
	private String defaultPermissions;
	
	private String defaultRoles;
	
	/**
	 * 默认permission，如果有多个用逗号分割
	 * 
	 * @param defaultPermissions permission
	 */
	public void setDefaultPermissions(String defaultPermissions) {
		this.defaultPermissions = defaultPermissions;
	}
	
	/**
	 * 默认role，如果有多个用逗号分割
	 * 
	 * @param defaultRoles role
	 */
	public void setDefaultRoles(String defaultRoles) {
		this.defaultRoles = defaultRoles;
	}

	/**
	 * 
	 * 当用户进行访问链接时的授权方法
	 * 
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
        if (principals == null) {
            throw new AuthorizationException("Principal对象不能为空");
        }
        
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        
        CommonVariableModel model = (CommonVariableModel) principals.fromRealm(getName()).iterator().next();
        
        String id = model.getUser().getId();
        
        //加载用户的组信息和资源信息
        List<Resource> authorizationInfo = accountManager.getUserResourcesByUserId(id);
        List<Group> groupsList = accountManager.getUserGroupsByUserId(id);
        List<Resource> resourcesList = accountManager.mergeResourcesToParent(authorizationInfo, ResourceType.Security);
        
        model.setAuthorizationInfo(authorizationInfo);
        model.setGroupsList(groupsList);
        model.setMenusList(resourcesList);
        
        //添加用户拥有的permission
        addPermissions(info,authorizationInfo);
        //添加用户拥有的role
        addRoles(info,groupsList);
        
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
	
	/**
	 * 通过组集合，将集合中的role字段内容解析后添加到SimpleAuthorizationInfo授权信息中
	 * 
	 * @param info SimpleAuthorizationInfo
	 * @param groupsList 组集合
	 */
	private void addRoles(SimpleAuthorizationInfo info, List<Group> groupsList) {
		
		//解析当前用户组中的role
        List<String> temp = CollectionUtils.extractToList(groupsList, "role", true);
        List<String> roles = getValue(temp,"roles\\[\"(.*?)\\\"]");
       
        //添加默认的roles到roels
        String[] dRoles = StringUtils.split(defaultRoles,",");
        if (ArrayUtils.isNotEmpty(dRoles)) {
            CollectionUtils.addAll(roles, dRoles);
        }
        //将当前用户拥有的roles设置到SimpleAuthorizationInfo中
        info.addRoles(roles);
		
	}

	/**
	 * 通过资源集合，将集合中的permission字段内容解析后添加到SimpleAuthorizationInfo授权信息中
	 * 
	 * @param info SimpleAuthorizationInfo
	 * @param authorizationInfo 资源集合
	 */
	private void addPermissions(SimpleAuthorizationInfo info,List<Resource> authorizationInfo) {
		//解析当前用户资源中的permissions
        List<String> temp = CollectionUtils.extractToList(authorizationInfo, "permission", true);
        List<String> permissions = getValue(temp,"perms\\[\"(.*?)\\\"]");
       
        //添加默认的permissions到permissions
        String[] dPermissions = StringUtils.split(defaultPermissions,",");
        if (ArrayUtils.isNotEmpty(dPermissions)) {
            CollectionUtils.addAll(permissions, dPermissions);
        }
        //将当前用户拥有的permissions设置到SimpleAuthorizationInfo中
        info.addStringPermissions(permissions);
		
	}
	
	/**
	 * 通过正则表达式获取字符串集合的值
	 * 
	 * @param obj 字符串集合
	 * @param regex 表达式
	 * 
	 * @return List
	 */
	private List<String> getValue(List<String> obj,String regex){

        List<String> result = new ArrayList<String>();
        
		if (CollectionUtils.isEmpty(obj)) {
			return result;
		}
		
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(StringUtils.join(obj, ","));
        
        while(matcher.find()){
        	result.add(matcher.group(1));
        }
        
		return result;
	}

}
