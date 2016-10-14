package com.leederedu.qsearch.core.highlighter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import com.leederedu.qsearch.core.bean.SearchResultBean;
import com.leederedu.qsearch.utils.Constants;
import com.leederedu.qsearch.core.manager.IndexManager;

public class HighLightHelper {

	private Analyzer analyzer;
	
	public HighLightHelper() {
		super();
	}

	public HighLightHelper(Analyzer analyzer) {
		super();
		this.analyzer = analyzer;
	}
	
	public HighLightHelper(String indexName) {
		super();
		this.analyzer = IndexManager.getIndexManager(indexName).getAnalyzer();
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * 给结果集添加高亮显示
	 * @param query 查询
	 * @param resultBean 结果集JavaBean
	 * @param analyzer 分词器
	 * @param fieldNames 键名
	 * @return 返回一个添加了关键字高亮的结果集
	 * @throws IOException
	 * @throws InvalidTokenOffsetsException
	 */
	public List<Map<String, Object>> addHighLight(Query query, SearchResultBean resultBean, String[] fieldNames) throws IOException, InvalidTokenOffsetsException {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		// 高亮显示关键字，如果内容中本来就有<span></span>，可能导致显示错乱
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));  
		if (resultBean != null && resultBean.getCount() > 0) {
			for (Document doc : resultBean.getDatas()) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(Constants.FIELD_NAME.ID, doc.get(Constants.FIELD_NAME.ID));
				for (String fieldName : fieldNames) {
					if (Constants.FIELD_NAME.ID.equals(fieldName)) {
						continue;
					}
					TokenStream tokenStream = analyzer.tokenStream(fieldName, new StringReader(doc.get(fieldName)));  
					String fieldValue = highlighter.getBestFragment(tokenStream,  doc.get(fieldName));
					map.put(fieldName, fieldValue);
				}
				list.add(map);
			}
		}
		return list;
	}
	
}
