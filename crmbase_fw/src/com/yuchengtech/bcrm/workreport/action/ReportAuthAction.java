package com.yuchengtech.bcrm.workreport.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.workreport.model.OcrmFSysReportAuth;
import com.yuchengtech.bcrm.workreport.service.ReportAuthService;
import com.yuchengtech.bob.common.CommonAction;



@Action("/ReportAuthAction")

public class ReportAuthAction extends CommonAction {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ReportAuthService testPersonInfoService;

	@Autowired
	public void init() {
		model = new OcrmFSysReportAuth();
		setCommonService(testPersonInfoService);
	}

	/**角色页面查询显示*/
	public HttpHeaders indexPage() throws Exception {
		try {
			StringBuilder sb = new StringBuilder(
					"select c from AdminAuthRole c where 1=1 and c.roleType='normaluser'");
			Map<String, Object> values = new HashMap<String, Object>();
			
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			this.setJson(request.getParameter("condition"));
			
			 return super.indexPageByJql(sb.toString(), values);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**未授权列表查询显示*/
	public HttpHeaders indexReportPage() throws Exception {
		try {
			StringBuilder sb = new StringBuilder(
					"select c from OcrmFSysReport c where 1=1 and c.appId=62 and c.reportStatus=1");
					
			Map<String, Object> values = new HashMap<String, Object>();
			
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			this.setJson(request.getParameter("condition"));
			String selectCode=request.getParameter("selectCode");
			
			 if(!selectCode.equals("") && null!=selectCode){
                sb.append(" and c.reportCode in (select a.reportCode from OcrmFSysReportAuth a where a.roleCode in (select b.roleCode from AdminAuthRole b where b.roleCode='"+selectCode+"'))");
			 }
			 sb.append("order by c.reportSort desc");
			 return super.indexPageByJql(sb.toString(), values);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	
	
	/**授权列表查询显示*/
	public HttpHeaders indexReportPageTwo() throws Exception {
		try {
			StringBuilder sb = new StringBuilder(
					"select c from OcrmFSysReport c where 1=1 and c.appId=62 and c.reportStatus=1");	
			Map<String, Object> values = new HashMap<String, Object>();
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			this.setJson(request.getParameter("condition"));
			String selectCode=request.getParameter("selectCode");
			
			
			 if(!selectCode.equals("") && null!=selectCode){
                sb.append(" and c.reportCode not in (select a.reportCode from OcrmFSysReportAuth a where a.roleCode in (select b.roleCode from AdminAuthRole b where b.roleCode='"+selectCode+"'))");
			 }
			 sb.append("order by c.reportSort desc");
			 return super.indexPageByJql(sb.toString(), values);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**移除所有*/
	public String deleteAll(){
		StringBuilder sb = new StringBuilder(
		"delete  from OcrmFSysReportAuth c where 1=1 ");
		Map<String, Object> values = new HashMap<String, Object>();
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
		.get(ServletActionContext.HTTP_REQUEST);
		String roleCode=request.getParameter("roleCode");

		sb.append("and c.roleCode='"+roleCode+"'");
		return testPersonInfoService.deleteOne(sb);

	}
	
	/**选中功能*/
	public String insertOne(){
		ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        String roleCode=request.getParameter("roleCode");
        String reportCode=request.getParameter("reportCode");
        
        return testPersonInfoService.insertOne(roleCode,reportCode);
	}
	
	public String saveAuth(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
		.get(ServletActionContext.HTTP_REQUEST);
		
		deleteAll();
		String roleCode = request.getParameter("roleCode");
		String reportCodes=request.getParameter("reportCodes");
		if(reportCodes==""){
			return "success";
		}
		else{
			String [] ids = reportCodes.split(",");
			for(int i=0 ; i< ids.length ;++i){
				testPersonInfoService.insertOne(roleCode,ids[i]);
			}
		}
		
		return "success";
	}
	
	
	
	/**移除功能*/
	public String deleteOne (){
		StringBuilder sb = new StringBuilder(
				"delete  from OcrmFSysReportAuth c where 1=1 ");
		Map<String, Object> values = new HashMap<String, Object>();
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String roleCode=request.getParameter("roleCode");
        String reportCode=request.getParameter("reportCode");
 
       sb.append("and c.roleCode='"+roleCode+"' and c.reportCode='"+reportCode+"'");
       return testPersonInfoService.deleteOne(sb);
		
		
		
	}
	
	
	
	
}
