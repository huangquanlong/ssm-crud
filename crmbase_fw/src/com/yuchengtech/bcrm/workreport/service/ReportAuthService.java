package com.yuchengtech.bcrm.workreport.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.workreport.model.OcrmFSysReportAuth;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.constance.SystemConstance;

@Service
public class ReportAuthService extends CommonService{
	
	public ReportAuthService(){
		JPABaseDAO<OcrmFSysReportAuth,Long> baseDao = new JPABaseDAO<OcrmFSysReportAuth,Long>(OcrmFSysReportAuth.class);
		super.setBaseDAO(baseDao);
	}
	
	public String insertOne(String roleCode,String reportCode){
		//JPABaseDAO<OcrmFSysReportAuth, Long>  cfgbaseDAO = new JPABaseDAO<OcrmFSysReportAuth, Long>(OcrmFSysReportAuth.class);
		OcrmFSysReportAuth cfg= new OcrmFSysReportAuth();
		cfg.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
		cfg.setRoleCode(roleCode);
		cfg.setReportCode(reportCode);
		//cfg.setId(121212l);
		baseDAO.save(cfg);
		return "success";
	}
	
	public String deleteOne(StringBuilder jql){
		Map<String, Object> values = new HashMap<String, Object>();
		baseDAO.batchExecuteWithNameParam(jql.toString(), values);
		return "success";
	}
	
	
	

}
