package org.exitsoft.showcase.vcsadmin.web;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.exitsoft.showcase.vcsadmin.common.SystemVariableUtils;
import org.exitsoft.showcase.vcsadmin.entity.account.User;
import org.exitsoft.showcase.vcsadmin.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 系统安全控制器
 * 
 * @author vincent
 *
 */
@Controller
public class SecurityController {
	
	@Autowired
	private AccountManager accountManager;
	
	/**
	 * 登录C，返回登录页面。当C发现当前用户已经登录名且认真后，会自动跳转到index页面
	 * 
	 * @return String
	 */
	@RequestMapping("/login")
	public String login() {
		if (!SystemVariableUtils.isAuthenticated()) {
			return "login";
		}
		return "redirect:/index";
	}
	
	/**
	 * 首页C,在request用翻入当前用户的菜单集合给页面循环
	 * 
	 * @param model String mvc model 接口
	 * 
	 * @return String
	 */
	@RequestMapping("/index")
	public String index(Model model) {
		
		model.addAttribute("menusList", SystemVariableUtils.getSecurityModel().getMenusList());
		
		return "index";
	}
	
	/**
	 * 当shior发现当前没有任何用户登录的信息时候调用该C，返回login页面
	 * 
	 * @return String
	 */
	@RequestMapping("/unauthorized")
	public String unauthorized(){
		return "login";
	}
	
	/**
	 * 用户修改密码C.修改成功返回"true"否则返回"false"
	 * 
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * 
	 * @return String
	 */
	@ResponseBody
	@RequestMapping("/changePassword")
	public String changePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword) {
		
		User user = SystemVariableUtils.getSecurityModel().getUser();
		
		oldPassword = new SimpleHash("MD5", oldPassword.toCharArray()).toString();
		
		if(user.getPassword().equals(oldPassword)) {
			accountManager.updateUserPassword(user.getId(),newPassword);
			user.setPassword(newPassword);
			return "true";
		}
		
		return "false";
	}
	
}
