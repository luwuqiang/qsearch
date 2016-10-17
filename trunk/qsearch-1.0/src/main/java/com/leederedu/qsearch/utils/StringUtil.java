package com.leederedu.qsearch.utils;

/**
 * 字符串处理相关帮助类
 * @author TaneRoom
 * @since 2016年8月3日 下午3:51:55
 */
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * @param cs 有序字符集合
	 * @return 返回是否为空
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断字符串是否不为空
	 * @param cs 有序字符集合
	 * @return 返回是否不为空
	 */
	public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
	
}
