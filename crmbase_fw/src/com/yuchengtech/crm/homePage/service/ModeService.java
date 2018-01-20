package com.yuchengtech.crm.homePage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.homePage.model.PanelMode;
import com.yuchengtech.crm.homePage.model.TabPanel;

/**
 * 模块Services 接口
 * @author lenovo
 *
 */
@Service
@Transactional(value = "postgreTransactionManager")
public class ModeService extends CommonService {
	
	public ModeService() {
		JPABaseDAO<TabPanel, Long> baseDAO = new JPABaseDAO<TabPanel, Long>(
				TabPanel.class);
		super.setBaseDAO(baseDAO);
	}
	
	/**
	 * 添加模块
	 * @param mode
	 */
	public void addMode(PanelMode mode){
		em.persist(mode);
		em.flush();
	}
	
	
	/**
	 * 删除模块
	 * @param modID
	 */
	public void removeMode(String modID)
	{
		PanelMode panelMode = em.find(PanelMode.class, modID);
		if (null != panelMode) {
			em.remove(em.merge(panelMode));
			em.flush();
		}
	}
	
	/**
	 * 更新模块
	 * @param mode
	 */
	public void updateMode(PanelMode mode){
		em.merge(mode);
		em.flush();
	}
	
	
	/**
	 * 查询模块
	 */
	public PanelMode getModeByID(String modID){
		return em.find(PanelMode.class, modID);
	}

}
