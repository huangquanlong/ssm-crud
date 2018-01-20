package com.yuchengtech.bcrm.workplat.service;



import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.workplat.model.OcrmFWpRemindRead;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

@Service
public class OcrmFWpRemindReadService extends CommonService{
//	
	public OcrmFWpRemindReadService(){
		
		JPABaseDAO<OcrmFWpRemindRead, Long>  baseDAO=new JPABaseDAO<OcrmFWpRemindRead, Long>(OcrmFWpRemindRead.class);  
		   super.setBaseDAO(baseDAO);
	}
	
	
}
