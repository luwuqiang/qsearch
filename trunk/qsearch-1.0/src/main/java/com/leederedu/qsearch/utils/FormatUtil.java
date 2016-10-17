package com.leederedu.qsearch.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式化帮助类
 * @author TaneRoom
 * @since 2016年8月12日 上午10:44:20
 */
public class FormatUtil {

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
	
	/**
	 * 格式化时间格式(年-月-日 时:分:秒)
	 * @param date 需要格式化的时间
	 * @return 返回格式化的时间
	 */
	public static String formatTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		return sdf.format(date);
	}
	
	/**
	 * 格式化日期格式(年-月-日)
	 * @param date 需要格式化的日期
	 * @return 返回格式化的日期
	 */
	public static String formatDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}
	
}
