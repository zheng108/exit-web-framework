package org.exitsoft.project.vcsadmin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

/**
 * UUID主键父类
 * @author vincent
 *
 */
@MappedSuperclass
public class UniversallyUniqueIdentifier implements Serializable{
	
	protected String id;
	
	/**
	 * 获取主键ID
	 * 
	 * @return String
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(nullable = false,length=32,columnDefinition="char(32)")
	@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDHexGenerator")
	public String getId() {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return this.id;
	}

	/**
	 * 设置主键ID，
	 * @param id
	 */
	public void setId(String id) {
		if (StringUtils.isEmpty(id)) {
			this.id = null;
		} else {
			this.id = id;
		}
		
	}

}
