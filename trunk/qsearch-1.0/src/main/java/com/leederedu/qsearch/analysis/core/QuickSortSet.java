package com.leederedu.qsearch.analysis.core;

/**
 * 分词器专用的词元快速排序集合
 * @author TaneRoom
 * @since 2016年9月12日 下午5:20:38
 */
class QuickSortSet {
	//链表头
	private Cell head;
	//链表尾
	private Cell tail;
	//链表的实际大小
	private int size;
	
	QuickSortSet(){
		this.size = 0;
	}
	
	/**
	 * 向链表集合添加词元
	 * @param wordElement
	 */
	boolean addWordElement(WordElement wordElement){
		Cell newCell = new Cell(wordElement); 
		if(this.size == 0){
			this.head = newCell;
			this.tail = newCell;
			this.size++;
			return true;
			
		}else{
			if(this.tail.compareTo(newCell) == 0){//词元与尾部词元相同，不放入集合
				return false;
				
			}else if(this.tail.compareTo(newCell) < 0){//词元接入链表尾部
				this.tail.next = newCell;
				newCell.prev = this.tail;
				this.tail = newCell;
				this.size++;
				return true;
				
			}else if(this.head.compareTo(newCell) > 0){//词元接入链表头部
				this.head.prev = newCell;
				newCell.next = this.head;
				this.head = newCell;
				this.size++;
				return true;
				
			}else{					
				//从尾部上逆
				Cell index = this.tail;
				while(index != null && index.compareTo(newCell) > 0){
					index = index.prev;
				}
				if(index.compareTo(newCell) == 0){//词元与集合中的词元重复，不放入集合
					return false;
					
				}else if(index.compareTo(newCell) < 0){//词元插入链表中的某个位置
					newCell.prev = index;
					newCell.next = index.next;
					index.next.prev = newCell;
					index.next = newCell;
					this.size++;
					return true;					
				}
			}
		}
		return false;
	}
	
	/**
	 * 返回链表头部元素
	 * @return
	 */
	WordElement peekFirst(){
		if(this.head != null){
			return this.head.wordElement;
		}
		return null;
	}
	
	/**
	 * 取出链表集合的第一个元素
	 * @return WordElement
	 */
	WordElement pollFirst(){
		if(this.size == 1){
			WordElement first = this.head.wordElement;
			this.head = null;
			this.tail = null;
			this.size--;
			return first;
		}else if(this.size > 1){
			WordElement first = this.head.wordElement;
			this.head = this.head.next;
			this.size --;
			return first;
		}else{
			return null;
		}
	}
	
	/**
	 * 返回链表尾部元素
	 * @return
	 */
	WordElement peekLast(){
		if(this.tail != null){
			return this.tail.wordElement;
		}
		return null;
	}
	
	/**
	 * 取出链表集合的最后一个元素
	 * @return WordElement
	 */
	WordElement pollLast(){
		if(this.size == 1){
			WordElement last = this.head.wordElement;
			this.head = null;
			this.tail = null;
			this.size--;
			return last;
			
		}else if(this.size > 1){
			WordElement last = this.tail.wordElement;
			this.tail = this.tail.prev;
			this.size--;
			return last;
			
		}else{
			return null;
		}
	}
	
	/**
	 * 返回集合大小
	 * @return
	 */
	int size(){
		return this.size;
	}
	
	/**
	 * 判断集合是否为空
	 * @return
	 */
	boolean isEmpty(){
		return this.size == 0;
	}
	
	/**
	 * 返回wordElement链的头部
	 * @return
	 */
	Cell getHead(){
		return this.head;
	}
	
	/**
	 * QuickSortSet集合单元
	 * @author TaneRoom
	 * @since 2016年9月12日 下午5:20:10
	 */
	class Cell implements Comparable<Cell>{
		private Cell prev;
		private Cell next;
		private WordElement wordElement;
		
		Cell(WordElement wordElement){
			if(wordElement == null){
				throw new IllegalArgumentException("wordElement must not be null");
			}
			this.wordElement = wordElement;
		}

		public int compareTo(Cell o) {
			return this.wordElement.compareTo(o.wordElement);
		}

		public Cell getPrev(){
			return this.prev;
		}
		
		public Cell getNext(){
			return this.next;
		}
		
		public WordElement getWordElement(){
			return this.wordElement;
		}
	}
}

