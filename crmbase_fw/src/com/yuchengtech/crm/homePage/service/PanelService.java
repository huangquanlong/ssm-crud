package com.yuchengtech.crm.homePage.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.homePage.model.HomePageSet;
import com.yuchengtech.crm.homePage.model.TabPanel;

/**
 * 页签Service处理接口
 * 
 * @author lenovo
 * 
 */
@Service
@Transactional(value = "postgreTransactionManager")
public class PanelService extends CommonService {
	

	public PanelService() {
		JPABaseDAO<TabPanel, Long> baseDAO = new JPABaseDAO<TabPanel, Long>(
				TabPanel.class);
		super.setBaseDAO(baseDAO);
	}

	/**
	 * 新增页签
	 * 
	 * @param obj
	 * @return
	 */
	public void addTabPanel(Object obj) {
		TabPanel tabPanel = (TabPanel) obj;
		// 新增
		em.persist(tabPanel);
		em.flush();
	}

	/**
	 * 根据ID主键找到TabPanel对象
	 * 
	 * @param ID
	 * @return
	 */
	public Object getTabPanel(String ID) {
		return em.find(TabPanel.class, ID);
	}

	/**
	 * 删除页签
	 */
	public void removeTabpanel(String tabID) {
		TabPanel tabPanel = em.find(TabPanel.class, tabID);
		if (null != tabPanel) {
			em.remove(em.merge(tabPanel));
			em.flush();
		}

	}

	/**
	 * 更新页签标题
	 * 
	 * @param obj
	 * @return
	 */
	public void updateTabpanelName(Object obj) {
		TabPanel tabPanel = (TabPanel) obj;
		em.merge(tabPanel);
	}

	/**
	 * 页签增加模块
	 */
	public void addPanelMode(HomePageSet obj) {
		if (null != obj) {
			em.persist(obj);
			em.flush();
		}
	}

	/**
	 * 页签更新模块
	 */
	public void updatePabelMode(HomePageSet obj) {
		if (null != obj) {
			em.merge(obj);
			em.flush();
		}
	}
}
