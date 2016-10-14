package com.leederedu.qsearch.core.query;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;

import com.leederedu.qsearch.core.manager.IndexManager;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;

/**
 * 对常用查询的一个封装
 * @author TaneRoom
 * @since 2016年9月7日 下午6:13:57
 */
public class PackQuery {

	Log log = LogFactory.getLog(PackQuery.class);
	
	// 分词器
	private Analyzer analyzer;

	// 使用索引中的分词器
	public PackQuery(String indexName) {
		analyzer = IndexManager.getIndexManager(indexName).getAnalyzer();
	}

	// 使用自定义分词器
	public PackQuery(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * 查询字符串匹配多个查询域
	 * @param key 查询关键字
	 * @param fields 查询域
	 * @return 返回查询对应的结果
	 * @throws ParseException
	 */
	public Query getMultiFieldQuery(String key, String[] fields) throws ParseException {
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,analyzer);
		return parser.parse(key);
	}
	
	/**
	 * 查询字符串匹配单个查询域
	 * @param key 查询关键字
	 * @param field 查询域
	 * @return 返回查询对应结果
	 * @throws ParseException
	 */
	public Query getOneFieldQuery(String key, String field) throws ParseException{
		if(key == null || key.length() < 1){
			return null;
		}
		QueryParser parser = new QueryParser(field,analyzer);
		return parser.parse(key);
	}
	
	/**
	 * 查询字符串、多个查询域以及查询域在查询语句中的关系
	 * @param key 查询关键字
	 * @param fields 查询域
	 * @param occur
	 * @return
	 * @throws IOException
	 */
	public Query getBooleanQuery(String key, String[] fields, Occur[] occur) throws IOException{
		if(fields.length != occur.length){
			log.info("fields.length isn't equals occur.length, please check params!");
			return null;
		}
		//在lucene5.x中，BooleanQuery的实例化变成了BooleanQuery.Builder
		//BooleanQuery query = new BooleanQuery();
		BooleanQuery.Builder query = new BooleanQuery.Builder();
		TokenStream tokenStream = analyzer.tokenStream("", new StringReader(key));
		List<String> analyzerKeys = new ArrayList<String>();
		tokenStream.reset(); 
		while(tokenStream.incrementToken()){
			CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
			analyzerKeys.add(term.toString());
		}
		for(int i = 0 ; i < fields.length ; i++){
			for(String analyzerKey : analyzerKeys){
				TermQuery termQuery = new TermQuery(new Term(fields[i],analyzerKey));
				query.add(termQuery,occur[i]);
			}
		}
		tokenStream.end();
		tokenStream.close();
		return query.build();
	}
	
	/**
	 * 组合多个查询，之间的关系有occur决定
	 * @param querys
	 * @param occurs
	 * @return
	 */
	public Query getBooleanQuery(List<Query> querys,List<Occur> occurs){
		if(querys.size() != occurs.size()){
			log.info("querys.size() isn't equals occurs.size(), please check params!");
			return null;
		}
		//在lucene5.x中，BooleanQuery的实例化变成了BooleanQuery.Builder
		//BooleanQuery query = new BooleanQuery();
		BooleanQuery.Builder query = new BooleanQuery.Builder();
		for(int i = 0 ; i < querys.size() ; i++){
			query.add(querys.get(i),occurs.get(i));
		}
		//return query;
		return query.build();
	}
	
	/**
	 * StringField属性的搜索
	 * @param value
	 * @param fieldName
	 * @return
	 */
	public Query getStringFieldQuery(String value,String fieldName){
		return new TermQuery(new Term(fieldName,value));
	}
	
	/**
	 * 多个StringField属性的搜索
	 * @param value
	 * @param fields
	 * @param occur
	 * @return
	 */
	public Query getStringFieldQuery(String[] value, String[] fields, Occur occur){
		if(value == null || fields == null || value.length != fields.length){
			return null;
		}
		List<Query> querys = new ArrayList<Query>();
		List<Occur> occurs = new ArrayList<Occur>();
		for(int i = 0 ; i < fields.length ; i++){
			querys.add(getStringFieldQuery(value[i], fields[i]));
			occurs.add(occur);
		}
		return getBooleanQuery(querys, occurs);
	}
	
	/**
	 * key开头的查询字符串，和单个域匹配
	 * @param key
	 * @param field
	 * @return
	 */
	private Query getStartQuery(String key, String field) {
		if(key == null || field.length() < 1){
			return null;
		}
		return new PrefixQuery(new Term(field,key));
	}
	
	/**
	 * key开头的查询字符串，和多个域匹配，每个域之间的关系由occur决定
	 * @param key
	 * @param fields
	 * @param occur
	 * @return
	 */
	public Query getStartQuery(String key,String[] fields,Occur occur){
		if(key == null || fields.length < 1){
			return null;
		}
		List<Query> querys = new ArrayList<Query>();
		List<Occur> occurs = new ArrayList<Occur>();
		for(String field : fields){
			querys.add(getStartQuery(key, field));
			occurs.add(occur);
		}
		return getBooleanQuery(querys, occurs);
	}
	
	/**
	 * key开头的查询字符串，和多个域匹配，每个域之间的关系由occur决定
	 * @param key
	 * @param fields
	 * @param occur
	 * @param isAnalyzer 是否分词
	 * @return
	 * @throws IOException 
	 */
	public Query getStartQuery(String key,String[] fields,Occur occur,boolean isAnalyzer) throws IOException{
		if(key == null || fields.length < 1){
			return null;
		}
		List<Query> querys = new ArrayList<Query>();
		List<Occur> occurs = new ArrayList<Occur>();
		//判断是否使用分词，如果分词就先分词，再搜索
		if (isAnalyzer) {
			TokenStream tokenStream = analyzer.tokenStream("", new StringReader(key));
			List<String> analyzerKeys = new ArrayList<String>();
			tokenStream.reset(); 
			while(tokenStream.incrementToken()){
				CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
				analyzerKeys.add(term.toString());
			}
			for(int i = 0 ; i < fields.length ; i++){
				for(String analyzerKey : analyzerKeys){
					querys.add(getStartQuery(analyzerKey, fields[i]));
					occurs.add(occur);
				}
			}
			tokenStream.end();
			tokenStream.close();
		}else{
			for(String field : fields){
				querys.add(getStartQuery(key, field));
				occurs.add(occur);
			}
		}
		return getBooleanQuery(querys, occurs);
	}
	
	/**
	 * key开头的查询字符串，和多个域匹配，每个域之间的关系Occur.SHOULD
	 * @param key
	 * @param fields
	 * @param isAnalyzer 是否支持分词
	 * @return
	 * @throws IOException 
	 */
	public Query getStartQuery(String key,String[] fields,boolean isAnalyzer) throws IOException{
		return getStartQuery(key, fields, Occur.SHOULD, isAnalyzer);
	}
	
	/**
	 * 自定义每个次元之间的最大距离
	 * @param key
	 * @param field
	 * @param slop
	 * @return
	 */
	public Query getPhraseQuery(String key, String field, int slop){
		if(key == null || key.length() < 1){
			return null;
		}
		StringReader reader = new StringReader(key);
		//在lucene5.x中，PhraseQuery的实例化变成了PhraseQuery.Builder
		//PhraseQuery query = new PhraseQuery();
		PhraseQuery.Builder query = new PhraseQuery.Builder();
		query.setSlop(slop);
		try{
			TokenStream tokenStream = this.analyzer.tokenStream(field, reader);
			tokenStream.reset();
			CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
			while(tokenStream.incrementToken()){
				query.add(new Term(field,term.toString()));
			}
			tokenStream.end();
			tokenStream.close();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		} finally {
			reader.close();
		}
		//return query;
		return query.build();
	}
	
	/**
	 * 自定义每个次元之间的最大距离，查询多个域，每个域之间的关系由occur决定
	 * @param key
	 * @param fields
	 * @param slop
	 * @param occur
	 * @return
	 */
	public Query getPhraseQuery(String key,String[] fields, int slop, Occur occur){
		if(key == null || key.length() < 1){
			return null;
		}
		List<Query> querys = new ArrayList<Query>();
		List<Occur> occurs = new ArrayList<Occur>();
		for(String field : fields){
			querys.add(getPhraseQuery(key, field, slop));
			occurs.add(occur);
		}
		return getBooleanQuery(querys, occurs);
	}
	
	/**
	 * 自定义每个次元之间的最大距离，查询多个域，每个域之间的关系为Occur.SHOULD
	 * @param key
	 * @param fields
	 * @param slop
	 * @return
	 */
	public Query getPhraseQuery(String key, String[] fields,int slop){
		return getPhraseQuery(key, fields, slop, Occur.SHOULD);
	}

	/**
	 * 通配符搜索，例如:getWildcardQuery("a*thor", "field") 
	 * @param key
	 * @param field
	 * @return
	 */
	public Query getWildcardQuery(String key,String field){
		if(key == null || key.length() < 1){
			return null;
		}
		return new WildcardQuery(new Term(field,key));
	}
	
	/**
	 * 通配符搜索，域之间的关系为occur
	 * @param key
	 * @param fields
	 * @param occer
	 * @return
	 */
	public Query getWidcardQuery(String key,String[] fields,Occur occer){
		if(key == null || key.length() < 1){
			return null;
		}
		List<Query> querys = new ArrayList<Query>();
		List<Occur> occurs = new ArrayList<Occur>();
		for(String field : fields){
			querys.add(getWildcardQuery(key, field));
			occurs.add(occer);
		}
		return getBooleanQuery(querys, occurs);
	}
	
	/**
	 * 通配符搜索，域之间的关系为Occur.SHOULD
	 * @param key
	 * @param fields
	 * @return
	 */
	public Query getWildcardQuery(String key, String[] fields){
		return getWidcardQuery(key, fields, Occur.SHOULD);
	}
	
	/**
	 * 范围搜索
	 * @param keyStart
	 * @param keyEnd
	 * @param field
	 * @param includeStart
	 * @param includeEnd
	 * @return
	 */
	public Query getRangeQuery(String keyStart,String keyEnd,String field, boolean includeStart,boolean includeEnd){
		return TermRangeQuery.newStringRange(field, keyStart, keyEnd, includeStart, includeEnd);
	}
	
	/**
	 * 范围搜索
	 * @param min
	 * @param max
	 * @param field
	 * @param includeMin
	 * @param includeMax
	 * @return
	 */
	public Query getRangeQuery(int min,int max,String field,boolean includeMin,boolean includeMax){
		return NumericRangeQuery.newIntRange(field, min, max, includeMin, includeMax);
	}
	
	/**
	 * 范围搜索
	 * @param min
	 * @param max
	 * @param field
	 * @param includeMin
	 * @param includeMax
	 * @return
	 */
	public Query getRangeQuery(float min,float max,String field,boolean includeMin,boolean includeMax){
		return NumericRangeQuery.newFloatRange(field, min, max, includeMin, includeMax);
	}
	
	/**
	 * 范围搜索
	 * @param min
	 * @param max
	 * @param field
	 * @param includeMin
	 * @param includeMax
	 * @return
	 */
	public Query getRangeQuery(double min,double max,String field,boolean includeMin,boolean includeMax){
		return NumericRangeQuery.newDoubleRange(field, min, max, includeMin, includeMax);
	}
	
}
