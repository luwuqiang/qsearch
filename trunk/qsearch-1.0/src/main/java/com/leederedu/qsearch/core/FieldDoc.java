package com.leederedu.qsearch.core;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;

import com.leederedu.qsearch.utils.Constants;
import com.leederedu.qsearch.utils.Pinyin4jUtils;

/**
 * AbstractFieldDoc默认实现
 * @author TaneRoom
 * @since 2016年9月26日 下午2:24:38
 */
public class FieldDoc extends AbstractFieldDoc {

	@Override
	public Document getDoc() {
		Document doc = getDocument();
		for (FieldArg fieldArg : getFieldArgs()) {
			if (Constants.FIELD_NAME.ID.equalsIgnoreCase(fieldArg.getKey())) {
				FieldParam param = new FieldParam(FieldName.STRINGFIELD, Constants.FIELD_NAME.ID, fieldArg.getValue(), Store.YES);
				doc.add(addToField(param));
			} else if (Constants.FIELD_NAME.TITLE.equalsIgnoreCase(fieldArg.getKey())) {
				FieldParam param1 = new FieldParam(FieldName.STRINGFIELD, Constants.FIELD_NAME.TITLE, fieldArg.getValue(), Store.YES);
				FieldParam param2 = new FieldParam(FieldName.TEXTFIELD, Constants.FIELD_NAME.TITLE_ANALYZER, fieldArg.getValue(), Store.NO);
				FieldParam param3 = new FieldParam(FieldName.STRINGFIELD, Constants.FIELD_NAME.TITLE_PINYIN, Pinyin4jUtils.getPinyinOneToLowerCase(fieldArg.getValue().toString()), Store.NO);
				FieldParam param4 = new FieldParam(FieldName.STRINGFIELD, Constants.FIELD_NAME.TITLE_PINYIN_HEAD, Pinyin4jUtils.getOneJianPinLowerCase(fieldArg.getValue().toString()), Store.NO);
				doc.add(addToField(param1));
				doc.add(addToField(param2));
				doc.add(addToField(param3));
				doc.add(addToField(param4));
			} else if (Constants.FIELD_NAME.BOOST.equalsIgnoreCase(fieldArg.getKey())) {
				FieldParam param = new FieldParam(FieldName.FLOATFIELD, Constants.FIELD_NAME.BOOST, fieldArg.getValue(), Store.YES);
				doc.add(addToField(param));
			}
		}
		return doc;
	}
	
	/**
	 * 根据ID以及传入参数组装Document<br/>
	 * 该方法将根据传入的类型进行对应的Field包装，所以传入参数的时候，一定要注意数据类型<br/>
	 * 比如ID是int类型的，那么必须用它的包装类型Integer传进来<br/>
	 * @param FieldId ID字段
	 * @return 返回一个组装好的Document
	 */
	public Document getDoc(long FieldId) {
		Document doc = getDocument();
		FieldParam fp = new FieldParam(FieldName.LONGFIELD, Constants.FIELD_NAME.ID, FieldId, Store.YES);
		doc.add(addToField(fp));
		for (FieldArg fieldArg : getFieldArgs()) {
			if (fieldArg.getValue() instanceof Integer) {
				FieldParam param = new FieldParam(FieldName.INTFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				doc.add(addToField(param));
			} else if (fieldArg.getValue() instanceof String) {
				FieldParam param1 = new FieldParam(FieldName.STRINGFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				FieldParam param2 = new FieldParam(FieldName.TEXTFIELD, fieldArg.getKey()+"Analyzer", fieldArg.getValue(), Store.NO);
				FieldParam param3 = new FieldParam(FieldName.STRINGFIELD, fieldArg.getKey()+"Pinyin", Pinyin4jUtils.getPinyinOneToLowerCase(fieldArg.getValue().toString()), Store.NO);
				FieldParam param4 = new FieldParam(FieldName.STRINGFIELD, fieldArg.getKey()+"PinyinHead", Pinyin4jUtils.getOneJianPinLowerCase(fieldArg.getValue().toString()), Store.NO);
				doc.add(addToField(param1));
				doc.add(addToField(param2));
				doc.add(addToField(param3));
				doc.add(addToField(param4));
			} else if (fieldArg.getValue() instanceof Long) {
				FieldParam param = new FieldParam(FieldName.LONGFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				doc.add(addToField(param));
			} else if (fieldArg.getValue() instanceof Float) {
				FieldParam param = new FieldParam(FieldName.FLOATFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				doc.add(addToField(param));
			} else if (fieldArg.getValue() instanceof Double) {
				FieldParam param = new FieldParam(FieldName.DOUBLEFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				doc.add(addToField(param));
			} 
		}
		return doc;
	}
	
	/**
	 * 根据字段名加权
	 * @param fieldName 字段名
	 * @param boost 递增的权值
	 * @return
	 */
	public Document getDoc(String fieldName, float boost) {
		Document doc = getDocument();
		for (FieldArg fieldArg : getFieldArgs()) {
			
			if (Constants.FIELD_NAME.ID.equals(fieldArg.getKey())) {
				FieldParam param = new FieldParam(FieldName.STRINGFIELD, Constants.FIELD_NAME.ID, fieldArg.getValue(), Store.YES);
				if (fieldName.equals(fieldArg.getKey())) {
					doc.add(addToField(param,boost));
				} else {
					doc.add(addToField(param));
				}
			} else if (Constants.FIELD_NAME.TITLE.equals(fieldArg.getKey())) {
				FieldParam param1 = new FieldParam(FieldName.STRINGFIELD, Constants.FIELD_NAME.TITLE, fieldArg.getValue(), Store.YES);
				FieldParam param2 = new FieldParam(FieldName.TEXTFIELD, Constants.FIELD_NAME.TITLE_ANALYZER, fieldArg.getValue(), Store.NO);
				FieldParam param3 = new FieldParam(FieldName.STRINGFIELD, Constants.FIELD_NAME.TITLE_PINYIN, Pinyin4jUtils.getPinyinOneToLowerCase(fieldArg.getValue().toString()), Store.NO);
				FieldParam param4 = new FieldParam(FieldName.STRINGFIELD, Constants.FIELD_NAME.TITLE_PINYIN_HEAD, Pinyin4jUtils.getOneJianPinLowerCase(fieldArg.getValue().toString()), Store.NO);
				if (fieldName.equals(fieldArg.getKey())) {
					doc.add(addToField(param1,boost));
					doc.add(addToField(param2,boost));
					doc.add(addToField(param3,boost));
					doc.add(addToField(param4,boost));
				} else {
					doc.add(addToField(param1));
					doc.add(addToField(param2));
					doc.add(addToField(param3));
					doc.add(addToField(param4));
				}
			} else if (Constants.FIELD_NAME.BOOST.equalsIgnoreCase(fieldArg.getKey())) {
				FieldParam param = new FieldParam(FieldName.FLOATFIELD, Constants.FIELD_NAME.BOOST, fieldArg.getValue(), Store.YES);
				if (fieldName.equals(fieldArg.getKey())) {
					doc.add(addToField(param,boost));
				} else {
					doc.add(addToField(param));
				}
			}
		}
		return doc;
	}
	
	/**
	 * 根据字段名加权
	 * @param fieldName 字段名
	 * @param boost 递增的权值
	 * @param FieldId 字段ID
	 * @return
	 */
	public Document getDoc(String fieldName, float boost, long FieldId) {
		Document doc = getDocument();
		FieldParam fp = new FieldParam(FieldName.LONGFIELD, Constants.FIELD_NAME.ID, FieldId, Store.YES);
		doc.add(addToField(fp));
		for (FieldArg fieldArg : getFieldArgs()) {
			if (fieldArg.getValue() instanceof Integer) {
				FieldParam param = new FieldParam(FieldName.INTFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				if (fieldName.equals(fieldArg.getKey())) {
					doc.add(addToField(param,boost));
				} else {
					doc.add(addToField(param));
				}
			} else if (fieldArg.getValue() instanceof String) {
				FieldParam param1 = new FieldParam(FieldName.STRINGFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				FieldParam param2 = new FieldParam(FieldName.TEXTFIELD, fieldArg.getKey()+"Analyzer", fieldArg.getValue(), Store.NO);
				FieldParam param3 = new FieldParam(FieldName.STRINGFIELD, fieldArg.getKey()+"Pinyin", Pinyin4jUtils.getPinyinOneToLowerCase(fieldArg.getValue().toString()), Store.NO);
				FieldParam param4 = new FieldParam(FieldName.STRINGFIELD, fieldArg.getKey()+"PinyinHead", Pinyin4jUtils.getOneJianPinLowerCase(fieldArg.getValue().toString()), Store.NO);
				if (fieldName.equals(fieldArg.getKey())) {
					doc.add(addToField(param1,boost));
					doc.add(addToField(param2,boost));
					doc.add(addToField(param3,boost));
					doc.add(addToField(param4,boost));
				} else {
					doc.add(addToField(param1));
					doc.add(addToField(param2));
					doc.add(addToField(param3));
					doc.add(addToField(param4));
				}
			} else if (fieldArg.getValue() instanceof Long) {
				FieldParam param = new FieldParam(FieldName.LONGFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				if (fieldName.equals(fieldArg.getKey())) {
					doc.add(addToField(param,boost));
				} else {
					doc.add(addToField(param));
				}
			} else if (fieldArg.getValue() instanceof Float) {
				FieldParam param = new FieldParam(FieldName.FLOATFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				if (fieldName.equals(fieldArg.getKey())) {
					doc.add(addToField(param,boost));
				} else {
					doc.add(addToField(param));
				}
			} else if (fieldArg.getValue() instanceof Double) {
				FieldParam param = new FieldParam(FieldName.DOUBLEFIELD, fieldArg.getKey(), fieldArg.getValue(), Store.YES);
				if (fieldName.equals(fieldArg.getKey())) {
					doc.add(addToField(param,boost));
				} else {
					doc.add(addToField(param));
				}
			} 
		}
		return doc;
	}
	
}
