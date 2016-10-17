package com.leederedu.qsearch.core;

import java.util.LinkedList;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;

/**
 * Document组装抽象类
 * @author TaneRoom
 * @since 2016年9月26日 下午2:28:51
 */
public abstract class AbstractFieldDoc {

	private LinkedList<FieldArg> fieldArgs;
	
	public LinkedList<FieldArg> getFieldArgs() {
		return fieldArgs;
	}

	public void setFieldArgs(LinkedList<FieldArg> fieldArgs) {
		this.fieldArgs = fieldArgs;
	}

	public AbstractFieldDoc() {
		super();
		this.fieldArgs = new LinkedList<FieldArg>();
	}

	public AbstractFieldDoc(LinkedList<FieldArg> fieldArgs) {
		super();
		this.fieldArgs = fieldArgs;
	}

	public AbstractFieldDoc add(FieldArg fieldArg) {
		fieldArgs.add(fieldArg);
		return this;
	}
	
	/**
	 * 获取一个全新的Document
	 * @return 新建一个Document并返回
	 */
	protected Document getDocument() {
		return new Document();
	}
	
	/**
	 * 根据传入参数组装Document<br/>
	 * 该方法将根据传入的类型进行对应的Field包装，同时，如果要扩展该类的话，首先要继承AbstractFieldDoc类<br/>
	 * 然后重写getDoc方法，匹配一下FieldArg的Key，然后进行相应Field处理<br/>
	 * @return 返回一个组装好的Document
	 */
	public abstract Document getDoc();
	
	/**
	 * 向Field中添加值
	 * @param param 参数
	 * @return 返回添加了值的Field
	 */
	protected Field addToField(FieldParam param) {
		//新建Document并往Document中添加Field
		Field field = null;
		//新建Field并往Field中添加数据
		FieldName field_n = param.getField();
		String field_name = param.getFieldName();
		Object field_value = param.getFieldValue();
		Store field_store = param.getStore();
			
		switch (field_n) {
		case STRINGFIELD:
			field = new StringField(field_name, field_value.toString(), field_store);
			break;
		case TEXTFIELD:
			field = new TextField(field_name, field_value.toString(), field_store);
			break;
		case INTFIELD:
			field = new IntField(field_name, Integer.parseInt(field_value.toString()), field_store);
			break;
		case LONGFIELD:
			field = new LongField(field_name, Long.parseLong(field_value.toString()), field_store);
			break;
		case FLOATFIELD:
			field = new FloatField(field_name, Float.parseFloat(field_value.toString()), field_store);
			break;
		case DOUBLEFIELD:
			field = new DoubleField(field_name, Double.parseDouble(field_value.toString()), field_store);
			break;
		default:
			field = new TextField(field_name, field_value.toString(), field_store);
			break;
		}
		return field;
	}
	
	/**
	 * 向Field中添加值并设置权值
	 * @param param 参数
	 * @param boost 每次递增的权值
	 * @return 返回添加了值的Field
	 */
	protected Field addToField(FieldParam param, float boost) {
		//新建Document并往Document中添加Field
		Field field = null;
		//新建Field并往Field中添加数据
		FieldName field_n = param.getField();
		String field_name = param.getFieldName();
		Object field_value = param.getFieldValue();
		Store field_store = param.getStore();
			
		switch (field_n) {
		case STRINGFIELD:
			field = new StringField(field_name, field_value.toString(), field_store);
			break;
		case TEXTFIELD:
			field = new TextField(field_name, field_value.toString(), field_store);
			break;
		case INTFIELD:
			field = new IntField(field_name, Integer.parseInt(field_value.toString()), field_store);
			break;
		case LONGFIELD:
			field = new LongField(field_name, Long.parseLong(field_value.toString()), field_store);
			break;
		case FLOATFIELD:
			field = new FloatField(field_name, Float.parseFloat(field_value.toString()), field_store);
			break;
		case DOUBLEFIELD:
			field = new DoubleField(field_name, Double.parseDouble(field_value.toString()), field_store);
			break;
		default:
			field = new TextField(field_name, field_value.toString(), field_store);
			break;
		}
		// 文档加权操作
		field.setBoost(boost+field.boost());
		return field;
	}
	
}
