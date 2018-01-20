package com.yuchengtech.bcrm.workreport.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.workreport.model.OcrmFSysReport;
import com.yuchengtech.bcrm.workreport.model.OcrmFSysReportCfg;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;

/****
 * 
 * 报表配置
 * @author CHANGZH
 * @date 2013-07-17 
 */

@Service
public class ReportCfgService  extends CommonService{
	
	public ReportCfgService(){
		   JPABaseDAO<OcrmFSysReport, Long>  baseDAO = new JPABaseDAO<OcrmFSysReport, Long>(OcrmFSysReport.class);  
		   super.setBaseDAO(baseDAO);
	}
	
	/**
	 * 更新报表状态
	 * @param id 报表ID
	 * @param reportStatus 报表状态
	 **/
	@SuppressWarnings("unchecked")
	public void updateReportStatus(Long id, Long reportStatus) {
		OcrmFSysReport ofsr = (OcrmFSysReport) this.find(id);
		ofsr.setReportStatus(reportStatus);
		baseDAO.save(ofsr);
	}
	/**
	 * 保存报表配置
	 * @param reportPanel 报表主表
	 * @param reportList 报表子表list
	 **/
	public void saveReport(Map<String, Object> reportPanel, List<Map<String, Object>> reportList) {
		OcrmFSysReport ofsr = new OcrmFSysReport();
		//新增修改判断
		if (null != reportPanel.get("id") && !"".equals(reportPanel.get("id"))) {
			ofsr = (OcrmFSysReport) this.find((Long.parseLong((String) reportPanel.get("id"))));
			ofsr.setReportStatus(ofsr.getReportStatus());
		} else {
			ofsr.setReportStatus(new Long(0));
		}
		ofsr.setReportName((String) reportPanel.get("reportName"));
		ofsr.setReportCode((String) reportPanel.get("reportCode"));
		validatorReport(ofsr);
		ofsr.setReportUrl((String) reportPanel.get("reportUrl"));
		ofsr.setReportGroup((String) reportPanel.get("reportGroup"));
		ofsr.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
		//ofsr.setReportType("0");
		//ofsr.setReportType(null);
		ofsr.setCreator(getUserSession().getUserId());
		if (null != reportPanel.get("reportDesc") && !"".equals(reportPanel.get("reportDesc"))) {
			ofsr.setReportDesc((String) reportPanel.get("reportDesc"));
		}
		//if (null != reportPanel.get("reportGroup") && !"".equals(reportPanel.get("reportGroup"))) {
			//ofsr.setReportGroup((String) reportPanel.get("reportGroup"));
		//}
		if (null != reportPanel.get("reportType") && !"".equals(reportPanel.get("reportType"))) {
			ofsr.setReportType((String) reportPanel.get("reportType"));
		}
		if (null != reportPanel.get("reportSort") && !"".equals((String)reportPanel.get("reportSort"))) {
			ofsr.setReportSort(Long.parseLong((String) reportPanel.get("reportSort")));
		}		
		if (null != reportPanel.get("reportServerType") && !"".equals((String)reportPanel.get("reportServerType"))) {
			ofsr.setReportServerType((String) reportPanel.get("reportServerType"));
		}		
		baseDAO.save(ofsr);
		JPABaseDAO<OcrmFSysReportCfg, Long>  cfgbaseDAO = new JPABaseDAO<OcrmFSysReportCfg, Long>(OcrmFSysReportCfg.class); 
		cfgbaseDAO.setEntityManager(em);
		//更新子表数据
		if (null != reportList) {
			Map<String, Object> params = new HashMap<String, Object>();
			StringBuilder jql = new StringBuilder("delete from OcrmFSysReportCfg o where o.reportCode='"+ofsr.getReportCode()+"'");
			cfgbaseDAO.batchExecuteWithNameParam(jql.toString(), params);
			for (Map<String, Object> ofsrc : reportList) {
				OcrmFSysReportCfg cfg = new OcrmFSysReportCfg();
				cfg.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
				cfg.setConditionField((String) ofsrc.get("conditionField"));
				cfg.setConditionName((String) ofsrc.get("conditionName"));
				cfg.setConditionType((String) ofsrc.get("conditionType"));
				cfg.setConditionDefault((String) ofsrc.get("conditionDefault"));
				cfg.setIsAllowBlank((String) ofsrc.get("isAllowBlank"));
				cfg.setIsHidden((String) ofsrc.get("isHidden"));
				cfg.setReportCode(ofsr.getReportCode());
				cfgbaseDAO.save(cfg);
			}
		}
	}

	/**
	 * 检验报表信息
	 * @param OcrmFSysReport 报表信息
	 */
	private void validatorReport(OcrmFSysReport ofsr) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> values = new HashMap<String, Object>();
		sb.append("select o from OcrmFSysReport o where 1=1 ");
		List relist = new ArrayList();
		if (null != ofsr.getId()) {
			 sb.append(" and o.id<>"+ofsr.getId());
			 relist = this.baseDAO.findWithNameParm(sb.toString() + " and o.reportCode='"+ ofsr.getReportCode()+"'", values);
			 if (relist.size() > 0) {
				 throw new BizException(1,2,"1002","报表编码重复");
			 }
			 relist = this.baseDAO.findWithNameParm(sb.toString() + " and o.reportName='"+ ofsr.getReportName()+"'", values);
			 if (relist.size() > 0) {
				 throw new BizException(1,2,"1002","报表名称重复");
			 }
		} else {
			 relist = this.baseDAO.findWithNameParm(sb.toString() + " and o.reportCode='"+ ofsr.getReportCode()+"'", values);
			 if (relist.size() > 0) {
				 throw new BizException(1,2,"1002","报表编码重复");
			 }
			 relist = this.baseDAO.findWithNameParm(sb.toString() + " and o.reportName='"+ ofsr.getReportName()+"'", values);
			 if (relist.size() > 0) {
				 throw new BizException(1,2,"1002","报表名称重复");
			 }
		}
		
	}
	 
}
