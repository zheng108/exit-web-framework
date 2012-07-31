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
import org.exitsoft.showcase.vcsadmin.common.SystemVariableUtils;
import org.exitsoft.showcase.vcsadmin.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.vcsadmin.entity.foundation.DataDictionary;
import org.exitsoft.showcase.vcsadmin.service.foundation.SystemDictionaryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 数据字典管理Controller
 * 
 * @author vincent
 *
 */
@Controller
@RequestMapping("/foundation/data-dictionary")
public class DataDictionaryController {
	
	@Autowired
	private SystemDictionaryManager systemDictionaryManager;
	
	/**
	 * 获取数据字典列表
	 * 
	 * @return {@link Page}
	 */
	@RequestMapping("view")
	public Page<DataDictionary> view(PageRequest pageRequest,HttpServletRequest request) {
		
		List<PropertyFilter> filters = PropertyFilterRestrictionHolder.buildFromHttpRequest(request);
		
		if (!pageRequest.isOrderBySetted()) {
			pageRequest.setOrderBy("id");
			pageRequest.setOrderDir(Sort.DESC);
		}
		
		request.setAttribute("categoriesList", systemDictionaryManager.getAllDictionaryCategories());
		
		return systemDictionaryManager.searchDataDictionaryPage(pageRequest, filters);
	}
	
	/**
	 * 
	 * 保存或更新数据字典
	 * 
	 */
	@RequestMapping("save")
	public String save(@ModelAttribute("entity") DataDictionary entity,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		String categoryId = request.getParameter("categoryId");
		
		if (StringUtils.isEmpty(categoryId)) {
			entity.setCategory(null);
		} else {
			entity.setCategory(systemDictionaryManager.getDictionaryCategory(categoryId));
		}
		
		systemDictionaryManager.saveDataDictionary(entity);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		
		return "redirect:/foundation/data-dictionary/view";
	}
	
	/**
	 * 
	 * 读取数据字典
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
		
		return "/foundation/data-dictionary/read";
		
	}
	
	/**
	 * 删除数据字典
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		systemDictionaryManager.deleteDataDictionary(ids);
		redirectAttributes.addFlashAttribute("message", "删除" + ids.size() + "条信息成功");
		return "redirect:/foundation/data-dictionary/view";
	}
	
	/**
	 * 绑定数据，如果存在id时获取后在做其他操作
	 * 
	 */
	@ModelAttribute("entity")
	public DataDictionary bindingModel(HttpServletRequest request) {

		request.setAttribute("valueTypes", SystemVariableUtils.getDataDictionariesByCategoryCode(SystemDictionaryCode.ValueType));
		
		String id = request.getParameter("id");
		DataDictionary dataDictionary = new DataDictionary();
		
		if (StringUtils.isNotEmpty(id)) {
			dataDictionary = systemDictionaryManager.getDataDictionary(id);
		}
		
		return dataDictionary;
	}
}
