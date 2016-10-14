package com.leederedu.qsearch.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

import com.leederedu.qsearch.core.bean.SearchResultBean;
import com.leederedu.qsearch.core.manager.IndexManager;

/**
 * 实时索引，实现查询操作
 * @author TaneRoom
 * @since 2016年8月12日 下午2:58:18
 */
public class NRTSearch {

	private IndexManager indexManager;
	
	public NRTSearch(String indexName){
		indexManager = IndexManager.getIndexManager(indexName);
	}
	
	/**
	 * 获取索引中的记录条数
	 * @return 返回记录条数
	 */
	public int getIndexNum(){
		return indexManager.getIndexNum();
	}
	
	/**
	 * 查询结果
	 * @param query 查询字符串
	 * @param start 起始位置
	 * @param end 结束位置
	 * @return 返回索引对应的搜索结果集
	 */
	public SearchResultBean search(Query query, int start, int end){
		start = start < 0 ? 0 : start;
		end = end < 0 ? 0 : end;
		
		if(start >= end || indexManager == null || query == null){
			return null;
		}
		SearchResultBean result = new SearchResultBean();
		List<Document> datas = new ArrayList<Document>();
		result.setDatas(datas);
		IndexSearcher indexSearch = indexManager.getIndexSearcher();
		try{
			TopDocs docs = indexSearch.search(query, end);
			result.setCount(docs.totalHits);
			end = end > docs.totalHits ? docs.totalHits : end;
			for(int i = start ; i < end ; i++){
				datas.add(indexSearch.doc(docs.scoreDocs[i].doc));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			indexManager.release(indexSearch);
		}
		return result;
	}
	
	/**
	 * 查询结果
	 * @param query 查询字符串
	 * @param start 起始位置
	 * @param end 结束位置
	 * @param sort 排序
	 * @return 返回索引对应的搜索结果集
	 */
	public SearchResultBean search(Query query, int start, int end, Sort sort){
		start = start < 0 ? 0 : start;
		end = end < 0 ? 0 : end;
		if(start >= end || indexManager == null || query == null){
			return null;
		}
		SearchResultBean result = new SearchResultBean();
		List<Document> datas = new ArrayList<Document>();
		result.setDatas(datas);
		IndexSearcher indexSearcher = indexManager.getIndexSearcher();
		try{
			TopDocs docs = indexSearcher.search(query, end, sort);
			result.setCount(docs.totalHits);
			end = end > docs.totalHits ? docs.totalHits : end;
			for(int i = start ; i < end ; i++){
				datas.add(indexSearcher.doc(docs.scoreDocs[i].doc));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			indexManager.release(indexSearcher);
		}
		return result;
	}
	
	/**
	 * 按序号检索
	 * @param start 起始位置
	 * @param count 取多少条
	 * @return 返回索引对应的搜索结果集
	 */
	public SearchResultBean search(int start, int count){
		start = start < 0 ? 0 : start;
		count = count < 0 ? 0 : count;
		if(indexManager == null){
			return null;
		}
		IndexSearcher indexSearcher = indexManager.getIndexSearcher();
		SearchResultBean result = new SearchResultBean();
		List<Document> datas = new ArrayList<Document>();
		result.setDatas(datas);
		try{
			for(int i = 0 ; i < count ; i++){
				//这里(start + i) % getIndexNum()是因为担心start>getIndexNum()，所以对它进行取余
				datas.add(indexSearcher.doc((start + i) % getIndexNum()));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			indexManager.release(indexSearcher);
		}
		return result;
	}
	
}
