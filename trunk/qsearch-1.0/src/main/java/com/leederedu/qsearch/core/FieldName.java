package com.leederedu.qsearch.core;

/**
 * Field方法名
 * @author TaneRoom
 * @since 2016年9月23日 上午11:06:41
 */
public enum FieldName {
	/** 用于字符串-不分词，索引，存储 */
	STRINGFIELD, 
	/** 用于字符串-分词，索引，存储 */
	TEXTFIELD, 
	/** 用于整型-不分词，索引，存储 */
	INTFIELD, 
	/** 用于长整型不分词，索引，存储 */
	LONGFIELD, 
	/** 用于浮点型-不分词，索引，存储*/
	FLOATFIELD, 
	/** 用于双精度浮点型-不分词，索引，存储*/
	DOUBLEFIELD
}
