package org.exitsoft.project.vcsadmin.entity.foundation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.orm.core.PropertyType;
import org.exitsoft.orm.enumeration.ExecuteMehtod;
import org.exitsoft.orm.strategy.annotation.ConvertCode;
import org.exitsoft.orm.strategy.annotation.ConvertProperty;
import org.exitsoft.project.vcsadmin.common.strategy.PinYinWuBiConvertStrategy;
import org.exitsoft.project.vcsadmin.entity.UniversallyUniqueIdentifier;
import org.hibernate.annotations.NamedQuery;


/**
 * 数据字典实体
 * 
 * @author vincent
 *
 */
@Entity
@Table(name="TB_DATA_DICTIONARY")
@NamedQuery(readOnly=true,name=DataDictionary.FindByCateGoryCode,query="from DataDictionary dd where dd.category.code = ?")
@ConvertCode(
	convertPropertys={
			@ConvertProperty(propertyNames={"wubiCode","pinYinCode"},
			strategyClass=PinYinWuBiConvertStrategy.class)
	},
	fromProperty="name",
	executeMehtod=ExecuteMehtod.Save
)
public class DataDictionary extends UniversallyUniqueIdentifier{
	
	public static final String FindByCateGoryCode = "findByCateGoryCode";
	
	//名称
	private String name;
	//值
	private String value;
	//类型
	private String type = "S";
	//备注
	private String remark;
	//所属类别
	public DictionaryCategory category;
	//五笔编码
	private String wubiCode;
	//拼音编码
	private String pinYinCode;
	
	public DataDictionary() {
		
	}
	
	/**
	 * 获取名称
	 * 
	 * @return String
	 */
	@Column(length=512,nullable=false)
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取值
	 * 
	 * @return String
	 */
	@Column(length=64,nullable=false)
	public String getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value 值
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取值类型
	 * 
	 * @return String
	 */
	@Column(length=1,nullable=false)
	public String getType() {
		return type;
	}

	/**
	 * 设置值类型
	 * 
	 * @param type 值类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取备注
	 * 
	 * @return String
	 */
	@Column(columnDefinition="text")
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取所属类别
	 * 
	 * @return {@link DictionaryCategory}
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "FK_CATEGORY_ID",columnDefinition="char(32)",nullable=false)
	public DictionaryCategory getCategory() {
		return category;
	}

	/**
	 * 设置所属类别
	 * 
	 * @param category 所属类别
	 */
	public void setCategory(DictionaryCategory category) {
		this.category = category;
	}

	/**
	 * 获取五笔编码
	 * 
	 * @return String
	 */
	@Column(length=512)
	public String getWubiCode() {
		return wubiCode;
	}

	/**
	 * 设置五笔编码
	 * 
	 * @param wubiCode 五笔编码
	 */
	public void setWubiCode(String wubiCode) {
		this.wubiCode = wubiCode;
	}

	/**
	 * 获取拼音编码
	 * 
	 * @return String
	 */
	@Column(length=512)
	public String getPinYinCode() {
		return pinYinCode;
	}

	/**
	 * 设置拼音编码
	 * 
	 * @param pinYinCode 拼音编码
	 */
	public void setPinYinCode(String pinYinCode) {
		this.pinYinCode = pinYinCode;
	}
	
	/**
	 * 根据type属性的值获取真正的值
	 * 
	 * @return Object
	 */
	@Transient
	public Object getReadValue() {
		return ConvertUtils.convertToObject(this.value, PropertyType.valueOf(type).getValue());
	}
}
