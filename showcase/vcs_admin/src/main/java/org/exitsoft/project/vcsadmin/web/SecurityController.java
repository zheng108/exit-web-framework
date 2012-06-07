package org.exitsoft.project.vcsadmin.web;

import org.exitsoft.project.vcsadmin.common.SystemVariableUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 系统安全控制器
 * 
 * @author vincent
 *
 */
@Controller
public class SecurityController {
	
	@RequestMapping("/login")
	public String login() {
		if (!SystemVariableUtils.isLogin()) {
			return "login";
		}
		return "redirect:/index";
	}
	
	@RequestMapping("/index")
	public String index(Model model) {
		
		model.addAttribute("resourcesList", SystemVariableUtils.getCurrentUser().getResourcesList());
		
		return "index";
	}
	
	@RequestMapping("/unauthorized")
	public String unauthorized(){
		return "login";
	}
	
}
