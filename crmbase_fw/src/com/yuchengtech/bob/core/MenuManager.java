package com.yuchengtech.bob.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.MenuTreeNode;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.ehcache.EhCacheManager;
import com.yuchengtech.crm.ehcache.IEhCacheManager;
/****
 * 
 *    系统菜单管理器
 *    @author CHANGZH 
 *    @date   20130419
 */
public class MenuManager {
	
	/***单态对象*/
	private static MenuManager instance;
	/***日志*/
	private static Logger log = Logger.getLogger(MenuManager.class);
	/***dataSource变量*/
	private DataSource dsOracle;
	/**菜单*/
	private MenuTreeNode menuTree = new MenuTreeNode();
	/**菜单list*/
	private List<Map> menuList = new ArrayList<Map>();
	/**菜单树节点List*/
	List<MenuTreeNode> userMenuNodes;
	/***rootId*/
	private static String rootId = "0";
	/***rootName*/
	private static String rootName = "根节点";
	/***node*/
	private MenuTreeNode menuNode = new MenuTreeNode();
	/***菜单最大深度*/
	public Long menuMaxLevel = new Long(0);
	/***菜单 keyName*/
	public static String menuTreeName = "menuTree";
	/***构造方法*/
	private MenuManager() {
    }
	
	
	
	public void setDsOracle(DataSource dsOracle) {
		this.dsOracle = dsOracle;
	}



	/***取单态方法*/
	public static synchronized MenuManager getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new MenuManager();
			instance.menuTree.setId(rootId);
			instance.menuTree.setName(rootName);
			instance.menuTree.setNodeLevel(new Long(0));
			instance.menuMaxLevel = new Long(0);
		}
		return instance;
	}
	/**
	 *  构造菜单树
	 * @param menuList 菜单数据
	 * 
	 */
	private void initMenuTree(MenuTreeNode node) {
		if (menuList != null && menuList.size() > 0) {
			for(Map menu : menuList) {
				if (menu != null) {
					if(node.getId().equals((String)menu.get("PARENT_ID"))) {
						MenuTreeNode child = new MenuTreeNode();
						child.setId((String)menu.get("ID"));
						child.setParentId((String)menu.get("PARENT_ID"));
						child.setName((String)menu.get("NAME"));
						child.setNodeType((String)menu.get("NODETYPE"));
						if ("1".equals(child.getNodeType())) {
							child.addAttribute("opCode", (String)menu.get("OPCODE"));
						} else {
							child.addAttribute("ACTION", (String)menu.get("ACTION"));
						}
						child.setNodeLevel(node.getNodeLevel()+1);
						if(child.getNodeLevel() > menuMaxLevel) {
							menuMaxLevel = child.getNodeLevel();
						}
						child.addAttribute("sortOrder", (String)menu.get("ORDER_"));
						node.getChildren().add(child);
						menu = null;;
					}
				}
			}
			if (node.getChildren().size() == 0) {
				node.setIsLeaf(true);
				return;
			} else {
				node.setIsLeaf(false);
				for(MenuTreeNode childNode : node.getChildren()) {
					initMenuTree(childNode);
				}
			}
		}
	}
	
	/***
	 * 数据字典管理器初始化
	 * @param applicationContext spring上下文
	 **/
	public void initialize() {
		refreshMenusCache();
	}
	/***
	 * 取Cache
	 * @param name cacheName
	 **/
	private Cache getCache(String name) {
    	return (Cache) EhCacheManager.getInstance().getCacheManager().getCache(name);
    }
	/***
	 * from Cache取menus
	 **/
	private MenuTreeNode getMenusFromCache() {
		Cache lookupCache = getCache(IEhCacheManager.CRMCacheName);
		Element menusElement = lookupCache.get(menuTreeName);
    	return (MenuTreeNode)menusElement.getObjectValue();
    }
	/***刷新菜单缓存*/
	@SuppressWarnings("unchecked")
	public void refreshMenusCache() {
		log.info("开始刷新CRM的菜单缓存……");
		this.menuTree = new MenuTreeNode();
		this.menuTree.setId(rootId);
		this.menuTree.setName(rootName);
		this.menuTree.setNodeLevel(new Long(0));
		this.menuMaxLevel = new Long(0);
		menuList = loadMenu();		
		initMenuTree(menuTree);
		Element menusElement = new Element(menuTreeName, this.menuTree);
		log.info("CRM的菜单缓存刷新完成，共计：" + menuList.size());
		getCache(IEhCacheManager.CRMCacheName).put(menusElement);
	}
	/***取菜单节点*/
	public String getMenuName(String id) {
		if (id != null && !"".equals(id)) {
			menuNode = new MenuTreeNode();
			findMenuNode(id, this.menuTree);
			return menuNode.getName();
		} else {
			return null;
		}
	}
	/***取菜单节点*/
	public MenuTreeNode getMenuNode(String id) {
		if (id != null && !"".equals(id)) {
			menuNode = new MenuTreeNode();
			findMenuNode(id, this.menuTree);
			return menuNode;
		} else {
			return null;
		}
	}
	/***取菜单*/
	private void findMenuNode(String id, MenuTreeNode parentNode) {
		for (MenuTreeNode node : parentNode.getChildren()) {
			if (id.equals(node.getId())) {
				this.menuNode = node;
				break;
			} else {
				findMenuNode(id, node);
			}
		}
	}
	private List loadMenu() {
		StringBuffer menuSql = new StringBuffer(" SELECT C.ID,C.PARENT_ID, C.NAME,'0' as NODETYPE,'' as OPCODE,F.ACTION as ACTION,C.ORDER_ as ORDER_" +
												" FROM CNT_MENU C LEFT JOIN FW_FUNCTION F ON C.MOD_FUNC_ID = F.ID WHERE " +
												" C.APP_ID = '"+SystemConstance.LOGIC_SYSTEM_APP_ID+"' ORDER BY C.ORDER_ ASC");
		StringBuffer ctopSql = new StringBuffer(" SELECT RC.ID,CM.ID AS PARENT_ID,RC.NAME ,'1' as NODETYPE,RC.CON_CODE as OPCODE,'' as ACTION,9999999 as ORDER_ " +
												" FROM AUTH_RES_CONTROLLERS RC LEFT JOIN CNT_MENU CM ON RC.FW_FUN_ID=CM.MOD_FUNC_ID " +
												" WHERE CM.PARENT_ID IS NOT NULL");
		QueryHelper qh;
		List resultList = new ArrayList();
		List menusList;
		List ctrList;
		try {
			qh        = new QueryHelper(menuSql.toString(), dsOracle.getConnection());			
			menusList = (List) qh.getJSON().get("data");
			qh        = new QueryHelper(ctopSql.toString(), dsOracle.getConnection());
			ctrList   = (List) qh.getJSON().get("data");
			if (menusList != null && menusList.size() > 0) {
				resultList.addAll(menusList);
			} 
			if (ctrList != null && ctrList.size() > 0) {
				resultList.addAll(ctrList);
			} 
			return resultList;
		} catch (Exception e) {
			log.error("加载Menu数据时发生异常:", e);
			e.printStackTrace();
			return resultList;
		}
		
	}
	/**取菜单*/
	public MenuTreeNode getMenuTree() {
		Cache menuTreeCache = getCache(IEhCacheManager.CRMCacheName);
		Element menusElement = menuTreeCache.get(menuTreeName);
		if (menusElement == null) {
			refreshMenusCache();
		}		
		return getMenusFromCache();
	}
	/***
	 * 取session 用户权限树节点数组
	 * @return userMenus
	 */
	public List<MenuTreeNode> getUserMenuNodes(AuthUser auth) {
		userMenuNodes =  new ArrayList<MenuTreeNode>();
		Map<String,Object> menus = auth.getGrant();
		
		StringBuffer sb = new StringBuffer();
		
		for(String key : menus.keySet()){
			if(sb.toString().equals("")){
				sb.append(key);
			}else{
				sb.append(","+key);
			}
		}
		sb.insert(0, "SELECT C.ID FROM CNT_MENU C LEFT JOIN FW_FUNCTION F ON C.MOD_FUNC_ID = F.ID WHERE C.ID IN (");
		sb.append(") ORDER BY C.ORDER_ ASC ");
		
		QueryHelper indexInit;
		List<Map> menuIds = new ArrayList<Map>();
		try {
			indexInit = new QueryHelper(sb.toString(), dsOracle.getConnection());
			menuIds = (List) indexInit.getJSON().get("data");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
			
		for(Map menuMap : menuIds){
			if (userMenuNodes.size() > 0) {
				userMenuNodes.add(this.getMenuNode((String)menuMap.get("ID")));
			} else {
				userMenuNodes.add(this.getMenuNode((String)menuMap.get("ID")));
			}
		}
		return userMenuNodes;
	}
	/***
	 * 取session 用户权限树
	 * @return userMenuTree
	 */
	public MenuTreeNode getUserMenuTree(AuthUser auth) {
		MenuTreeNode userMenuTree = new MenuTreeNode();
		userMenuTree.setId(rootId);
		userMenuTree.setId(rootId);
		userMenuTree.setName(rootName);
		userMenuTree.setNodeLevel(new Long(0));
		getUserMenuNodes(auth);
		initUserMenuTree(userMenuTree);
		return userMenuTree;
	}
	/***
	 * 构造 session 用户菜单树
	 * @return userMenuTree
	 */
	private void initUserMenuTree(MenuTreeNode parentNode) {
		for(MenuTreeNode node : userMenuNodes) {
			if (node != null) {
				if(parentNode.getId().equals(node.getParentId())) {
					MenuTreeNode child = new MenuTreeNode();
					child.setId(node.getId());
					child.setParentId(node.getParentId());
					child.setName(node.getName());
					child.setNodeType(node.getNodeType());					
					if ("1".equals(child.getNodeType())) {
						child.addAttribute("opCode", node.getAttribute("opCode"));
					} else {
						child.addAttribute("ACTION", (String)node.getAttribute("ACTION"));
					}
					child.setNodeLevel(node.getNodeLevel());
					parentNode.getChildren().add(child);
					node = null;;
				}
			}
		}
		if (parentNode.getChildren().size() == 0) {
			parentNode.setIsLeaf(true);
			return;
		} else {
			parentNode.setIsLeaf(false);
			for(MenuTreeNode childNode : parentNode.getChildren()) {
				initUserMenuTree(childNode);
			}
		}
	}
}
