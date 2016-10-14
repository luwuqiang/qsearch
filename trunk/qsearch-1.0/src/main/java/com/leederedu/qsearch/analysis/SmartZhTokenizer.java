package com.leederedu.qsearch.analysis;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import org.apache.lucene.util.AttributeFactory;

import com.leederedu.qsearch.analysis.core.SmartZhSegmenter;
import com.leederedu.qsearch.analysis.core.WordElement;

/**
 * SmartZh分词器 Lucene Tokenizer适配器类<br/>
 * 兼容lucene 4.x-6.x
 * @author TaneRoom
 * @since 2016年9月13日 上午9:37:59
 */
public final class SmartZhTokenizer extends Tokenizer {
	
	//SmartZh分词器实现
	private SmartZhSegmenter _SmartZhImplement;
	
	//词元文本属性
	private final CharTermAttribute termAtt;
	//词元位移属性
	private final OffsetAttribute offsetAtt;
	//词元分类属性（该属性分类参考cn.taneroom.lucene.analysis.core.WordType中的分类常量）
	private final TypeAttribute typeAtt;
	//记录最后一个词元的结束位置
	private int endPosition;
	
	/**
	 * Lucene 4.x Tokenizer适配器类构造函数
	 * @param in
	 */
	public SmartZhTokenizer(Reader in){
	    offsetAtt = addAttribute(OffsetAttribute.class);
	    termAtt = addAttribute(CharTermAttribute.class);
	    typeAtt = addAttribute(TypeAttribute.class);
		_SmartZhImplement = new SmartZhSegmenter(input);
	}

	/**
	 * Lucene 5.x Tokenizer适配器类构造函数
	 * @param in
	 */
	public SmartZhTokenizer(AttributeFactory in){
		super(in);
		offsetAtt = addAttribute(OffsetAttribute.class);
		termAtt = addAttribute(CharTermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
		_SmartZhImplement = new SmartZhSegmenter(input);
	}

	public SmartZhTokenizer(){
		offsetAtt = addAttribute(OffsetAttribute.class);
		termAtt = addAttribute(CharTermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
		_SmartZhImplement = new SmartZhSegmenter(input);
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public boolean incrementToken() throws IOException {
		//清除所有的词元属性
		clearAttributes();
		WordElement nextWordElement = _SmartZhImplement.next();
		if(nextWordElement != null){
			//将Lexeme转成Attributes
			//设置词元文本
			termAtt.append(nextWordElement.getWordText());
			//设置词元长度
			termAtt.setLength(nextWordElement.getLength());
			//设置词元位移
			offsetAtt.setOffset(nextWordElement.getBeginPosition(), nextWordElement.getEndPosition());
			//记录分词的最后位置
			endPosition = nextWordElement.getEndPosition();
			//记录词元分类
			typeAtt.setType(nextWordElement.getWordTypeString());			
			//返会true告知还有下个词元
			return true;
		}
		//返会false告知词元输出完毕
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.analysis.Tokenizer#reset(java.io.Reader)
	 */
	@Override
	public void reset() throws IOException {
		super.reset();
		_SmartZhImplement.reset(input);
	}	
	
	@Override
	public final void end() {
	    // set final offset
		int finalOffset = correctOffset(this.endPosition);
		offsetAtt.setOffset(finalOffset, finalOffset);
	}
}
