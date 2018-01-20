package com.yuchengtech.bcrm.workplat.service;



import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.workplat.model.OcrmFWpRemindRule;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

@Service
public class OcrmFWpRemindRuleService extends CommonService{
//	
	public OcrmFWpRemindRuleService(){
		
		JPABaseDAO<OcrmFWpRemindRule, Long>  baseDAO=new JPABaseDAO<OcrmFWpRemindRule, Long>(OcrmFWpRemindRule.class);  
		   super.setBaseDAO(baseDAO);
	}
	
	
}
