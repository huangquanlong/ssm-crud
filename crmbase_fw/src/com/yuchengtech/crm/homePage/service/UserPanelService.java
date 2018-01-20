package com.yuchengtech.crm.homePage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.homePage.model.HomePageSet;
import com.yuchengtech.crm.homePage.model.UserPanMode;

/**
 * UserPanelService
 * @author lenovo
 *
 */
@Service
@Transactional(value="postgreTransactionManager")
public class UserPanelService extends CommonService{
	public UserPanelService(){
		   JPABaseDAO<UserPanMode, Long>  baseDAO=new JPABaseDAO<UserPanMode, Long>(UserPanMode.class);  
		   super.setBaseDAO(baseDAO);
	 }
	
	
	/**
	 * 添加用户 模块
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public Object addUserPanelMode(Object obj)
	{
		UserPanMode  userPanMode = (UserPanMode)obj;
		em.persist(userPanMode);
		return baseDAO.save(userPanMode);
	}
	
	/**
	 * 根据ID删除页签上的模块
	 */
	public void deleteUserPanMode(String ID){
		em.remove(em.find(HomePageSet.class, ID));
	}
	
	/**
	 * 根据ID添加页签上的模块
	 */
	public void addPanMode(Object obj){
		HomePageSet homeMode = (HomePageSet)obj;
		em.persist(homeMode);
	}
	

}
