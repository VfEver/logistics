package com.logistics.utils;
/**
 * string utils class
 * @author zys
 *
 */
public class StringUtils {

	/**
	 * 判断字符串是否为空
	 * @param inpput
	 * @return
	 */
	public static boolean isEmpty(String input) {
		
		if(input == null || input.length() <= 0 || "".equals(input)) {
			return true;
		} 
		return false;
	}
}
