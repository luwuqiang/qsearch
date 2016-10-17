package com.leederedu.qsearch.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.util.ClasspathResourceLoader;

import com.leederedu.qsearch.analysis.conf.DefaultConfig;

/**
 * 带近义词的中文分词器
 * @author TaneRoom
 * @since 2016年9月13日 下午3:46:25
 */
public class SynSmartZhAnalyzer extends Analyzer {

	private String synonymsPath = "analysis/synonyms.txt";//近义词路径
	private SynonymMap map;// 近义词集合
	private Map<String, String> params=null;
	private SynonymFilterFactory filter = null;
	
	public String getSynonymsPath() {
		return synonymsPath;
	}

	public void setSynonymsPath(String synonymsPath) {
		this.synonymsPath = synonymsPath;
	}

	public SynonymMap getMap() {
		return map;
	}

	public void setMap(SynonymMap map) {
		this.map = map;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	/**
	 * 构造方法，默认加载classpath目录下面的stopwords.txt文件
	 */
	public SynSmartZhAnalyzer() {
		super();
		this.synonymsPath = DefaultConfig.getInstance().getSynonymsPath();
		initalSynonmFilter();
	}

	/**
	 * 构造方法，默认加载classpath目录下面的stopwords.txt文件
	 */
	public SynSmartZhAnalyzer(SynonymMap map) {
		super();
		this.synonymsPath = DefaultConfig.getInstance().getSynonymsPath();
		this.map = map;
		initalSynonmFilter();
	}

	/**
	 * 构造方法<br/>
	 * <font color="red">注意：synonymsFile路径必须是基于classpath的相对路径</font>
	 * @param synonymsFile 近义词路径
	 */
	public SynSmartZhAnalyzer(String synonymsPath, SynonymMap map) {
		super();
		this.map = map;
		this.synonymsPath = synonymsPath;
		initalSynonmFilter();
	}

	/**
	 * 初始化同义词构造器
	 */
	public void initalSynonmFilter(){
		if (params == null) {
			params = new HashMap<String, String>();
			params.put("expand", "true");
			params.put("synonyms", synonymsPath);
			params.put("autoupdate", "false");
			params.put("flushtime", "10");
		}
		try {
			filter=new SynonymFilterFactory(params);
			filter.inform(new ClasspathResourceLoader(), map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new SmartZhTokenizer();
		TokenStream result = filter.create(source);
		return new TokenStreamComponents(source, result);
	}

}
