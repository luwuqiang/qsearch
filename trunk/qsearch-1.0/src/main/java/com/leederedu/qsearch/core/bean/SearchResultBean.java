package com.leederedu.qsearch.core.bean;

import java.util.List;

import org.apache.lucene.document.Document;

/**
 * 索引搜索结果数据结构
 * @author TaneRoom
 * @since 2016年8月12日 上午11:46:48
 */
public class SearchResultBean {

	private int count;//符合条件的记录条数
	private List<Document> datas;//查询到的记录数组

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Document> getDatas() {
		return datas;
	}

	public void setDatas(List<Document> datas) {
		this.datas = datas;
		if (datas != null) {
			this.count = datas.size();
		}
	}

	@Override
	public String toString() {
		return "[count]:"+count+"[datas]:"+datas.toString();
	}

}
