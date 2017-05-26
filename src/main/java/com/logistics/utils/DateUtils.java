package com.logistics.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author zys
 *
 */
public class DateUtils {

	public static String parseIntoString(Date date) {
		
		String defaultDate = "2017-7-1 12:12:12";
		if (date != null) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			defaultDate = sdf.format(date);
		}
		
		return defaultDate;
	}
}
