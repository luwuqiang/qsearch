package com.leederedu.qsearch.analysis.core;

/**
 * 词元链
 * 
 * @author TaneRoom
 * @since 2016年9月12日 下午5:27:05
 */
public class WordElementChain extends QuickSortSet implements Comparable<WordElementChain> {

	// 起始位置
	private int pathBegin;
	// 结束
	private int pathEnd;
	// 词元链的有效字符长度
	private int payloadLength;

	WordElementChain() {
		this.pathBegin = -1;
		this.pathEnd = -1;
		this.payloadLength = 0;
	}

	/**
	 * 向WordElementChain追加相交的WordElement
	 * @param we
	 * @return
	 */
	boolean addCrossWordElement(WordElement we) {
		if (this.isEmpty()) {
			this.addWordElement(we);
			this.pathBegin = we.getBegin();
			this.pathEnd = we.getBegin() + we.getLength();
			this.payloadLength += we.getLength();
			return true;

		} else if (this.checkCross(we)) {
			this.addWordElement(we);
			if (we.getBegin() + we.getLength() > this.pathEnd) {
				this.pathEnd = we.getBegin() + we.getLength();
			}
			this.payloadLength = this.pathEnd - this.pathBegin;
			return true;

		} else {
			return false;

		}
	}

	/**
	 * 向WordElementChain追加不相交的WordElement
	 * @param we
	 * @return
	 */
	boolean addNotCrossWordElement(WordElement we) {
		if (this.isEmpty()) {
			this.addWordElement(we);
			this.pathBegin = we.getBegin();
			this.pathEnd = we.getBegin() + we.getLength();
			this.payloadLength += we.getLength();
			return true;

		} else if (this.checkCross(we)) {
			return false;

		} else {
			this.addWordElement(we);
			this.payloadLength += we.getLength();
			WordElement head = this.peekFirst();
			this.pathBegin = head.getBegin();
			WordElement tail = this.peekLast();
			this.pathEnd = tail.getBegin() + tail.getLength();
			return true;

		}
	}

	/**
	 * 移除尾部的WordElement
	 * @return
	 */
	WordElement removeTail() {
		WordElement tail = this.pollLast();
		if (this.isEmpty()) {
			this.pathBegin = -1;
			this.pathEnd = -1;
			this.payloadLength = 0;
		} else {
			this.payloadLength -= tail.getLength();
			WordElement newTail = this.peekLast();
			this.pathEnd = newTail.getBegin() + newTail.getLength();
		}
		return tail;
	}

	/**
	 * 检测词元位置交叉（有歧义的切分）
	 * 
	 * @param we
	 * @return
	 */
	boolean checkCross(WordElement we) {
		return (we.getBegin() >= this.pathBegin && we.getBegin() < this.pathEnd)
				|| (this.pathBegin >= we.getBegin() && this.pathBegin < we.getBegin() + we.getLength());
	}

	int getPathBegin() {
		return pathBegin;
	}

	int getPathEnd() {
		return pathEnd;
	}

	/**
	 * 获取Path的有效词长
	 * 
	 * @return
	 */
	int getPayloadLength() {
		return this.payloadLength;
	}

	/**
	 * 获取WordElementChain的路径长度
	 * 
	 * @return
	 */
	int getPathLength() {
		return this.pathEnd - this.pathBegin;
	}

	/**
	 * X权重（词元长度积）
	 * @return
	 */
	int getXWeight() {
		int product = 1;
		Cell c = this.getHead();
		while (c != null && c.getWordElement() != null) {
			product *= c.getWordElement().getLength();
			c = c.getNext();
		}
		return product;
	}

	/**
	 * 词元位置权重
	 * @return
	 */
	int getPWeight() {
		int pWeight = 0;
		int p = 0;
		Cell c = this.getHead();
		while (c != null && c.getWordElement() != null) {
			p++;
			pWeight += p * c.getWordElement().getLength();
			c = c.getNext();
		}
		return pWeight;
	}

	WordElementChain copy() {
		WordElementChain theCopy = new WordElementChain();
		theCopy.pathBegin = this.pathBegin;
		theCopy.pathEnd = this.pathEnd;
		theCopy.payloadLength = this.payloadLength;
		Cell c = this.getHead();
		while (c != null && c.getWordElement() != null) {
			theCopy.addWordElement(c.getWordElement());
			c = c.getNext();
		}
		return theCopy;
	}

	@Override
	public int compareTo(WordElementChain wec) {
		// 比较有效文本长度
		if (this.payloadLength > wec.payloadLength) {
			return -1;
		} else if (this.payloadLength < wec.payloadLength) {
			return 1;
		} else {
			// 比较词元个数，越少越好
			if (this.size() < wec.size()) {
				return -1;
			} else if (this.size() > wec.size()) {
				return 1;
			} else {
				// 路径跨度越大越好
				if (this.getPathLength() > wec.getPathLength()) {
					return -1;
				} else if (this.getPathLength() < wec.getPathLength()) {
					return 1;
				} else {
					// 根据统计学结论，逆向切分概率高于正向切分，因此位置越靠后的优先
					if (this.pathEnd > wec.pathEnd) {
						return -1;
					} else if (pathEnd < wec.pathEnd) {
						return 1;
					} else {
						// 词长越平均越好
						if (this.getXWeight() > wec.getXWeight()) {
							return -1;
						} else if (this.getXWeight() < wec.getXWeight()) {
							return 1;
						} else {
							// 词元位置权重比较
							if (this.getPWeight() > wec.getPWeight()) {
								return -1;
							} else if (this.getPWeight() < wec.getPWeight()) {
								return 1;
							}

						}
					}
				}
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("pathBegin  : ").append(pathBegin).append("\r\n");
		sb.append("pathEnd  : ").append(pathEnd).append("\r\n");
		sb.append("payloadLength  : ").append(payloadLength).append("\r\n");
		Cell head = this.getHead();
		while(head != null){
			sb.append("wordElement : ").append(head.getWordElement()).append("\r\n");
			head = head.getNext();
		}
		return sb.toString();
	}

}
