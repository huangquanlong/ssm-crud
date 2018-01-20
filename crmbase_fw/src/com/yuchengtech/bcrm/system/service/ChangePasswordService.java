package com.yuchengtech.bcrm.system.service;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AdminAuthAccount;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/**
 * 个人密码修改Service
 * @author wangwan
 * @since 2012-10-23 
 */
@Service
public class ChangePasswordService extends CommonService {
   
   public ChangePasswordService(){
		JPABaseDAO<AdminAuthAccount, Long>  baseDAO=new JPABaseDAO<AdminAuthAccount, Long>(AdminAuthAccount.class);  
		super.setBaseDAO(baseDAO);
	}
   
}
