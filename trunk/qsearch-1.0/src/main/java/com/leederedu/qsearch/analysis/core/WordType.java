package com.leederedu.qsearch.analysis.core;

/**
 * 词元类型
 * 
 * @author TaneRoom
 * @since 2016年9月12日 下午4:41:14
 */
public class WordType {

	// CharType常量
	/** 词元类型常量:未知 */
	public static final int TYPE_UNKNOWN = 0;
	/** 词元类型常量:英文 */
	public static final int TYPE_ENGLISH = 1;
	/** 词元类型常量:数字 */
	public static final int TYPE_ARABIC = 2;
	/** 词元类型常量:英文数字混合 */
	public static final int TYPE_LETTER = 3;
	/** 词元类型常量:中文词元 */
	public static final int TYPE_CNWORD = 4;
	/** 词元类型常量:中文单字 */
	public static final int TYPE_CNCHAR = 64;
	/** 词元类型常量:日韩文字 */
	public static final int TYPE_OTHER_CJK = 8;
	/** 词元类型常量:中文数词 */
	public static final int TYPE_CNUM = 16;
	/** 词元类型常量:中文量词 */
	public static final int TYPE_COUNT = 32;
	/** 词元类型常量:中文数量词 */
	public static final int TYPE_CQUAN = 48;
	
}
