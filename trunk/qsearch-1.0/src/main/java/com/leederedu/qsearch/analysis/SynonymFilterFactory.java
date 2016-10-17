package com.leederedu.qsearch.analysis;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 近义词工厂
 * @author TaneRoom
 * @since 2016年9月13日 上午10:16:09
 */
public class SynonymFilterFactory extends TokenFilterFactory implements Runnable {

	private String synonyms;// 近义词路径
	private String tokenizerFactory;// tokenizer工厂
	private String format;// 近义词格式(solr/wordnet)
	private String analyzerName;// analyzer名称
	private SynonymMap map;// 近义词集合
	//private boolean loadremote;// 是否远程加载
	private boolean ignoreCase;// 是否忽略大小写
	private boolean expand;// 是否支持扩展
	private ResourceLoader loader = null;// 资源加载器
	boolean isAutoUpdate;// 是否自动更新
	Analyzer analyzer = null;// 分词器
	int flushtime;// 刷新时间

	private final Map<String, String> tokArgs = new HashMap<>();

	protected SynonymFilterFactory(Map<String, String> params) {
		super(params);
		this.synonyms = get(params, "synonyms");
		this.tokenizerFactory = get(params, "tokenizerFactory");
		this.format = get(params, "format");
		this.analyzerName = get(params, "analyzer");
		//this.loadremote = getBoolean(params, "loadremote", false);
		this.ignoreCase = getBoolean(params, "ignoreCase", false);
		this.expand = getBoolean(params, "expand", false);
		this.isAutoUpdate = getBoolean(params, "autoupdate", false);
		this.flushtime = getInt(params, "flushtime", 10);

		if (analyzerName != null && tokenizerFactory != null) {
			throw new IllegalArgumentException("Analyzer and TokenizerFactory can't be specified both: " + analyzerName
					+ " and " + tokenizerFactory);
		}

		if (tokenizerFactory != null) {
			tokArgs.put("luceneMatchVersion", getLuceneMatchVersion().toString());
			for (Iterator<String> itr = params.keySet().iterator(); itr.hasNext();) {
				String key = itr.next();
				tokArgs.put(key.replaceAll("^tokenizerFactory\\.", ""), params.get(key));
				itr.remove();
			}
		}

		if (!params.isEmpty()) {
			throw new IllegalArgumentException("Unknown parameters: " + params);
		}
	}

	public void inform(ResourceLoader loader, SynonymMap map) throws IOException {
		final TokenizerFactory factory = tokenizerFactory == null ? null
				: loadTokenizerFactory(loader, tokenizerFactory);
		Analyzer analyzer;

		if (analyzerName != null) {
			analyzer = loadAnalyzer(loader, analyzerName);
		} else {
			analyzer = new Analyzer() {
				@Override
				protected TokenStreamComponents createComponents(String fieldName) {
					Tokenizer tokenizer = factory == null ? new WhitespaceTokenizer() : factory.create();
					TokenStream stream = ignoreCase ? new LowerCaseFilter(tokenizer) : tokenizer;
					return new TokenStreamComponents(tokenizer, stream);
				}
			};
		}

		try {
			// this.map = loadSolrSynonyms(true, analyzer);//加载配置文件使用
			// this.map = loadDBSynonyms(true, analyzer);//从数据库加载使用
//			if (loadremote) {// 是否远程加载
//				this.map = loadRedisSynonyms(true, analyzer);// 从redis加载使用
//			} else {
			if (map != null) {
				this.map = map;
			} else {
				String formatClass = format;
				if (format == null || format.equals("solr")) {
					formatClass = SolrSynonymParser.class.getName();
				} else if (format.equals("wordnet")) {
					formatClass = WordnetSynonymParser.class.getName();
				}
				this.map = loadSolrSynonyms(loader, formatClass, true, analyzer);// 加载配置文件使用
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Exception thrown while loading synonyms", e);
		}

		if ((this.isAutoUpdate) && (this.synonyms != null) && (!this.synonyms.trim().isEmpty())) {
			this.loader = loader;
			this.analyzer = analyzer;
		}
		if (isAutoUpdate) {
			ScheduledExecutorService updateService = Executors.newSingleThreadScheduledExecutor();
			updateService.scheduleAtFixedRate(this, 5, flushtime, TimeUnit.SECONDS);
		}
	}

	/**
	 * 从数据库加载同义词
	 * @param loader 资源加载器
	 * @param dedup
	 * @param analyzer 分词器
	 * @return 返回近义词集合
	 * @throws Exception
	 * @throws ParseException
	 */
//	private SynonymMap loadDBSynonyms(boolean dedup, Analyzer analyzer) throws Exception, ParseException {
//		// System.out.println("进同义词了....");
//		SolrSynonymParser parser = new SolrSynonymParser(dedup, this.expand, analyzer);
//		// String dbtxt = DBHelper.getKey("synonym");
//		String dbtxt = "";
//		dbtxt = dbtxt.replace("#", "\n");
//		parser.parse(new StringReader(dbtxt));
//		return parser.build();
//	}

	/**
	 * 从缓存加载同义词
	 * @param dedup
	 * @param analyzer 分词器
	 * @return
	 */
//	private SynonymMap loadRedisSynonyms(boolean dedup, Analyzer analyzer) {
//		SynonymMap smap = null;
//		SolrSynonymParser parser = new SolrSynonymParser(dedup, this.expand, analyzer);
//		try {
//			// String dbtxt = RedisHelper.getKey("synonym");
//			String dbtxt = "";
//			parser.parse(new StringReader(dbtxt));
//			smap = parser.build();
//		} catch (Exception e) {
//			log.error("从Redis加载同义词库异常！");
//		}
//		return smap;
//	}

	/**
	 * 从文件加载同义词<br>
	 * 以classpath为基础的路径
	 * @param loader 资源加载器
	 * @param cname 格式文件名
	 * @param dedup
	 * @param analyzer 分词器
	 * @return 返回同义词集合
	 * @throws IOException
	 * @throws ParseException
	 */
	private SynonymMap loadSolrSynonyms(ResourceLoader loader, String cname, boolean dedup, Analyzer analyzer)
			throws IOException, ParseException {
		if (this.synonyms == null) {
			throw new IllegalArgumentException("Missing required argument 'synonyms'.");
		}
		CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
				.onUnmappableCharacter(CodingErrorAction.REPORT);

		SynonymMap.Parser parser;
		Class<? extends SynonymMap.Parser> clazz = loader.findClass(cname, SynonymMap.Parser.class);
		try {
			parser = clazz.getConstructor(boolean.class, boolean.class, Analyzer.class).newInstance(dedup, expand,
					analyzer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		List<String> files = splitFileNames(synonyms);
		for (String file : files) {
			decoder.reset();
			try (final Reader isr = new InputStreamReader(loader.openResource(file), decoder)) {
				parser.parse(isr);
			}
		}
		return parser.build();
	}

	/**
	 * 通过工厂名加载Tokenizer工厂
	 * @param loader 资源加载器
	 * @param cname 工厂名
	 * @return 返回工厂实例
	 * @throws IOException
	 */
	private TokenizerFactory loadTokenizerFactory(ResourceLoader loader, String cname) throws IOException {
		Class<? extends TokenizerFactory> clazz = loader.findClass(cname, TokenizerFactory.class);
		try {
			TokenizerFactory tokFactory = clazz.getConstructor(Map.class).newInstance(tokArgs);
			if (tokFactory instanceof ResourceLoaderAware) {
				((ResourceLoaderAware) tokFactory).inform(loader);
			}
			return tokFactory;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过分词名加载分词器
	 * @param loader 资源加载器
	 * @param cname 分词器名
	 * @return 返回分词器实例
	 * @throws IOException
	 */
	private Analyzer loadAnalyzer(ResourceLoader loader, String cname) throws IOException {
		Class<? extends Analyzer> clazz = loader.findClass(cname, Analyzer.class);
		try {
			Analyzer analyzer = clazz.getConstructor().newInstance();
			if (analyzer instanceof ResourceLoaderAware) {
				((ResourceLoaderAware) analyzer).inform(loader);
			}
			return analyzer;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Logger log = LoggerFactory.getLogger(SynonymFilterFactory.class);

	@Override
	public TokenStream create(TokenStream input) {
		if (input == null) {
			System.out.println("input is null");
		}
		if (this.map == null) {
			System.out.println("map is null");
		}

		return this.map.fst == null ? input : new SynonymFilter(input, this.map, this.ignoreCase);
	}

	static SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void update() {
		try {
			if (isAutoUpdate) {
				if (this.map == null) {
					String formatClass = format;
					if (format == null || format.equals("solr")) {
						formatClass = SolrSynonymParser.class.getName();
					} else if (format.equals("wordnet")) {
						formatClass = WordnetSynonymParser.class.getName();
					}
					this.map = loadSolrSynonyms(loader, formatClass, true, analyzer);
				}
				log.info("{} 同义词库词库更新了.....", f.format(new Date()));
			}
		} catch (Exception e) {
			log.error("< 同义词库更新异常： SynonymFilterFactory> IOException!!", e);
		}
	}

	public void run() {
		this.update();
	}

}
