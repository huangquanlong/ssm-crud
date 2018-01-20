package com.yuchengtech.bob.core;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.ehcache.EhCacheManager;
/***
 * 在线用户管理
 * @author CHANGZH
 * @since 2013-02-26
 */
public class UserOnlineManager {	 
	/***日志*/
	private static Logger log = Logger.getLogger(UserOnlineManager.class);
	/***在线用户缓存标识*/
	public static String cacheName = "userOnlineCache";
	/***在线用户缓存list标识*/
	public static String cacheKey = "userOnlineList";
	/***最大在线用户数缓存标识*/
	public static String onlineNumCacheKey = "userOnlineNum";
	
	public EhCacheManager ehCacheManager;
	
	public EhCacheManager getEhCacheManager() {
		return EhCacheManager.getInstance();
	}

	private Cache userOnlineCache;
	
	private static UserOnlineManager instance;
	
	public static synchronized UserOnlineManager getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new UserOnlineManager();
		}
		return instance;
	}

	/***
     * 在线用户信息刷新
     * @param flag 增加 or 移除
     * @param authUser 用户信息
     */
    @SuppressWarnings("unchecked")
	public void refreshCache(boolean flag, Object authUser) {
    	log.info("在线用户信息刷新"); 	
		Cache userOnlineCache = UserOnlineManager.getInstance().getUserOnlineCache();
        Element element = userOnlineCache.get(cacheKey);
        List<AuthUser> list = new ArrayList();
        if (element != null) {
			list = (List<AuthUser>)element.getObjectValue();
        }
        //log.info("刷新前在线用户信息个数为["+list.size()+"]");
        if (flag) {
        	addOnlineUser(list, (AuthUser) authUser);
        } else {
        	removeOnlineUser(list, (AuthUser) authUser);
        }
        //log.info("刷新后在线用户信息个数为["+list.size()+"]");
        //log.info("在线用户信息");
        //for (AuthUser au: list) {
        //	log.info("用户["+((AuthUser) au).getCname()+"];ip=["+((AuthUser) au).getCurrentIP()+"]");
        //}
        userOnlineCache.remove(UserOnlineManager.cacheKey);
        Element newElement = new Element(UserOnlineManager.cacheKey, list);
        userOnlineCache.put(newElement);
        log.info("在线用户数峰值刷新"); 	
        Element onlineElement = userOnlineCache.get(onlineNumCacheKey);
        int onlineNum = 0;
        if (onlineElement != null) {
        	onlineNum = (Integer)onlineElement.getObjectValue();
        	if (list.size() > onlineNum) {
        		onlineNum = list.size();
        	}
        } else {
        	onlineNum = list.size();
        }
        userOnlineCache.remove(onlineNumCacheKey);
        Element onlineNewElement = new Element(onlineNumCacheKey, onlineNum);
        userOnlineCache.put(onlineNewElement);
    }
    
    /***
     * 增加在线用户信息
     * @param list 在线用户信息列表
     * @param authUser 在线用户信息
     */
    public void addOnlineUser(List<AuthUser> list, Object authUser) {
    	//boolean flag = true;//true 此用户未登录，false此用户已登录
    	for (int i = 0; i < list.size(); i++) {
    		AuthUser au = list.get(i);
        	if (au.getUserId().equals(((AuthUser)authUser).getUserId())) {
        		//flag = false;
        		list.remove(i);
        	}
        }
    	//if (flag) {
    		log.info("增加在线用户信息["+((AuthUser) authUser).getCname()+"];ip=["+((AuthUser) authUser).getCurrentIP()+"]");
    		list.add((AuthUser) authUser);
    	//}
    }
    
    /***
     * 注销在线用户信息
     * @param list 在线用户信息列表
     * @param authUser 在线用户信息
     */
    public void removeOnlineUser(List<AuthUser> list, Object authUser) {
    	for (int i = 0; i < list.size(); i++) {
    		AuthUser au = list.get(i);
        	if (au.getUserId().equals(((AuthUser)authUser).getUserId())) {
        		log.info("移除在线用户信息["+((AuthUser) authUser).getCname()+"];ip=["+((AuthUser) authUser).getCurrentIP()+"]");
        		list.remove(i);
        	}
        }
    }
    
    public CacheManager getCacheManager() {
    	return EhCacheManager.getInstance().getCacheManager();
    }
    
	public void setUserOnlineCache(Cache userOnlineCache) {
		this.userOnlineCache = userOnlineCache;
	}
	public Cache getUserOnlineCache() {
		if (userOnlineCache == null) {
			userOnlineCache = getCacheManager().getCache(cacheName);
		}
		return userOnlineCache;
	}
    
}
