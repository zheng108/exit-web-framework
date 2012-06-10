package org.exitsoft.project.vcsadmin.web.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.utils.ServletUtils;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.project.vcsadmin.common.SystemVariableUtils;
import org.exitsoft.project.vcsadmin.common.enumeration.SystemDictionaryCode;
import org.exitsoft.project.vcsadmin.common.enumeration.entity.GroupType;
import org.exitsoft.project.vcsadmin.entity.account.Group;
import org.exitsoft.project.vcsadmin.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account/group")
public class GroupController {
	
	@Autowired
	private AccountManager accountManager;
	
	/**
	 * 获取资源列表
	 * 
	 * @return {@link Page}
	 */
	@RequestMapping("view")
	public Page<Group> view(PageRequest pageRequest,HttpServletRequest request) {
		
		List<PropertyFilter> filters = PropertyFilterRestrictionHolder.buildFromHttpRequest(request);
		
		if (!pageRequest.isOrderBySetted()) {
			pageRequest.setOrderBy("id");
			pageRequest.setOrderDir(Sort.DESC);
		}
		filters.add(PropertyFilterRestrictionHolder.createPropertyFilter("EQ_S_type", GroupType.RoleGorup.getValue()));
		return accountManager.searchGroupPage(pageRequest, filters);
	}
	
	/**
	 * 
	 * 保存资源
	 * 
	 */
	@RequestMapping("save")
	public String save(@ModelAttribute("entity") Group entity,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		String parentId = request.getParameter("parendId");
		
		if (StringUtils.isEmpty(parentId)) {
			entity.setParent(null);
		} else {
			entity.setParent(accountManager.getGroup(parentId));
		}
		
		List<String> resourceIds = ServletUtils.getParameterValues(request, "resourceIds");
		
		entity.setResourcesList(accountManager.getResources(resourceIds));
		
		accountManager.saveGroup(entity);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return "redirect:/account/group/view";
	}
	
	/**
	 * 
	 * 读取资源信息
	 * 
	 */
	@RequestMapping("read")
	public String read(Model model) {
		model.addAttribute("resourcesList", accountManager.getAllResources());
		return "account/group/read";
	}
	
	/**
	 * 删除资源
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		accountManager.deleteGroups(ids);
		redirectAttributes.addFlashAttribute("message", "删除" + ids.size() + "条信息成功");
		return "redirect:/account/group/view";
	}
	
	/**
	 * 绑定数据，如果存在id时获取后在做其他操作
	 * 
	 */
	@ModelAttribute("entity")
	public Group bindingModel(HttpServletRequest request) {
		
		request.setAttribute("states", SystemVariableUtils.getDataDictionariesByCategoryCode(SystemDictionaryCode.State));
		request.setAttribute("groupsList", accountManager.getAllGroup(GroupType.RoleGorup));
		
		String id = request.getParameter("id");
		Group group = new Group();
		
		if (StringUtils.isNotEmpty(id)) {
			group = accountManager.getGroup(id);
		}
		
		return group;
	}
}
