package com.yuchengtech.bcrm.system.action;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AdminAuthRole;
import com.yuchengtech.bcrm.system.service.RoleManagerService;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 角色层级信息查询Action
 * @author wangwan
 * @since 2012-10-08 
 */

@SuppressWarnings("serial")
@Action("/roleLevelQuery")
public class RoleLevelQueryAction extends CommonAction {
	
	@Autowired
	private RoleManagerService service;//定义RoleManagerService属性
	private HttpServletRequest request;
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	
	@Autowired
	public void init() {
		model = new AdminAuthRole();
		setCommonService(service);
		needLog = true;//新增修改删除记录是否记录日志,默认为false，不记录日志
	}
	/**
	 * 角色查询拼装SQL
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		String role_Id = request.getParameter("role_Id");
		
		//查询出角色层级信息
		StringBuilder sb = new StringBuilder( " select R.ROLE_LEVEL from ADMIN_AUTH_ROLE R" );
		
		if (role_Id != null) {//判断，根据用户角色ID不同，查询出对应角色层级信息
			sb.append(" WHERE R.ID = '"+role_Id+"'");
		}
        addOracleLookup("ROLE_TYPE","ROLE_TYPE");
        addOracleLookup("ROLE_LEVEL","ROLE_LEVEL");
        
        SQL = sb.toString();
        datasource=ds;
	}
}


