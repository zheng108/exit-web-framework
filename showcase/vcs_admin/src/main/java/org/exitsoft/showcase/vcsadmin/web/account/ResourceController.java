package org.exitsoft.showcase.vcsadmin.web.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.showcase.vcsadmin.common.SystemVariableUtils;
import org.exitsoft.showcase.vcsadmin.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.vcsadmin.entity.account.Resource;
import org.exitsoft.showcase.vcsadmin.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 资源管理Controller
 * 
 * @author vincent
 *
 */
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
	public String save(@ModelAttribute("entity") Resource entity,@RequestParam(value = "parentId",required=false)String parentId,RedirectAttributes redirectAttributes) {
		
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
	 * 绑定实体数据，如果存在id时获取后从数据库获取记录，进入到相对的C后在将数据库获取的记录填充到相应的参数中
	 * 
	 * @param id 主键ID
	 * 
	 */
	@ModelAttribute("entity")
	public Resource bindingModel(@RequestParam(value = "id", required = false)String id,Model model) {
		
		model.addAttribute("resourceType", SystemVariableUtils.getDataDictionariesByCategoryCode(SystemDictionaryCode.ResourceType));
		model.addAttribute("resourcesList", accountManager.getAllResources());
		
		Resource resource = new Resource();
		
		if (StringUtils.isNotEmpty(id)) {
			resource = accountManager.getResource(id);
		}
		
		return resource;
	}
}
