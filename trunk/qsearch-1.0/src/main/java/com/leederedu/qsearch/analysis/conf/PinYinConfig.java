package com.leederedu.qsearch.analysis.conf;

/**
 * 拼音分词的配置信息<br/>
 * 写在这，可以方便spring配置
 * @author TaneRoom
 * @since 2016年9月20日 上午10:00:32
 */
public class PinYinConfig {

	// 词元的默认最小长度
	private int defaultMinTermLength = 1;
	// 默认NGram的最短长度
	private int defaultMinGram = 1;
	// 默认NGram的最长长度
	private int defaultMaxGram = 5;
	// 默认对中文进行NGram
	private boolean defaultNgramChinese = true;
	// 对于多音字会有多个拼音,firstChar为true即表示只取第一个,否则会取多个拼音
	private boolean defaultFirstChar = false;
	// 默认输出中文
	private boolean defaultOutChinese = true;

	public int getDefaultMinTermLength() {
		return defaultMinTermLength;
	}

	public void setDefaultMinTermLength(int defaultMinTermLength) {
		this.defaultMinTermLength = defaultMinTermLength;
	}

	public int getDefaultMinGram() {
		return defaultMinGram;
	}

	public void setDefaultMinGram(int defaultMinGram) {
		this.defaultMinGram = defaultMinGram;
	}

	public int getDefaultMaxGram() {
		return defaultMaxGram;
	}

	public void setDefaultMaxGram(int defaultMaxGram) {
		this.defaultMaxGram = defaultMaxGram;
	}

	public boolean isDefaultNgramChinese() {
		return defaultNgramChinese;
	}

	public void setDefaultNgramChinese(boolean defaultNgramChinese) {
		this.defaultNgramChinese = defaultNgramChinese;
	}

	public boolean isDefaultFirstChar() {
		return defaultFirstChar;
	}

	public void setDefaultFirstChar(boolean defaultFirstChar) {
		this.defaultFirstChar = defaultFirstChar;
	}

	public boolean isDefaultOutChinese() {
		return defaultOutChinese;
	}

	public void setDefaultOutChinese(boolean defaultOutChinese) {
		this.defaultOutChinese = defaultOutChinese;
	}

}
