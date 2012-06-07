package org.exitsoft.project.vcsadmin.common.strategy;

import org.exitsoft.orm.strategy.CodeStrategy;

/**
 * 拼音五笔转码则略
 * 
 * @author vincent
 *
 */
public class PinYinWuBiConvertStrategy implements CodeStrategy{
	
	@Override
	public Object convertCode(Object value,String propertyName) {
		if (value == null) {
			return "";
		}
		if (propertyName.equals("wubiCode")) {
			return WuBiConvert.getAcronym(value.toString());
		} else {
			return PinYinConvert.getAcronym(value.toString(), true);
		}
	}
	
}
