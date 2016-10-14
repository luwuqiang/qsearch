package com.leederedu.qsearch.analysis.pinyin;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;

import com.leederedu.qsearch.analysis.SmartZhTokenizer;
import com.leederedu.qsearch.analysis.conf.DefaultPinYinConfig;

/**
 * 自定义拼音分词器
 * @author TaneRoom
 * @since 2016年9月19日 下午5:30:29
 */
public class PinyinAnalyzer extends Analyzer {
	private int minGram;
	private int maxGram;

	public PinyinAnalyzer() {
		super();
		this.maxGram = DefaultPinYinConfig.getPinYinconfig().getDefaultMaxGram();
		this.minGram = DefaultPinYinConfig.getPinYinconfig().getDefaultMinGram();
	}

	public PinyinAnalyzer(int maxGram) {
		super();
		this.maxGram = maxGram;
		this.minGram = DefaultPinYinConfig.getPinYinconfig().getDefaultMinGram();
	}

	public PinyinAnalyzer(int minGram, int maxGram) {
		super();
		this.minGram = minGram;
		this.maxGram = maxGram;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Reader reader = new BufferedReader(new StringReader(fieldName));
		Tokenizer tokenizer = new SmartZhTokenizer(reader);
		// 转拼音
		TokenStream tokenStream = new PinyinTokenFilter(tokenizer,
				DefaultPinYinConfig.getPinYinconfig().isDefaultFirstChar(),
				DefaultPinYinConfig.getPinYinconfig().getDefaultMinTermLength());
		// 对拼音进行NGram处理
		tokenStream = new PinyinNGramTokenFilter(tokenStream, this.minGram, this.maxGram);
		tokenStream = new LowerCaseFilter(tokenStream);
		tokenStream = new StopFilter(tokenStream, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		return new Analyzer.TokenStreamComponents(tokenizer, tokenStream);
	}
}
