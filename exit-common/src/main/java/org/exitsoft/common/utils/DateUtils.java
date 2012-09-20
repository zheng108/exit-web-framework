package org.exitsoft.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 时间工具类 扩展rg.apache.commons.lang3.time.DateUtils
 * 
 * @author vincent
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils{
	
	/**
	 * 存储SimpleDateFormat对应格式发的String类型
	 */
	private static Map<String, SimpleDateFormat> map = new HashMap<String, SimpleDateFormat>();
	
	/**
	 * 格式化时间，返回格式化后的时间字符串
	 * 
	 * <pre>
	 * 例子,如有一个Date = 2012-08-09:
	 * DateUtils.format(date,"yyyy-MM-dd") = "2012-08-09"
	 * DateUtils.format(date,"yyyy年MM月dd日") = "2012年08月09日"
	 * DateUtils.format(date,"") = null
	 * DateUtils.format(date,null) = null
	 * </pre>
	 * 
	 * @param date 时间
	 * @param parsePatterns 格式化字符串
	 * 
	 * @return String
	 */
	public static String format(Date date,String parsePatterns) {
		
		if(StringUtils.isEmpty(parsePatterns) || date == null) {
			return null;
		}
		
		return getSimpleDateFormat(parsePatterns).format(date);
	}
	
	/**
	 * 通过时间格式化字符串获取SimpleDateFormat
	 * 
	 * @param parsePatterns  时间格式化
	 * 
	 * @return {@link SimpleDateFormat}
	 */
	public static SimpleDateFormat getSimpleDateFormat(String parsePatterns) {
		SimpleDateFormat dateFormat = null;
		if (map.containsKey(parsePatterns)) {
			dateFormat = map.get(parsePatterns);
		} else {
			dateFormat =  new SimpleDateFormat(parsePatterns);
			map.put(parsePatterns, dateFormat);
		}
		return dateFormat;
	}
}
