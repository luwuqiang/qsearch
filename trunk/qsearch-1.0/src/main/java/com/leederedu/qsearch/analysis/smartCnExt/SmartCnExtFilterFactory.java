package com.leederedu.qsearch.analysis.smartCnExt;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.cn.smart.HMMChineseTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.TokenFilterFactory;

/**
 * 近义词过滤工厂
 * @author TaneRoom
 * @since 2016年9月2日 下午6:32:20
 */
public class SmartCnExtFilterFactory extends TokenFilterFactory {

	private String synonyms;//近义词文件
	private SynonymMap map;//近义词集合
	private boolean ignoreCase;
	private boolean expand;
	
	protected SmartCnExtFilterFactory(Map<String, String> paramsMap) {
		super(paramsMap);
		this.synonyms = get(paramsMap, "synonyms");
		this.ignoreCase = getBoolean(paramsMap, "ignoreCase", false);
		this.expand = getBoolean(paramsMap, "expand", true);
	}

	public void inform(ResourceLoader loader) throws Exception {
		Analyzer analyzer = new Analyzer(){
			@Override
			protected TokenStreamComponents createComponents(String fieldName) {
				Tokenizer tokenizer = new HMMChineseTokenizer();
				TokenStream tokenStream = SmartCnExtFilterFactory.this.ignoreCase ? new LowerCaseFilter(tokenizer) : tokenizer;
				return new TokenStreamComponents(tokenizer, tokenStream);
			}
		};
		try {
			this.map = loadSolrSynonyms(loader, true, analyzer);// 加载配置文件使用
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载配置文件使用
	 * @param loader 加载器
	 * @param dedup 
	 * @param analyzer 分词器
	 * @return
	 */
	private SynonymMap loadSolrSynonyms(ResourceLoader loader, boolean dedup, Analyzer analyzer) throws Exception {
		if (this.synonyms == null) {
			throw new RuntimeException("缺乏请求参数'synonyms'!");
		}
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPORT)
				.onUnmappableCharacter(CodingErrorAction.REPORT);
		SolrSynonymParser parser = new SolrSynonymParser(dedup, this.expand, analyzer);
		File synonymFile = new File(this.synonyms);
		if (loader != null) {
			if (synonymFile.exists()) {
				decoder.reset();
				parser.parse(new InputStreamReader(loader.openResource(this.synonyms), decoder));
			} else {
				List<String> files = splitFileNames(this.synonyms);
				for (String file : files) {
					decoder.reset();
					parser.parse(new InputStreamReader(loader.openResource(file), decoder));
				}
			}
		}
		return parser.build();
	}

	@Override
	public TokenStream create(TokenStream input) {
		if (input == null) {
			System.out.println("TokenStream为空！");
		}
		if (this.map == null) {
			System.out.println("map为空！");
		}
		return this.map.fst == null ? input : new SynonymFilter(input, this.map, this.ignoreCase);
	}

}
