package com.yuchengtech.crm.dataauth.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.exception.BizException;

@Action("/datagrantfilterquery")
@Results({@Result(name = "success", type = "redirectAction", params = {
		"actionName", "datagrantfilterquery" }) })
public class DataGrantFilterQueryAction extends CommonAction {

	 @Autowired
     @Qualifier("dsOracle")
     private DataSource ds;
	 private HttpServletRequest request;
	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String tempId = request.getParameter("tempId");
		String tempFilterId = request.getParameter("tempFilterId");
		if(tempId==null||tempId==""){
			throw new BizException(1,0,"9999","没有选择角色");
		}else{
			StringBuffer sb = new StringBuffer("select  auth.ID,auth.filter_id " +
					"from  auth_sys_filter_auth auth " +
					"left join AUTH_SYS_FILTER filter on filter.ID = auth.FILTER_ID " +
					"left join AUTH_SYS_FILTER_MAP map on filter.CLASS_NAME = map.CLASS_NAME " +
					"left join FW_FUNCTION fw on fw.ID = map.FUNCTION_ID " +
					"left join CNT_MENU menu on menu.MOD_FUNC_ID = fw.ID " +
					"where map.ID ="+tempFilterId+" and auth.ROLE_ID='"+tempId+"'");
			SQL = sb.toString();
		setPrimaryKey("auth.filter_id desc");
		datasource = ds;
	}
	}
}
