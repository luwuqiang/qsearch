package com.leederedu.qsearch.analysis.conf;

import java.util.List;

/**
 * 配置管理类接口
 * @author TaneRoom
 * @since 2016年9月6日 下午4:37:01
 */
public interface Configuration {

	/**
	 * 获取主词典路径
	 * @return 主词典路径
	 */
	public String getMainDictionary();

	/**
	 * 获取量词词典路径
	 * @return 量词词典路径
	 */
	public String getQuantifierDicionary();

	/**
	 * 获取扩展字典配置路径
	 * @return 相对类加载器的路径
	 */
	public List<String> getExtDictionarys();


	/**
	 * 获取扩展停止词典配置路径
	 * @return 相对类加载器的路径
	 */
	public List<String> getExtStopWordDictionarys();
	
	
	/**
	 * 获取同义词典的路径
	 * @return 相对类的加载器
	 */
	public String getSynonymsPath();
	
}
