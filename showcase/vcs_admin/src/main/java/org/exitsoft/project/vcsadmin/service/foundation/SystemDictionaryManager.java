package org.exitsoft.project.vcsadmin.service.foundation;

import java.util.List;

import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.project.vcsadmin.dao.foundation.DataDictionaryDao;
import org.exitsoft.project.vcsadmin.dao.foundation.DictionaryCategoryDao;
import org.exitsoft.project.vcsadmin.entity.foundation.DataDictionary;
import org.exitsoft.project.vcsadmin.entity.foundation.DictionaryCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统字典管理
 * 
 * @author vincent
 *
 */
@Service
@Transactional
public class SystemDictionaryManager {

	//数据字典数据访问
	@Autowired
	private DataDictionaryDao dataDictionaryDao;
	
	//字典类别数据访问
	@Autowired
	private DictionaryCategoryDao dictionaryCategoryDao;
	
	//---------------------------------------数据字典管理---------------------------------------//
	
	/**
	 * 获取数据字典实体
	 * 
	 * @param id 数据字典id
	 */
	public DataDictionary getDataDictionary(String id) {
		return dataDictionaryDao.get(id);
	}
	
	/**
	 * 保存数据字典
	 * 
	 * @param entity 数据字典实体
	 */
	@CacheEvict(value=DataDictionary.FindByCateGoryCode,allEntries=true)
	public void saveDataDictionary(DataDictionary entity) {
		dataDictionaryDao.save(entity);
	}
	
	/**
	 * 删除数据字典
	 * 
	 * @param ids 数据字典id集合
	 */
	@CacheEvict(value=DataDictionary.FindByCateGoryCode,allEntries=true)
	public void deleteDataDictionary(List<String> ids) {
		dataDictionaryDao.deleteAll(ids);
	}
	
	/**
	 * 获取数据字典分页对象
	 * 
	 * @param request 分页参数请求
	 * @param filters 属性过滤器
	 * 
	 * @return Page
	 */
	public Page<DataDictionary> searchDataDictionary(PageRequest request,List<PropertyFilter> filters) {
		return dataDictionaryDao.findPage(request, filters);
	}
	
	/**
	 * 通过字典类别代码获取数据字典集合
	 * 
	 * @param categoryCode 字典列别代码
	 * 
	 * @return List
	 */
	@Cacheable(value=DataDictionary.FindByCateGoryCode,key="#categoryCode")
	public List<DataDictionary> getDataDictionariesByCategoryCode(String categoryCode) {
		
		return dataDictionaryDao.findByQueryName(DataDictionary.FindByCateGoryCode, categoryCode);
	}
	
	//---------------------------------------字典类别管理---------------------------------------//
	
	/**
	 * 获取字典类别实体
	 * 
	 * @param id 数据字典id
	 */
	public DictionaryCategory getDictionaryCategory(String id) {
		return dictionaryCategoryDao.get(id);
	}
	
	/**
	 * 保存字典类别
	 * 
	 * @param entity 字典类别实体
	 */
	public void saveDictionaryCategory(DictionaryCategory entity) {
		dictionaryCategoryDao.save(entity);
	}
	
	/**
	 * 删除字典类别
	 * 
	 * @param id 字典类别id
	 */
	public void deleteDictionaryCategory(String id) {
		dictionaryCategoryDao.delete(id);
	}
	
	/**
	 * 获取所有父类字典类别
	 * 
	 * @return List
	 */
	public List<DictionaryCategory> getAllParentDictionaryCategories() {
		return dictionaryCategoryDao.findByProperty("parent.id", null);
	}
	
	/**
	 * 获取所有字典类别
	 * 
	 * @return List
	 */
	public List<DictionaryCategory> getAllDictionaryCategories() {
		return dictionaryCategoryDao.getAll();
	}

}
