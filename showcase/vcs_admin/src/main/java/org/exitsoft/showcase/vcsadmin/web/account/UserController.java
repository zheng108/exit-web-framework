package org.exitsoft.showcase.vcsadmin.web.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.utils.CollectionUtils;
import org.exitsoft.common.utils.ServletUtils;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.showcase.vcsadmin.common.SystemVariableUtils;
import org.exitsoft.showcase.vcsadmin.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.vcsadmin.entity.account.User;
import org.exitsoft.showcase.vcsadmin.entity.foundation.DataDictionary;
import org.exitsoft.showcase.vcsadmin.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 用户管理Controller
 * 
 * @author vincent
 *
 */
@Controller
@RequestMapping("/account/user")
public class UserController {
	
	@Autowired
	private AccountManager accountManager;
	
	/**
	 * 获取用户列表
	 * 
	 * @return {@link Page}
	 */
	@RequestMapping("view")
	public Page<User> view(PageRequest pageRequest,HttpServletRequest request) {
		
		List<PropertyFilter> filters = PropertyFilterRestrictionHolder.buildFromHttpRequest(request);
		
		if (!pageRequest.isOrderBySetted()) {
			pageRequest.setOrderBy("id");
			pageRequest.setOrderDir(Sort.DESC);
		}
		
		return accountManager.searchUserPage(pageRequest, filters);
	}
	
	
	/**
	 * 创建用户
	 */
	@RequestMapping("insert")
	public String insert(User entity,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		List<String> groupIds = ServletUtils.getParameterValues(request, "groupIds");
		
		entity.setGroupsList(accountManager.getGroups(groupIds));
		
		accountManager.insertUser(entity);
		redirectAttributes.addFlashAttribute("message", "新增成功");
		return "redirect:/account/user/view";
	}
	
	/**
	 * 删除用户
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		accountManager.deleteUsers(ids);
		redirectAttributes.addFlashAttribute("message", "删除" + ids.size() + "条信息成功");
		return "redirect:/account/user/view";
	}
	
	/**
	 * 更新用户
	 */
	@RequestMapping(value="update")
	public String update(@ModelAttribute("entity")User entity, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		List<String> groupIds = ServletUtils.getParameterValues(request, "groupIds");
		
		entity.setGroupsList(accountManager.getGroups(groupIds));
		
		accountManager.updateUser(entity);
		redirectAttributes.addFlashAttribute("message", "修改成功");
		return "redirect:/account/user/view";
	}
	
	/**
	 * 判断用户帐号是否唯一,在新建时使用,如果存在用户返回true,否则返回false
	 * 
	 * @param username 用户帐号
	 * 
	 * @return boolean 
	 */
	@ResponseBody
	@RequestMapping("isUsernameUnique")
	public String isUsernameUnique(String username) {
		
		return String.valueOf(accountManager.isUsernameUnique(username));
		
	}
	
	/**
	 * 创建和更新使用的方法签名.如果链接没有?id=*会跳转到create.flt,如果存在跳转到read.ftl
	 * 
	 */
	@RequestMapping("read")
	public String read(@RequestParam(value = "id", required = false)String id,Model model) {
		
		model.addAttribute("groupsList", accountManager.getGroups(GroupType.RoleGorup));
		if (StringUtils.isEmpty(id)) {
			return "account/user/create";
		} else {
			return "account/user/read";
		}
		
	}
	
	/**
	 * 绑定实体数据，如果存在id时获取后从数据库获取记录，进入到相对的C后在将数据库获取的记录填充到相应的参数中
	 * 
	 * @param id 主键ID
	 * 
	 */
	@ModelAttribute("entity")
	public User bindingModel(@RequestParam(value = "id", required = false)String id,Model model) {
		
		model.addAttribute("states", SystemVariableUtils.getDataDictionariesByCategoryCode(SystemDictionaryCode.State,"3"));
		
		User user = new User();
		
		if (StringUtils.isNotEmpty(id)) {
			user = accountManager.getUser(id);
		}
		
		return user;
	}
	
}
