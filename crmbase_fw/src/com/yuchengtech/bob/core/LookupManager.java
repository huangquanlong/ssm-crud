package com.yuchengtech.bob.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.yuchengtech.bob.common.LogService;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.ehcache.EhCacheManager;
import com.yuchengtech.crm.ehcache.IEhCacheManager;
/****
 * 
 * 数据字典管理器
 * @author modify by CHANGZH 20130307 
 *         使用 缓存管理数据字典数据;
 *         增加注释
 */
public class LookupManager {
	
	/***单态对象*/
	private static LookupManager instance;
	/***日志*/
	private static Logger log = Logger.getLogger(LookupManager.class);
	/***取dataSource*/
	public DataSource getDsOracle() {
		return dsOracle;
	}
	/***设置dataSource*/
	public void setDsOracle(DataSource dsOracle) {
		this.dsOracle = dsOracle;
	}
	/***dataSource变量*/
	private DataSource dsOracle;
	/***数据字典类别*/
	private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> oracleLookup;
	/***数据字典item*/
	private ConcurrentHashMap<String, String> oracleLookupItem;
	/**菜单*/
	private ConcurrentHashMap<String, String> menuMap;
	/***数据字典类别keyName*/
	public static String lookupKeyName     = "oracleLookup";
	/***数据字典Item keyName*/
	public static String lookupItemKeyName = "oracleLookupItem";
	/***菜单 keyName*/
	public static String menusKeyName = "menus";
	
	public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getAll(){
		return oracleLookup;
	}
	
	/***构造方法*/
	private LookupManager() {}
	
	/***取单态方法*/
	public static synchronized LookupManager getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new LookupManager();
		}
		return instance;
	}
	/***设置数据字典类别*/
	private void setOracleLookup(
			ConcurrentHashMap<String, ConcurrentHashMap<String, String>> oracleLookup) {
		this.oracleLookup = oracleLookup;
	}
	/***取得数据字典Item*/
	private ConcurrentHashMap<String, String> getOracleLookupItem() {
		return oracleLookupItem;
	}
	/***设置数据字典Item*/
	private void setOracleLookupItem(
			ConcurrentHashMap<String, String> oracleLookupItem) {
		this.oracleLookupItem = oracleLookupItem;
	}
	/**设置菜单*/
	private void setMenuMap(ConcurrentHashMap<String, String> menuMap) {
		this.menuMap = menuMap;
	}
	/***取数据字典Item值*/
	public String getOracleValue(String name, String code) {
		String value = null;
		setOracleLookupItem(getOracleLookup().get(name));
		if (getOracleLookupItem() != null) {
			value = getOracleLookupItem().get(code);
		}
		return value;
	}
	/***取数据字典Item map*/
	public ConcurrentHashMap<String, String> getOracleValues(String name) {
		return getOracleLookup().get(name);
	}
	/***增加数据字典Item*/
	private void addOracleLookupItem(String name, String code, String value) {
		if (name == null || code == null || value == null) {
			return;
		}
		oracleLookupItem = oracleLookup.get(name);
		if (oracleLookupItem == null) {
			oracleLookupItem = new ConcurrentHashMap<String, String>();
		}
		oracleLookupItem.put(code, value);
		oracleLookup.put(name, oracleLookupItem);
	}
	/***加载数据字典*/
	private int loadOracleLookup(String name) {
		String SQL = "SELECT t2.f_name, t1.f_code, t1.f_value FROM ocrm_sys_lookup_item t1 LEFT JOIN ocrm_sys_lookup t2 ON t1.f_lookup_id = t2.f_name";
		if (name != null) {
			SQL += " WHERE t2.f_name = ?";
		}
		int rowsCached = 0;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = dsOracle.getConnection();
			pstmt = conn.prepareStatement(SQL);
			if (name != null) {
				pstmt.setString(1, name);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				addOracleLookupItem(rs.getString("F_NAME"), rs
						.getString("F_CODE"), rs.getString("F_VALUE"));
				rowsCached++;
			}
		} catch (SQLException e) {
			log.error("加载Oracle数据库中的字典表时发生异常:", e);
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null){
					rs.close();
				}if(pstmt!=null){
					pstmt.close();
				}if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				log.error("数据字典关闭数据连接异常:", e);
				e.printStackTrace();
			}
			
		}
		return rowsCached;
	}
	/***
	 * 数据字典管理器初始化
	 * @param applicationContext spring上下文
	 **/
	public void initialize() {
		oracleLookup = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
		refreshLookupCache();
		refreshMenusCache();
		LogService.dsOracle = dsOracle;
	}
	/***
	 * 重新加载数据字典
	 * @param name 数据字典类别名称
	 **/
	public int reloadOracle(String name) {
		log.info("开始刷新CRM的字典表：" + name);
		oracleLookup.remove(name);
		int count = loadOracleLookup(name);
		log.info("完成加载CRM的字典表，共计：" + count);
		Element lookupElement = new Element(lookupKeyName, oracleLookup);		
		getCache(IEhCacheManager.CRMCacheName).put(lookupElement);
		log.info("CRM的字典表缓存刷新完成，共计：" + count);
		return 0;
	}
	/***
	 * 取Cache
	 * @param name cacheName
	 **/
	private Cache getCache(String name) {
    	return (Cache) EhCacheManager.getInstance().getCacheManager().getCache(name);
    }
	/***
	 * from Cache取oracleLookup
	 **/
	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getLookupFromCache() {
		Cache lookupCache = getCache(IEhCacheManager.CRMCacheName);
		Element lookupElement = lookupCache.get(lookupKeyName);
		
    	return (ConcurrentHashMap<String, ConcurrentHashMap<String, String>>)
    			lookupElement.getObjectValue();
    }
	/***
	 * from Cache取menus
	 **/
	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, String> getMenusFromCache() {
		Cache lookupCache = getCache(IEhCacheManager.CRMCacheName);
		Element menusElement = lookupCache.get(menusKeyName);
    	return (ConcurrentHashMap<String,String>)menusElement.getObjectValue();
    }
	/***刷新字典缓存*/
	private void refreshLookupCache() {
		setOracleLookup(new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>());
		log.info("开始刷新CRM的字典表缓存……");
		int count = loadOracleLookup(null);
		Element lookupElement = new Element(lookupKeyName, oracleLookup);
		log.info("CRM的字典表缓存刷新完成，共计：" + count);
		getCache(IEhCacheManager.CRMCacheName).put(lookupElement);
	}
	
	/***刷新菜单缓存*/
	private void refreshMenusCache() {
		setMenuMap(new ConcurrentHashMap<String, String>());
		log.info("开始刷新CRM的菜单缓存……");
		int count = loadMenu();		
		Element menusElement = new Element(menusKeyName, menuMap);
		log.info("CRM的菜单缓存刷新完成，共计：" + count);
		getCache(IEhCacheManager.CRMCacheName).put(menusElement);
	}
	/***取菜单*/
	public String getMenuName(String id) {
		String value = "";
		if (id != null)
			value = getMenuMap().get(id);
		return value;
	}
	private int loadMenu() {
		String SQL = "select id,name from cnt_menu where app_id='" +
						SystemConstance.LOGIC_SYSTEM_APP_ID + "'";
		int rowsCached = 0;
		try {
			ResultSet rs;
			PreparedStatement pstmt = dsOracle.getConnection()
					.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				menuMap.put(rs.getString("id"), rs.getString("name"));
				rowsCached++;
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			log.error("加载Menu映射时发生异常:", e);
			e.printStackTrace();
		} 
			return rowsCached;
	}
	/***取得数据字典类别*/
	private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getOracleLookup() {
		Cache lookupCache = getCache(IEhCacheManager.CRMCacheName);
		Element lookupElement = lookupCache.get(lookupKeyName);
		if (lookupElement == null) {
			refreshLookupCache();
		}		
		return getLookupFromCache();
	}
	/**取菜单*/
	private ConcurrentHashMap<String, String> getMenuMap() {
		Cache lookupCache = getCache(IEhCacheManager.CRMCacheName);
		Element menusElement = lookupCache.get(menusKeyName);
		if (menusElement == null) {
			refreshMenusCache();
		}		
		return getMenusFromCache();
	}
	
}
