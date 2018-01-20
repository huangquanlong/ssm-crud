package com.yuchengtech.bcrm.workreport.action;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.sec.common.SystemUserConstance;
/****
 * 
 * 报表展示
 * @author CHANGZH
 * @date 2013-07-17 
 */
@SuppressWarnings("serial")
@Action("/ReportQueryAction")
public class ReportQueryAction extends CommonAction 
{
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	
	/**
	 * 拼装查询SQL
	 */
	public void prepare() 
	{ 
		StringBuffer roleIds = new StringBuffer();
		StringBuffer roleCon = new StringBuffer("");
		for (int i = 0; i < getUserSession().getRolesInfo().size(); i ++ ) {
			Map tempMap = (HashMap)getUserSession().getRolesInfo().get(i);
			if (roleIds.length() == 0 ) {
				roleIds.append("'" + tempMap.get("ROLE_CODE") + "'");
			} else {
				roleIds.append(",'" + tempMap.get("ROLE_CODE") + "'");
			}
		}
		if (!isLogicSysManager()) {
			roleCon.append(" AND R.REPORT_CODE IN ( SELECT A.REPORT_CODE FROM OCRM_F_SYS_REPORT_AUTH A WHERE A.APP_ID= '"+ SystemConstance.LOGIC_SYSTEM_APP_ID+"'"
			+ "AND A.ROLE_CODE in ("+roleIds+") )");
		}
		
		StringBuilder sb = new StringBuilder("SELECT * FROM OCRM_F_SYS_REPORT R WHERE 1=1" +
				 roleCon +
				" AND R.REPORT_STATUS='1' " +
				" ORDER BY R.REPORT_SORT");
		SQL = sb.toString();
		datasource = ds;
	}
	/**
	 * 获取报表服务器路径
	 */
	public String getReportServerPath() {
		if(this.json!=null)
    		this.json.clear();
    	else 
    		this.json = new HashMap<String,Object>(); 
		json.put("reportServerPath", FileTypeConstance.getSystemProperty("REPORT_PATH"));
		return "success";
	}
	
	/**
	 * 判断是否过滤
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isLogicSysManager() {
		AuthUser userDetails = this.getUserSession();
		boolean isLogicSysManagerFlag = false;
		for (int i = 0; i < userDetails.getRolesInfo().size(); i ++ ) {
			Map tempMap = (HashMap) userDetails.getRolesInfo().get(i);
			String roleCode = (String) tempMap.get("ROLE_CODE");
			if ( SystemUserConstance.LOGIC_SYSTEM_USER_ID.equals(roleCode) ||
				 SystemUserConstance.SUPER_SYSTEM_USER_ID.equals(roleCode) ||
				 SystemUserConstance.SYSTEM_ADMIN_ID.equals(roleCode)) {
				isLogicSysManagerFlag = true;
				break;
			}
		}
		
		return isLogicSysManagerFlag;
	}
}
