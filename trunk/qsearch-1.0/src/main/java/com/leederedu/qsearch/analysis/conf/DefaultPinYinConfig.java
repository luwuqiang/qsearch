package com.leederedu.qsearch.analysis.conf;

/**
 * 默认拼音分词配置<br/>
 * 这个类可以放在spring中进行配置
 * @author TaneRoom
 * @since 2016年9月20日 上午10:25:58
 */
public class DefaultPinYinConfig {

	private static PinYinConfig pinYinconfig;
	/**
	 * 默认的配置<br/>
	 * 静态内部类是属于外部类的，会随外部类一起被实例化
	 * @author TaneRoom
	 * @since 2016年8月11日 下午5:34:33
	 */
	private static class LazyLoadConfig{
		private static final PinYinConfig defaultPinYinconfig = new PinYinConfig();
	}

	public static PinYinConfig getPinYinconfig() {
		//如果没有对config进行初始化，则使用默认配置
		if(pinYinconfig == null){
			pinYinconfig = LazyLoadConfig.defaultPinYinconfig;
		}
		return pinYinconfig;
	}

	public static void setPinYinconfig(PinYinConfig pinYinconfig) {
		DefaultPinYinConfig.pinYinconfig = pinYinconfig;
	}
	
}
