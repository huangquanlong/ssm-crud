package com.yuchengtech.bcrm.system.service;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.OcrmSysRicheditInfo;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/***
 * 基础示例
 * @author zhangmin
 *
 */
@Service
public class OcrmSysRicheditInfoService extends CommonService {
	public OcrmSysRicheditInfoService(){
		JPABaseDAO<OcrmSysRicheditInfo,Long> baseDAO = new JPABaseDAO<OcrmSysRicheditInfo,Long>(OcrmSysRicheditInfo.class);
		super.setBaseDAO(baseDAO);
	}
}
