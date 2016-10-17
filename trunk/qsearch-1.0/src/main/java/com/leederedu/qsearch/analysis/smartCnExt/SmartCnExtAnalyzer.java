package com.leederedu.qsearch.analysis.smartCnExt;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.cn.smart.HMMChineseTokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.ClasspathResourceLoader;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;

/**
 * 自定义分词器<br/>
 * 对近义词有匹配
 * @author TaneRoom
 * @since 2016年9月2日 下午5:46:59
 */
public class SmartCnExtAnalyzer extends StopwordAnalyzerBase {

	private String stopWordPath;
	
	public SmartCnExtAnalyzer() {
		super();
		this.stopWordPath = this.getClass().getClassLoader().
				getResource("stopwords.txt").getFile().substring(1);
	}
	
	/**
	 * 构造方法<br/>
	 * 构造一个分词器，并制定禁止词路径
	 * @param stopWordPath 禁止词路径
	 */
	public SmartCnExtAnalyzer(String stopWordPath) {
		super();
		this.stopWordPath = stopWordPath;
	}
	
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer tokenizer = new HMMChineseTokenizer();
		Map<String,String> paramsMap = new HashMap<String,String>();
		paramsMap.put("synonyms", "synonyms.txt");
		paramsMap.put("expand", "true");
		paramsMap.put("ignoreCase", "false");
		SynonymFilterFactory factory = null;
		try {
			factory = new SynonymFilterFactory(paramsMap);
			factory.inform(new ClasspathResourceLoader());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TokenStream tokenStream = factory.create(tokenizer);
		try {
			CharArraySet stopWords = loadStopwordSet(Paths.get(stopWordPath));
			tokenStream = new StopFilter(tokenStream, stopWords);//过滤掉禁止词
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new TokenStreamComponents(tokenizer, tokenStream);
	}

}
