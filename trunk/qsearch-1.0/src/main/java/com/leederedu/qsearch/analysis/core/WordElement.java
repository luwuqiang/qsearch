package com.leederedu.qsearch.analysis.core;

/**
 * 词元
 * @author TaneRoom
 * @since 2016年9月12日 下午5:21:03
 */
public class WordElement implements Comparable<WordElement> {

	// 词元的起始位移
	private int offset;
	// 词元的相对起始位置
	private int begin;
	// 词元的长度
	private int length;
	// 词元文本
	private String wordText;
	// 词元类型
	private int wordType;
	
	public WordElement(int offset, int begin, int length, int wordType) {
		super();
		this.offset = offset;
		this.begin = begin;
		if(length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
		this.wordType = wordType;
	}

	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}


	public int getBegin() {
		return begin;
	}


	public void setBegin(int begin) {
		this.begin = begin;
	}

	/**
	 * 获取词元的文本内容
	 * @return String
	 */
	public String getWordText() {
		if(wordText == null){
			return "";
		}
		return wordText;
	}


	public void setWordText(String wordText) {
		if(wordText == null){
			this.wordText = "";
			this.length = 0;
		}else{
			this.wordText = wordText;
			this.length = wordText.length();
		}
	}

	/**
	 * 获取词元类型
	 * @return int
	 */
	public int getWordType() {
		return wordType;
	}
	
	/**
	 * 获取词元类型标示字符串
	 * @return String
	 */
	public String getWordTypeString(){
		switch(wordType) {

		case WordType.TYPE_ENGLISH :
			return "ENGLISH";
			
		case WordType.TYPE_ARABIC :
			return "ARABIC";
			
		case WordType.TYPE_LETTER :
			return "LETTER";
			
		case WordType.TYPE_CNWORD : 
			return "CN_WORD";
			
		case WordType.TYPE_CNCHAR : 
			return "CN_CHAR";
			
		case WordType.TYPE_OTHER_CJK :
			return "OTHER_CJK";
			
		case WordType.TYPE_COUNT :
			return "COUNT";
			
		case WordType.TYPE_CNUM :
			return "TYPE_CNUM";
			
		case WordType.TYPE_CQUAN:	
			return "TYPE_CQUAN";
			
		default :
			return "UNKONW";
		}

	}

	public void setWordType(int wordType) {
		this.wordType = wordType;
	}
	
	/**
	 * 获取词元在文本中的起始位置
	 * @return int
	 */
	public int getBeginPosition(){
		return offset + begin;
	}

	/**
	 * 获取词元在文本中的结束位置
	 * @return int
	 */
	public int getEndPosition(){
		return offset + begin + length;
	}
	
	/**
	 * 获取词元的字符长度
	 * @return int
	 */
	public int getLength(){
		return this.length;
	}	
	
	public void setLength(int length) {
		if(this.length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
	}
	
	/**
	 * 合并两个相邻的词元
	 * @param l
	 * @param wordType
	 * @return boolean 词元是否成功合并
	 */
	public boolean append(WordElement l , int wordType){
		if(l != null && this.getEndPosition() == l.getBeginPosition()){
			this.length += l.getLength();
			this.wordType = wordType;
			return true;
		}else {
			return false;
		}
	}
	
	/**
     * 词元在排序集合中的比较算法
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
	@Override
	public int compareTo(WordElement other) {
		//起始位置优先
        if(this.begin < other.getBegin()){
            return -1;
        }else if(this.begin == other.getBegin()){
        	//词元长度优先
        	if(this.length > other.getLength()){
        		return -1;
        	}else if(this.length == other.getLength()){
        		return 0;
        	}else {
        		//this.length < other.getLength()
        		return 1;
        	}
        }else{
        	//this.begin > other.getBegin()
        	return 1;
        }
	}

	/**
     * 判断词元相等算法
     * 起始位置偏移、起始位置、终止位置相同
     * @see java.lang.Object#equals(Object o)
     */
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		
		if(this == obj){
			return true;
		}
		
		if(obj instanceof WordElement){
			WordElement other = (WordElement)obj;
			if(this.offset == other.getOffset()
					&& this.begin == other.getBegin()
					&& this.length == other.getLength()){
				return true;			
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}

	@Override
	public int hashCode() {
		int absBegin = getBeginPosition();
    	int absEnd = getEndPosition();
    	return  (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
	}

	@Override
	public String toString() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(this.getBeginPosition()).append("-").append(this.getEndPosition());
		strbuf.append(" : ").append(this.wordText).append(" : \t");
		strbuf.append(this.getWordTypeString());
		return strbuf.toString();
	}

	
	
}
