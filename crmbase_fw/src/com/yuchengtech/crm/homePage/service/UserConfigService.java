package com.yuchengtech.crm.homePage.service;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;
import com.yuchengtech.crm.homePage.model.AdminAuthAccountCfg;

/**
 *  用户UI配置
 * @author CHANGZH@YUCHENGTECH.COM
 * @date 2013-10-28
 */
@Service
@Transactional(value = "postgreTransactionManager")
public class UserConfigService extends CommonService {

	public UserConfigService() {
		JPABaseDAO<AdminAuthAccountCfg, Long> baseDAO = new JPABaseDAO<AdminAuthAccountCfg, Long>(
				AdminAuthAccountCfg.class);
		super.setBaseDAO(baseDAO);
	}

	public void updateUserCfg(String themeId) {
		String searchSql = "select n from AdminAuthAccountCfg n where n.userId =?1";
		Query query = em.createQuery(searchSql);
		query.setParameter(1, this.getUserSession().getUserId());
		query.setFirstResult(0);
		List<AdminAuthAccountCfg> result = (List<AdminAuthAccountCfg>)query.getResultList();
		if (result.size() == 0) {
			AdminAuthAccountCfg userCfg = new AdminAuthAccountCfg();
			userCfg.setUserId(this.getUserSession().getUserId());
			userCfg.setThemeId(themeId);
			userCfg.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			this.baseDAO.save(userCfg);
		} else if (result.size() == 1) {
			AdminAuthAccountCfg userCfg = result.get(0);
			userCfg.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			userCfg.setThemeId(themeId);
			this.baseDAO.save(userCfg);
		} else {
			throw new BizException(1,0,"1000","用户配置信息重复。");
		}
		
	}
	
	public void updateUserCfgColor(String ColorId) {
		String searchSql = "select n from AdminAuthAccountCfg n where n.userId =?1";
		Query query = em.createQuery(searchSql);
		query.setParameter(1, this.getUserSession().getUserId());
		query.setFirstResult(0);
		List<AdminAuthAccountCfg> result = (List<AdminAuthAccountCfg>)query.getResultList();
		if (result.size() == 0) {
			AdminAuthAccountCfg userCfg = new AdminAuthAccountCfg();
			userCfg.setUserId(this.getUserSession().getUserId());
			userCfg.setColorId(ColorId);
			userCfg.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			this.baseDAO.save(userCfg);
		} else if (result.size() == 1) {
			AdminAuthAccountCfg userCfg = result.get(0);
			userCfg.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			userCfg.setColorId(ColorId);
			this.baseDAO.save(userCfg);
		} else {
			throw new BizException(1,0,"1000","用户配置信息重复。");
		}
		
	}

	public String getUserCfg() {
		String searchSql = "select n from AdminAuthAccountCfg n where n.userId =?1";
		Query query = em.createQuery(searchSql);
		query.setParameter(1, this.getUserSession().getUserId());
		query.setFirstResult(0);
		List<AdminAuthAccountCfg> result = (List<AdminAuthAccountCfg>)query.getResultList();
		if (result.size() == 1) {
			if (result.get(0).getColorId() != null) {
				this.getUserSession().putAttribute("THEME_COLOR_ID", result.get(0).getColorId());
			}
			return result.get(0).getThemeId();
		} else if (result.size() == 0){
			return "1";
		} else {
			throw new BizException(1,0,"1000","用户配置信息重复。");
		}
		
	}

}
