package com.yuchengtech.bcrm.system.service;


import org.springframework.stereotype.Service;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.dataauth.model.AuthSysFilter;

/**
 * @describe 数据权限过滤去FILTER模块 增删改
 * @author wz
 * @since 2012-11-16
 */
@Service
public class DataGrantFilterService extends CommonService {
	public DataGrantFilterService() {
		JPABaseDAO<AuthSysFilter, Long> baseDAO = new JPABaseDAO<AuthSysFilter, Long>(
				AuthSysFilter.class);
		super.setBaseDAO(baseDAO);
	}
	
	/**
	 * 新增时做验证处理
	 * */
	public Object save(Object model){
		
		AuthSysFilter asf = (AuthSysFilter)model;
		asf.setMethodName(0);
		asf.setRoleId(null);
		return super.save(asf);
	}

}
