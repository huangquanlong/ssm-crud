package com.yuchengtech.bcrm.system.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.dataauth.model.AuthSysFilterMap;
import com.yuchengtech.crm.exception.BizException;

/**
 * @describe 数据权限过滤去MAP模块 增删改
 * @author wz
 * @since 2012-11-16
 */
@Service
public class DataGrantMapService extends CommonService {
	public DataGrantMapService() {
		JPABaseDAO<AuthSysFilterMap, Long> baseDAO = new JPABaseDAO<AuthSysFilterMap, Long>(
				AuthSysFilterMap.class);
		super.setBaseDAO(baseDAO);
	}
	
	/**
	 * 保存时验证处理
	 * */
	@SuppressWarnings("unchecked")
	public Object save(Object model){
		
		AuthSysFilterMap asfm = (AuthSysFilterMap)model;
		String queryJql = "select m.id from AuthSysFilterMap m where m.className = '"+asfm.getClassName()+"'";
		if (asfm.getId() != null) {
			
		}
		if (asfm.getId() != null) {
			queryJql += " and m.id <>" + asfm.getId();
		}
		
		Map<String,Object> values=new HashMap<String,Object>();		
		
		List<AuthSysFilterMap> rsList = this.findByJql(queryJql, values);
		if(!rsList.isEmpty()){//判断类名称是否唯一
			throw new BizException(1,0,"10000","已经存在类名称【"+asfm.getClassName()+"】！");
		}
		asfm.setFunctionId(Long.valueOf("0"));
		
		return super.save(asfm);
	}

}
