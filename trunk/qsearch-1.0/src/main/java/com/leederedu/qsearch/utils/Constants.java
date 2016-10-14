package com.leederedu.qsearch.utils;

/**
 * 常量封装
 * @author TaneRoom
 * @since 2016年9月23日 上午11:18:46
 */
public class Constants {

	/**
	 * FIELD域常量
	 * @author TaneRoom
	 * @since 2016年9月23日 上午11:17:26
	 */
	public class FIELD_NAME {
		/**
		 * 域名称为ID的统一命名，便于后面的删改
		 */
		public static final String ID = "id";
		/**
		 * 标题-不使用分词
		 */
		public static final String TITLE = "title";
		/**
		 * 标题-使用分词
		 */
		public static final String TITLE_ANALYZER = "titleAnalyzer";
		/**
		 * 标题的全拼
		 */
		public static final String TITLE_PINYIN = "titlePinyin";
		/**
		 * 标题的拼音简写
		 */
		public static final String TITLE_PINYIN_HEAD = "titlePinyinHead";
		
		/**
		 * 权值，每次被搜索就递增一次，为后面的排序做一个依据
		 */
		public static final String BOOST = "boost";
	}
	
	/**
	 * FIELD域值
	 * @author TaneRoom
	 * @since 2016年9月28日 下午2:30:54
	 */
	public class FIELD_VALUE {
		/**
		 * 默认boost初始值
		 */
		public static final float DEFAULT_BOOST = 1.0f;
	}
	
}
