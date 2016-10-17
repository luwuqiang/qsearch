package com.leederedu.qsearch.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

public class SmartZhAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer _SmartZhTokenizer = new SmartZhTokenizer();
		return new TokenStreamComponents(_SmartZhTokenizer);
	}

}
