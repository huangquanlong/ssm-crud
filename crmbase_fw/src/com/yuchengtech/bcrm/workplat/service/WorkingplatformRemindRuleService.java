package com.yuchengtech.bcrm.workplat.service;


import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.FwSysProp;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
@Service
public class WorkingplatformRemindRuleService extends CommonService{
	
	 public WorkingplatformRemindRuleService(){
		   JPABaseDAO<FwSysProp, Long>  baseDAO = new JPABaseDAO<FwSysProp, Long>(FwSysProp.class);  
		   super.setBaseDAO(baseDAO);
	 }
	 
	 public Object saveData(Object obj) {
			return baseDAO.save(obj);
		}
	 public Object merge(Object obj) {
			return baseDAO.merge(obj);
		}
	 
}
