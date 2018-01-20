package com.yuchengtech.crm.homePage.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;
import com.yuchengtech.crm.homePage.model.OcrmFSysSheetHomepagecfg;
import com.yuchengtech.crm.homePage.model.OcrmFSysSheetusertabpnel;

/**
 *  用户首页配置
 * @author CHANGZH@YUCHENGTECH.COM
 * @date 2013-10-31
 */
@Service
@Transactional(value = "postgreTransactionManager")
public class SheetHomePageCfgService extends CommonService {

	public SheetHomePageCfgService() {
		JPABaseDAO<OcrmFSysSheetHomepagecfg, Long> baseDAO = new JPABaseDAO<OcrmFSysSheetHomepagecfg, Long>(
				OcrmFSysSheetHomepagecfg.class);
		super.setBaseDAO(baseDAO);
	}

	public void updateCfgInfo(List<Map<String, Object>> list) {
		try {
			//处理 tabPanel
			//delete old data
			String querySQL = "select n from OcrmFSysSheetusertabpnel n where n.userId =?1";
			Query query = em.createQuery(querySQL);
			query.setParameter(1, this.getUserSession().getUserId());
			query.setFirstResult(0);
			List<OcrmFSysSheetusertabpnel> result = (List<OcrmFSysSheetusertabpnel>)query.getResultList();
			JPABaseDAO<OcrmFSysSheetusertabpnel, Long> tabPanelDAO = new JPABaseDAO<OcrmFSysSheetusertabpnel, Long>(
					OcrmFSysSheetusertabpnel.class);
			tabPanelDAO.setEntityManager(getEntityManager());
			for(OcrmFSysSheetusertabpnel tabPanel: result) {
				tabPanelDAO.remove(tabPanel);
			}
			
			querySQL = "select n from OcrmFSysSheetHomepagecfg n where n.userId =?1";
			query = em.createQuery(querySQL);
			query.setParameter(1, this.getUserSession().getUserId());
			query.setFirstResult(0);
			List<OcrmFSysSheetHomepagecfg> usertabpnelList = (List<OcrmFSysSheetHomepagecfg>)query.getResultList();
			for(OcrmFSysSheetHomepagecfg tabCfg: usertabpnelList) {
				em.remove(tabCfg);
			}
			
			//insert current data
			List<OcrmFSysSheetusertabpnel>  tabPanelList = new ArrayList<OcrmFSysSheetusertabpnel>();
			List<String>  tabCmpIdList = new ArrayList<String>();
			Long tempId = null;
			for(Map<String, Object> tabInfo : list) {
				if (tabInfo.get("tabCmpId") != null && !tabCmpIdList.contains(tabInfo.get("tabCmpId"))) {
					OcrmFSysSheetusertabpnel tabPanel = new OcrmFSysSheetusertabpnel();
					tabPanel.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
					tabPanel.setTabName((String) tabInfo.get("tabTitle"));
					tabPanel.setUserId(this.getUserSession().getUserId());
					tempId = tabPanelDAO.save(tabPanel).getId();
					tabPanelList.add(tabPanel);
					tabCmpIdList.add((String) tabInfo.get("tabCmpId"));
				}
				tabInfo.put("tabId", tempId);
				System.out.println(tempId);
				OcrmFSysSheetHomepagecfg homePageCfg =  new OcrmFSysSheetHomepagecfg();
				homePageCfg.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
				homePageCfg.setUserId(this.getUserSession().getUserId());
				homePageCfg.setTabId( tabInfo.get("tabId")+"");
				homePageCfg.setModelId((String) tabInfo.get("modId"));
				homePageCfg.setModelColumn((tabInfo.get("colNum")==null?"":tabInfo.get("colNum"))+"");
				homePageCfg.setModelRownum((tabInfo.get("rowNum")==null?"":tabInfo.get("rowNum"))+"");
				this.baseDAO.save(homePageCfg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(1,0,"1000","首面配置更新异常。");
		}
	}

	public List<OcrmFSysSheetHomepagecfg> getUserCfg() {
		String searchSql = "select n from OcrmFSysSheetHomepagecfg n where n.userId =?1";
		Query query = em.createQuery(searchSql);
		query.setParameter(1, this.getUserSession().getUserId());
		query.setFirstResult(0);
		List<OcrmFSysSheetHomepagecfg> result = (List<OcrmFSysSheetHomepagecfg>)query.getResultList();
		if (result.size() > 0) {
			return result;
		} else {
			throw new BizException(1,0,"1000","暂无用户配置信息。");
		}
		
	}

}
