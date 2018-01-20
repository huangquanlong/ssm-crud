package com.yuchengtech.crm.ehcache;

import net.sf.ehcache.CacheManager;
/***
 * 缓存管理器
 * @author CHANGZH
 * @since 2013-02-25
 */
public class EhCacheManager implements IEhCacheManager {	 
	
	/***缓存管理器*/
    private CacheManager cacheManager;
	
	private static EhCacheManager instance;
	
	public static synchronized EhCacheManager getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new EhCacheManager();
			instance.setCacheManager( new CacheManager());
		}
		return instance;
	}
	/***
	 * 获取CacheManager
	 */
	public CacheManager getCacheManager() {
		if (cacheManager == null) {
			cacheManager = new CacheManager();
		}
		return cacheManager;
	}
	/***
	 * 设置CacheManager
	 * @param cacheManager 缓存管理器
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

}
