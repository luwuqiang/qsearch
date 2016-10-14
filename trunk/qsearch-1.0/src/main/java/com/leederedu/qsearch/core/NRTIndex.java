package com.leederedu.qsearch.core;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.Term;

import com.leederedu.qsearch.utils.Constants;

public class NRTIndex extends AbstractIndex {

	public NRTIndex(String indexName) {
		super(indexName);
	}
	
	/**
	 * 添加文档到索引
	 * @param params 需要添加的信息集合
	 * @return 返回是否添加成功
	 */
	public boolean addDocuments(List<FieldParam> params){
		try {
			indexWriter.addDocument(addToDocs(params));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 添加文档到索引
	 * @param param 需要添加的信息
	 * @return 返回是否添加成功
	 */
	public boolean addDocument(FieldParam param){
		try {
			indexWriter.addDocument(addToDoc(param));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 按照索引ID从索引中删除Document
	 * @param field_id 需要删除的索引ID
	 * @return 返回是否删除成功
	 */
	public boolean deleteDocument(String field_id){
		try {
			Term term = new Term(Constants.FIELD_NAME.ID, field_id);
			indexWriter.deleteDocuments(term);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 按照索引ID修改索引中的document
	 * @param params 修改后的信息集合
	 * @param field_id 需要修改的索引ID
	 * @return 返回是否修改成功
	 */
	public boolean updateDocument(List<FieldParam> params,String field_id){
		try {
			Document doc = addToDocs(params);
			Term term = new Term(Constants.FIELD_NAME.ID, field_id);
			indexWriter.updateDocument(term, doc);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 获取一个全新的Document
	 * @return 返回一个新建的Document
	 */
	private Document getDocument() {
		return new Document();
	}
	
	/**
	 * 批量向Document中添加Field
	 * @param params Field参数集合
	 * @return 返回添加了Field的Document
	 */
	private Document addToDocs(List<FieldParam> params) {
		Document doc = null;
		if (params != null) {
			doc = new Document();
			for (FieldParam param : params) {
				doc.add(addToField(param));
			}
		}
		return doc;
	}
	
	/**
	 * 向Document中添加Field
	 * @param param Field参数
	 * @return 返回添加了Field的Document
	 */
	private Document addToDoc(FieldParam param) {
		Document doc = getDocument();
		doc.add(addToField(param));
		return doc;
	}
	
	/**
	 * 向Field中添加值
	 * @param param 参数
	 * @return 返回添加了值的Field
	 */
	private Field addToField(FieldParam param) {
		//新建Document并往Document中添加Field
		Field field = null;
		//新建Field并往Field中添加数据
		FieldName field_n = param.getField();
		String field_name = param.getFieldName();
		Store field_store = param.getStore();
			
		switch (field_n) {
		case STRINGFIELD:
			field = new StringField(field_name, param.fieldValToString(), field_store);
			break;
		case TEXTFIELD:
			field = new TextField(field_name, param.fieldValToString(), field_store);
			break;
		case INTFIELD:
			field = new IntField(field_name, param.fieldValToInt(), field_store);
			break;
		case LONGFIELD:
			field = new LongField(field_name, param.fieldValToLong(), field_store);
			break;
		case FLOATFIELD:
			field = new FloatField(field_name, param.fieldValToFload(), field_store);
			break;
		case DOUBLEFIELD:
			field = new DoubleField(field_name, param.fieldValToDouble(), field_store);
			break;
		default:
			field = new TextField(field_name, param.fieldValToString(), field_store);
			break;
		}
		return field;
	}

}
