package com.leederedu.qsearch.core.manager;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.leederedu.qsearch.utils.FormatUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.TrackingIndexWriter;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ReferenceManager;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

import com.leederedu.qsearch.core.bean.ConfigBean;
import com.leederedu.qsearch.core.bean.IndexConfig;

public class IndexManager {

	private IndexWriter indexWriter;
	//更新索引文件的indexWriter
	private TrackingIndexWriter trackingIndexWriter;
	//索引文件采用的分词器
	private Analyzer analyzer;
	//索引管理对象
	private ReferenceManager<IndexSearcher> referenceManager;
	//索引重读线程
	private ControlledRealTimeReopenThread<IndexSearcher> controlledRealTimeReopenThread;
	//索引写入磁盘线程
	private IndexCommitThread indexCommitThread;
	//索引地址
	private String indexPath;
	//索引重读最大、最小时间间隔
	private double indexReopenMaxStaleSec;
	private double indexReopenMixStaleSec;
	//索引commit时间
	private int indexCommitSeconds;
	//索引名
	private String indexManagerName;
	//commit时是否输出相关信息
	private boolean bprint = true;
	
	private static class LazyLoadIndexManager{
		private static final HashMap<String,IndexManager> indexManager 
			= new HashMap<String,IndexManager>();
		static{
			for(ConfigBean configBean : IndexConfig.getConfigBean()){
				indexManager.put(configBean.getIndexName(), new IndexManager(configBean));
			}
		}
	}
	
	private IndexManager(ConfigBean configBean){
		//设置相关属性
		this.analyzer = configBean.getAnalyzer();
		this.indexPath = configBean.getIndexPath();
		this.indexManagerName = configBean.getIndexName();
		this.indexReopenMaxStaleSec = configBean.getIndexReopenMaxStaleSec();
		this.indexReopenMixStaleSec = configBean.getIndexReopenMinStaleSec();
		this.indexCommitSeconds = configBean.getIndexCommitSeconds();
		this.bprint = configBean.isBprint();
		String indexFile = indexPath + indexManagerName +"/";
		//创建或者打开索引
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer); 
		//索引打开方式：没有就创建，有就打开
		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		Directory directory = null;
		try {
			directory = new NIOFSDirectory(Paths.get(indexFile));
			this.indexWriter = new IndexWriter(directory,indexWriterConfig);
			this.trackingIndexWriter = new TrackingIndexWriter(indexWriter);
//			this.nrtManager = new NRTManager(this.trackingIndexWriter,new SearcherFactory());
		
			this.referenceManager = new SearcherManager(
					trackingIndexWriter.getIndexWriter(), false, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//开启守护线程
		this.setThread();
	}
	
	/**
	 * 创建索引管理线程
	 */
	private void setThread(){
		this.controlledRealTimeReopenThread = new ControlledRealTimeReopenThread<IndexSearcher>(trackingIndexWriter, this.referenceManager, indexReopenMaxStaleSec, indexReopenMixStaleSec);
		this.controlledRealTimeReopenThread.setName("ReferenceManager Reopen Thread!");
		this.controlledRealTimeReopenThread.setPriority(Math.min(Thread.currentThread().getPriority()+2, Thread.MAX_PRIORITY));
		this.controlledRealTimeReopenThread.setDaemon(true);
		this.controlledRealTimeReopenThread.start();
		
		this.indexCommitThread = new IndexCommitThread(this.indexManagerName+" Index Commit Thread!");
		this.indexCommitThread.setDaemon(true);
		this.indexCommitThread.start();
				
	}
	
	/**
	 * 重启索引commit线程
	 * @return 返回是否启动成功
	 */
	public String setCommitThread(){
		try{
			if(this.indexCommitThread.isAlive()){
				return "is Alive!";
			}
			this.indexCommitThread = new IndexCommitThread(this.indexManagerName + " Index Commit Thread!");
			
			//将索引commit线程设置成守护线程
			this.indexCommitThread.setDaemon(true);
			this.indexCommitThread.start();
		}catch(Exception e){
			e.printStackTrace();
			return "failed";
		}
		return "reload";
	}
	
	/**
	 * 索引commit线程
	 * @author TaneRoom
	 * @since 2016年8月12日 上午10:59:00
	 */
	private class IndexCommitThread extends Thread{
		Log log = LogFactory.getLog(IndexCommitThread.class);
		
		public IndexCommitThread(String name){
			super(name);
		}
		
		@Override
		public void run() {
			while(true){
				try {
					indexWriter.commit();
					if(bprint){
						log.info(FormatUtil.formatTime(new Date())+"\t"+indexManagerName+"\tCommit");
					}
					TimeUnit.SECONDS.sleep(indexCommitSeconds);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 获取索引管理类
	 * @param indexName 索引名
	 * @return 返回索引管理类
	 */
	public static IndexManager getIndexManager(String indexName) {
		return LazyLoadIndexManager.indexManager.get(indexName);
	}
	
	/**
	 * 释放indexSearcher资源
	 * @param searcher 需要被释放的indexSearcher
	 */
	public void release(IndexSearcher searcher){
		try {
			this.referenceManager.release(searcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取indexSearcher对象
	 * @return
	 */
	public IndexSearcher getIndexSearcher(){
		try {
			return this.referenceManager.acquire();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public IndexWriter getIndexWriter() {
		return indexWriter;
	}

	public TrackingIndexWriter getTrackingIndexWriter() {
		return trackingIndexWriter;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public ReferenceManager<IndexSearcher> getReferenceManager() {
		return this.referenceManager;
	}

	/**
	 * 获取索引中的记录条数
	 * @return 返回记录条数
	 */
	public int getIndexNum(){
		return indexWriter.numDocs();
	}
	
}