package com.leederedu.qsearch.analysis.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * Configuration 默认实现
 * 
 * @author TaneRoom
 * @since 2016年9月6日 下午4:41:30
 */
public class DefaultConfig implements Configuration {

	private static final String SYNONYMS_PATH = "ext_synonyms";
	/** 分词器默认字典路径 */
	private static final String PATH_DIC_MAIN = "analysis/main.dic";
	/** 分词器默认量词词典路径 */
	private static final String PATH_DIC_QUANTIFIER = "analysis/quantifier.dic";
	/** 分词器配置文件路径 */
	private static final String FILE_NAME = "analysis/SmartZhAnalyzer.cfg.xml";
	/** 配置属性——扩展字典 */
	private static final String EXT_DICT = "ext_dict";
	/** 配置属性——扩展停止词典 */
	private static final String EXT_STOP = "ext_stopwords";
	/** 配置文件 */
	private Properties props;

	/**
	 * 返回单例
	 * @return Configuration单例
	 */
	public static Configuration getInstance() {
		return new DefaultConfig();
	}

	/**
	 * 初始化配置文件
	 */
	private DefaultConfig() {
		props = new Properties();
		InputStream input = this.getClass().
				getClassLoader().getResourceAsStream(FILE_NAME);
		if (input != null) {
			try {
				props.loadFromXML(input);
				//System.out.println(props.get("ext_stopwords"));
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getMainDictionary(){
		return PATH_DIC_MAIN;
	}

	@Override
	public String getQuantifierDicionary() {
		return PATH_DIC_QUANTIFIER;
	}

	@Override
	public List<String> getExtDictionarys() {
		List<String> extDictFiles = new ArrayList<String>(2);
		String extDictCfg = props.getProperty(EXT_DICT);
		 
		if(extDictCfg != null){
			//使用;分割多个扩展字典配置
			String[] filePaths = extDictCfg.split(";");
			if(filePaths != null){
				for(String filePath : filePaths){
					if(filePath != null && !"".equals(filePath.trim())){
						extDictFiles.add(filePath.trim());
					}
				}
			}
		}		
		return extDictFiles;
	}

	@Override
	public List<String> getExtStopWordDictionarys() {
		List<String> extStopWordDictFiles = new ArrayList<String>(2);
		String extStopWordDictCfg = props.getProperty(EXT_STOP);
		
		if(extStopWordDictCfg != null){
			//使用;分割多个扩展字典配置
			String[] filePaths = extStopWordDictCfg.split(";");
			if(filePaths != null){
				for(String filePath : filePaths){
					if(filePath != null && !"".equals(filePath.trim())){
						extStopWordDictFiles.add(filePath.trim());
					}
				}
			}
		}		
		return extStopWordDictFiles;
	}

	@Override
	public String getSynonymsPath() {
		String ss = props.getProperty(SYNONYMS_PATH);
		if (ss != null) {
			return ss;
		}
		return "";
	}

}
