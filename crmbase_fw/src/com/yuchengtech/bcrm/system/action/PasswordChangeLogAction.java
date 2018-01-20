package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AdminAuthPasswordLog;
import com.yuchengtech.bcrm.system.service.PasswordChangeLogService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.core.QueryHelper;

/*
 * 修改个人密码模块-查询当前用户密码信息Action
 * @author wangwan
 * @since 2012-10-08
 */

@SuppressWarnings("serial")
@Action("/passwordChangeLog")
public class PasswordChangeLogAction extends CommonAction {
	
	@Autowired
	private PasswordChangeLogService service;//定义UserManagerService属性
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	
	@Autowired
	public void init() {
		model = new AdminAuthPasswordLog();
		setCommonService(service);
		needLog = true;//新增修改删除记录是否记录日志,默认为false，不记录日志
	}
	/**
	 * 用户查询拼装SQL
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		String accountName = request.getParameter("accountName");

		//查询用户信息密码
    	StringBuffer sb = new StringBuffer("SELECT t.* FROM ADMIN_AUTH_ACCOUNT t ");
		
		if (accountName != null) {//判断
			sb.append(" WHERE t.ACCOUNT_NAME = '"+accountName+"'");
		}
    	SQL=sb.toString();
    	datasource = ds;
    	try{
    		json=new QueryHelper(SQL, ds.getConnection()).getJSON();
    	}catch(Exception e){}
}	

}


