package com.leederedu.qsearch.core.bean;

import java.util.HashSet;

/**
 * 索引相关配置参数
 * @author TaneRoom
 * @since 2016年8月11日 下午5:10:58
 */
public class IndexConfig {

	//配置参数
	private static HashSet<ConfigBean> configBean;
	
	/**
	 * 默认的配置<br/>
	 * 静态内部类是属于外部类的，会随外部类一起被实例化
	 * @author TaneRoom
	 * @since 2016年8月11日 下午5:34:33
	 */
	private static class LazyLoadConfigBean{
		private static final HashSet<ConfigBean> defualtConfigBean = new HashSet<ConfigBean>();
		static{
			ConfigBean configBean = new ConfigBean();
			defualtConfigBean.add(configBean);
		}
	}

	public static HashSet<ConfigBean> getConfigBean() {
		//如果没有对configbean进行初始化，则使用默认配置
		if(configBean == null){
			configBean = LazyLoadConfigBean.defualtConfigBean;
		}
		return configBean;
	}

	public static void setConfigBean(HashSet<ConfigBean> configBean) {
		IndexConfig.configBean = configBean;
	}
	
}
