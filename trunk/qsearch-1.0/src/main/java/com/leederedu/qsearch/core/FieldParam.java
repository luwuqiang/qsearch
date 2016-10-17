package com.leederedu.qsearch.core;

import com.leederedu.qsearch.utils.NumberUtils;
import org.apache.lucene.document.Field.Store;

/**
 * 域Field参数
 * @author TaneRoom
 * @since 2016年9月23日 下午5:50:48
 */
public class FieldParam {
	
	/** 域方法名 */
	private FieldName field;
	/** 域名称 */
	private String fieldName;
	/** 域值 */
	private Object fieldValue;
	/** 是否存储 */
	private Store store;

	public FieldParam(FieldName stringfield, String fieldName, Object fieldValue, Store store) {
		super();
		this.field = stringfield;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.store = store;
	}

	public FieldParam() {
		super();
	}

	public FieldName getField() {
		return field;
	}

	public void setField(FieldName field) {
		this.field = field;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String fieldValToString(){
		if(this.fieldValue != null){
			return this.fieldValue.toString();
		}
		return "";
	}

	public int fieldValToInt(){
		return NumberUtils.toInt(this.fieldValue, 0);
	}

	public long fieldValToLong() {
		return NumberUtils.toLong(this.fieldValue, 0);
	}

	public float fieldValToFload(){
		return NumberUtils.toFloat(this.fieldValue, 0f);
	}

	public double fieldValToDouble(){
		return NumberUtils.toDouble(this.fieldValue, 0d);
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}