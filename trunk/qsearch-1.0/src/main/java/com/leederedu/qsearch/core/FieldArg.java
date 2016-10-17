package com.leederedu.qsearch.core;

public class FieldArg {

	private String key;
	private Object value;

	public FieldArg(String key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	public FieldArg() {
		super();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
