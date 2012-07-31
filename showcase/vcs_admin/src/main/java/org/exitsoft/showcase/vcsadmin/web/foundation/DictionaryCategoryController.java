package org.exitsoft.showcase.vcsadmin.web.foundation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.showcase.vcsadmin.entity.foundation.DictionaryCategory;
import org.exitsoft.showcase.vcsadmin.service.foundation.SystemDictionaryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 字典类别管理Controller
 * 
 * @author vincent
 *
 */
@Controller
@RequestMapping("/foundation/dictionary-category")
public class DictionaryCategoryController {
	
	@Autowired
	private SystemDictionaryManager systemDictionaryManager;
	
	/**
	 * 获取字典类别列表
	 * 
	 * @return {@link Page}
	 */
	@RequestMapping("view")
	public Page<DictionaryCategory> view(PageRequest pageRequest,HttpServletRequest request) {
		
		List<PropertyFilter> filters = PropertyFilterRestrictionHolder.buildFromHttpRequest(request);
		
		if (!pageRequest.isOrderBySetted()) {
			pageRequest.setOrderBy("id");
			pageRequest.setOrderDir(Sort.DESC);
		}
		
		request.setAttribute("categoriesList", systemDictionaryManager.getAllDictionaryCategories());
		
		return systemDictionaryManager.searchDictionaryCategoryPage(pageRequest, filters);
	}
	
	/**
	 * 
	 * 保存或更新字典类别
	 * 
	 */
	@RequestMapping("save")
	public String save(@ModelAttribute("entity") DictionaryCategory entity,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		String parentId = request.getParameter("parendId");
		
		if (StringUtils.isEmpty(parentId)) {
			entity.setParent(null);
		} else {
			entity.setParent(systemDictionaryManager.getDictionaryCategory(parentId));
		}
		
		systemDictionaryManager.saveDictionaryCategory(entity);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return "redirect:/foundation/dictionary-category/view";
	}
	
	/**
	 * 
	 * 读取字典类别
	 * 
	 */
	@RequestMapping("read")
	public String read(HttpServletRequest request) {
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		String id = request.getParameter("id");
		
		if (StringUtils.isNotEmpty(id)) {
			filters.add(PropertyFilterRestrictionHolder.createPropertyFilter("NE_S_id", id));
		}
		
		request.setAttribute("categoriesList", systemDictionaryManager.getAllDictionaryCategories(filters));
		
		return "/foundation/dictionary-category/read";
		
	}
	
	/**
	 * 删除字典类别
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		systemDictionaryManager.deleteDictionaryCategory(ids);
		redirectAttributes.addFlashAttribute("message", "删除" + ids.size() + "条信息成功");
		return "redirect:/foundation/dictionary-category/view";
	}
	
	/**
	 * 绑定数据，如果存在id时获取后在做其他操作
	 * 
	 */
	@ModelAttribute("entity")
	public DictionaryCategory bindingModel(HttpServletRequest request) {
		String id = request.getParameter("id");
		DictionaryCategory dictionaryCategory = new DictionaryCategory();
		
		if (StringUtils.isNotEmpty(id)) {
			dictionaryCategory = systemDictionaryManager.getDictionaryCategory(id);
		}
		
		return dictionaryCategory;
	}
	
}
