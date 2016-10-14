package com.leederedu.qsearch.analysis.core;

import java.util.Stack;
import java.util.TreeSet;

/**
 * SmartCnExt分词歧义裁决器
 * @author TaneRoom
 * @since 2016年9月12日 下午6:18:06
 */
class Arbitrator {

	Arbitrator() {
	}

	/**
	 * 分词歧义处理
	 * 
	 * @param orgLexemes
	 */
	void process(AnalyzeContext context) {
		QuickSortSet orgWordElements = context.getOrgWordElements();
		WordElement orgWordElement = orgWordElements.pollFirst();
		WordElementChain crossPath = new WordElementChain();
		while (orgWordElement != null) {
			if (!crossPath.addCrossWordElement(orgWordElement)) {
				// 找到与crossPath不相交的下一个crossPath
				if (crossPath.size() == 1) {
					// crossPath没有歧义 或者 不做歧义处理
					// 直接输出当前crossPath
					context.addWordElementChain(crossPath);
				} else {
					// 对当前的crossPath进行歧义处理
					QuickSortSet.Cell headCell = crossPath.getHead();
					WordElementChain judgeResult = this.judge(headCell, crossPath.getPathLength());
					// 输出歧义处理结果judgeResult
					context.addWordElementChain(judgeResult);
				}
				// 把orgLexeme加入新的crossPath中
				crossPath = new WordElementChain();
				crossPath.addCrossWordElement(orgWordElement);
			}
			orgWordElement = orgWordElements.pollFirst();
		}

		// 处理最后的path
		if (crossPath.size() == 1) {
			// crossPath没有歧义 或者 不做歧义处理
			// 直接输出当前crossPath
			context.addWordElementChain(crossPath);
		} else {
			// 对当前的crossPath进行歧义处理
			QuickSortSet.Cell headCell = crossPath.getHead();
			WordElementChain judgeResult = this.judge(headCell, crossPath.getPathLength());
			// 输出歧义处理结果judgeResult
			context.addWordElementChain(judgeResult);
		}
	}

	/**
	 * 歧义识别
	 * @param lexemeCell 歧义路径链表头
	 * @param fullTextLength 歧义路径文本长度
	 * @param option 候选结果路径
	 * @return
	 */
	private WordElementChain judge(QuickSortSet.Cell lexemeCell, int fullTextLength) {
		// 候选路径集合
		TreeSet<WordElementChain> pathOptions = new TreeSet<WordElementChain>();
		// 候选结果路径
		WordElementChain option = new WordElementChain();
		// 对crossPath进行一次遍历,同时返回本次遍历中有冲突的Lexeme栈
		Stack<QuickSortSet.Cell> lexemeStack = this.forwardPath(lexemeCell, option);
		// 当前词元链并非最理想的，加入候选路径集合
		pathOptions.add(option.copy());

		// 存在歧义词，处理
		QuickSortSet.Cell c = null;
		while (!lexemeStack.isEmpty()) {
			c = lexemeStack.pop();
			// 回滚词元链
			this.backPath(c.getWordElement(), option);
			// 从歧义词位置开始，递归，生成可选方案
			this.forwardPath(c, option);
			pathOptions.add(option.copy());
		}

		// 返回集合中的最优方案
		return pathOptions.first();
	}

	/**
	 * 向前遍历，添加词元，构造一个无歧义词元组合
	 * @param WordElementChain option
	 * @return
	 */
	private Stack<QuickSortSet.Cell> forwardPath(QuickSortSet.Cell lexemeCell, WordElementChain option) {
		// 发生冲突的WordElement栈
		Stack<QuickSortSet.Cell> conflictStack = new Stack<QuickSortSet.Cell>();
		QuickSortSet.Cell c = lexemeCell;
		// 迭代遍历WordElement链表
		while (c != null && c.getWordElement() != null) {
			if (!option.addNotCrossWordElement(c.getWordElement())) {
				// 词元交叉，添加失败则加入lexemeStack栈
				conflictStack.push(c);
			}
			c = c.getNext();
		}
		return conflictStack;
	}

	/**
	 * 回滚词元链，直到它能够接受指定的词元
	 * @param we
	 * @param option
	 */
	private void backPath(WordElement we, WordElementChain option) {
		while (option.checkCross(we)) {
			option.removeTail();
		}
	}

}
