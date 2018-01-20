package com.yuchengtech.bcrm.workplat.service;



import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.workplat.model.OcrmFWpRemindMsg;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

@Service
public class OcrmFWpRemindMsgService extends CommonService{
//	
	public OcrmFWpRemindMsgService(){
		
		JPABaseDAO<OcrmFWpRemindMsg, Long>  baseDAO=new JPABaseDAO<OcrmFWpRemindMsg, Long>(OcrmFWpRemindMsg.class);  
		   super.setBaseDAO(baseDAO);
	}
	
	
}
