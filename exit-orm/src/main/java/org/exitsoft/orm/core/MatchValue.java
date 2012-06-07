package org.exitsoft.orm.core;

import java.util.List;

/**
 * 对比值实体,根据该类的信息进行对一个或多个属性值该如何是 or或者and进行条件关联
 * 
 * @author vincent
 *
 */
public class MatchValue {
	
	private boolean hasOrOperate;
	private List<Object> values;
	
	public MatchValue(boolean hasOrOperate,List<Object> values) {
		this.hasOrOperate = hasOrOperate;
		this.values = values;
	}
	
	/**
	 * 获取是否存在or条件对比
	 * 
	 * @return boolean
	 */
	public boolean hasOrOperate() {
		return hasOrOperate;
	}
	
	/**
	 * 获取要对比的所有值
	 * 
	 * @return List
	 */
	public List<Object> getValues() {
		return values;
	}
}
