package com.yuchengtech.bcrm.system.service;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.OcrmSysViewManagerControl;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/**
 * 客户视图项维护
 * @author zhangsxin chenmeng
 * @since 2014-12-19
 */
@Service
public class CustViewMaintainControlService extends CommonService {
	public CustViewMaintainControlService(){
		JPABaseDAO<OcrmSysViewManagerControl, String>  baseDAO=new JPABaseDAO<OcrmSysViewManagerControl, String>(OcrmSysViewManagerControl.class);  
		super.setBaseDAO(baseDAO);
	}
	
	/**
	 * 删除的方法
	 * @param idStr
	 */
	public void remove(String idStr){
		em.remove(em.find(OcrmSysViewManagerControl.class, idStr));
	}
}
