package com.leederedu.qsearch.core;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TrackingIndexWriter;
import org.apache.lucene.search.Query;

import com.leederedu.qsearch.utils.Constants;
import com.leederedu.qsearch.core.manager.IndexManager;

/**
 * 实时索引，实现增删改三种操作
 * @author TaneRoom
 * @since 2016年8月12日 下午2:23:44
 */
public abstract class AbstractIndex {

	protected TrackingIndexWriter indexWriter;
	protected String indexName;
	
	//直接使用IndexManager的indexWriter，将索引的修改操作委托给trackingIndexWriter实现
	public AbstractIndex(String indexName){
		this.indexName = indexName;
		indexWriter = IndexManager.getIndexManager(indexName).getTrackingIndexWriter();
	}
	
	/**
	 * 添加文档到索引
	 * @param doc 文档
	 * @return 返回是否添加成功
	 */
	public boolean addDocument(Document doc){
		try {
			indexWriter.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 按照query条件从索引中删除Document
	 * @param query query条件
	 * @return 返回是否删除成功
	 */
	public boolean deleteDocument(Query query){
		try {
			indexWriter.deleteDocuments(query);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 按照term条件从索引中删除Document
	 * @param term term条件
	 * @return 返回是否删除成功
	 */
	public boolean deleteDocument(Term term){
		try {
			indexWriter.deleteDocuments(term);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 清空索引
	 * @return 返回是否删除成功
	 */
	public boolean deleteAll(){
		try {
			indexWriter.deleteAll();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 按照FieldId修改索引中的document
	 * @param doc 文档
	 * @return 返回是否修改成功
	 */
	public boolean updateDocument(Document doc){
		try {
			Term term = new Term(Constants.FIELD_NAME.ID, doc.getField(Constants.FIELD_NAME.ID).stringValue());
			indexWriter.updateDocument(term, doc);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 按照term条件修改索引中的document
	 * @param term term条件
	 * @param doc 文档
	 * @return 返回是否修改成功
	 */
	public boolean updateDocument(Term term,Document doc){
		try {
			indexWriter.updateDocument(term, doc);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 合并索引
	 * @throws IOException
	 */
	public void commit() throws IOException{
		IndexManager.getIndexManager(indexName).getIndexWriter().commit();
	} 
	
}
