package com.leederedu.qsearch.core.bean;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * 索引基本配置属性<br/>
 * 基本属性包括索引名、硬盘存储位置、采用的分词器、commit操作执行频率、内存索引重读频率等
 * 
 * @author TaneRoom
 * @since 2016年8月11日 下午4:58:57
 */
public class ConfigBean {

	// 分词器
	private Analyzer analyzer = new StandardAnalyzer();
	// 索引存放地址
	private String indexPath = "lucene\\";
	// 在0.025s~5.0s之间重启一次线程，这个是时间的最佳实践
	private double indexReopenMaxStaleSec = 5;
	private double indexReopenMinStaleSec = 0.025;
	// 索引commit时间
	private int indexCommitSeconds = 60;
	// 索引名称
	private String indexName = "index";
	// commit时是否输出相关信息
	private boolean bprint;

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public String getIndexPath() {
		//indexPath = getWebInfoPath()+indexPath;
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		if (!(indexPath.endsWith("/") || indexPath.endsWith("\\"))) {
			indexPath += "/";
		}
		this.indexPath = indexPath;
	}

	public double getIndexReopenMaxStaleSec() {
		return indexReopenMaxStaleSec;
	}

	public void setIndexReopenMaxStaleSec(double indexReopenMaxStaleSec) {
		this.indexReopenMaxStaleSec = indexReopenMaxStaleSec;
	}

	public double getIndexReopenMinStaleSec() {
		return indexReopenMinStaleSec;
	}

	public void setIndexReopenMinStaleSec(double indexReopenMinStaleSec) {
		this.indexReopenMinStaleSec = indexReopenMinStaleSec;
	}

	public int getIndexCommitSeconds() {
		return indexCommitSeconds;
	}

	public void setIndexCommitSeconds(int indexCommitSeconds) {
		this.indexCommitSeconds = indexCommitSeconds;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public boolean isBprint() {
		return bprint;
	}

	public void setBprint(boolean bprint) {
		this.bprint = bprint;
	}

	/**
	 * 获取WEB-INF目录下指定文件路径
	 * @param fileName 指定文件名
	 * @return 返回对应路径
	 */
	public static String getWebInfoPath() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		path = path.replace('/', '\\'); // 将/换成\
		path = path.replace("file:", ""); // 去掉file:
		path = path.replace("classes\\", ""); // 去掉class\
		path = path.substring(1); // 去掉第一个\,如 \D:\JavaWeb...
		// System.out.println(path);
		return path;
	}
	
	public static void main(String arg[]) {
		ConfigBean conf = new ConfigBean();
		System.out.println(conf.getIndexPath());
	}
	
}
