package com.yuchengtech.bcrm.system.service;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AdminAuthPasswordLog;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/**
 * 用户管理Service
 * @author wangwan
 * @since 2012-10-08 
 */
@Service
public class PasswordChangeLogService extends CommonService {
	
   
   public PasswordChangeLogService(){
		JPABaseDAO<AdminAuthPasswordLog, Long>  baseDAO=new JPABaseDAO<AdminAuthPasswordLog, Long>(AdminAuthPasswordLog.class);  
		super.setBaseDAO(baseDAO);
	}
}
