package org.exitsoft.project.vcsadmin.web.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.project.vcsadmin.common.SystemVariableUtils;
import org.exitsoft.project.vcsadmin.common.enumeration.SystemDictionaryCode;
import org.exitsoft.project.vcsadmin.entity.account.Resource;
import org.exitsoft.project.vcsadmin.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account/resource")
public class ResourceController {

	@Autowired
	private AccountManager accountManager;
	
	/**
	 * 获取资源列表
	 * 
	 * @return {@link Page}
	 */
	@RequestMapping("view")
	public Page<Resource> view(PageRequest pageRequest,HttpServletRequest request) {
		
		List<PropertyFilter> filters = PropertyFilterRestrictionHolder.buildFromHttpRequest(request);
		
		if (!pageRequest.isOrderBySetted()) {
			pageRequest.setOrderBy("sort");
			pageRequest.setOrderDir(Sort.DESC);
		}
		
		return accountManager.searchResourcePage(pageRequest, filters);
	}
	
	/**
	 * 
	 * 保存资源
	 * 
	 */
	@RequestMapping("save")
	public String save(@ModelAttribute("entity") Resource entity,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		String parentId = request.getParameter("parendId");
		
		if (StringUtils.isEmpty(parentId)) {
			entity.setParent(null);
		} else {
			entity.setParent(accountManager.getResource(parentId));
		}
		
		accountManager.saveResource(entity);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return "redirect:/account/resource/view";
	}
	
	/**
	 * 
	 * 读取资源信息
	 * 
	 */
	@RequestMapping("read")
	public String read() {
		return "account/resource/read";
	}
	
	/**
	 * 删除资源
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		accountManager.deleteResources(ids);
		redirectAttributes.addFlashAttribute("message", "删除" + ids.size() + "条信息成功");
		return "redirect:/account/resource/view";
	}
	
	/**
	 * 绑定数据，如果存在id时获取后在做其他操作
	 * 
	 */
	@ModelAttribute("entity")
	public Resource bindingModel(HttpServletRequest request) {
		
		request.setAttribute("resourceType", SystemVariableUtils.getDataDictionariesByCategoryCode(SystemDictionaryCode.ResourceType));
		request.setAttribute("resourcesList", accountManager.getAllResources());
		
		String id = request.getParameter("id");
		Resource resource = new Resource();
		
		if (StringUtils.isNotEmpty(id)) {
			resource = accountManager.getResource(id);
		}
		
		return resource;
	}
	
}
