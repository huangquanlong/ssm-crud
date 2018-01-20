package com.yuchengtech.bcrm.system.action;
import java.util.HashMap;
import java.util.Map;

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
 * 角色信息查询Action
 * @author wangwan
 * @since 2012-10-08
 */

@SuppressWarnings("serial")
@Action("/roleInfoQuery")
public class RoleInfoQueryAction extends CommonAction {
	
	@Autowired
	private RoleManagerService service;//定义RoleManagerService属性
	
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
		
		this.setJson(request.getParameter("condition"));
		String accountId = request.getParameter("accountId");
		String roleLevel = request.getParameter("roleLevel");
		
		//查询出角色信息,通过对用户表，用户角色信息表，用户授权角色信息表做关联，设置标志位，以显示用户已被授权的角色信息
		StringBuilder sb = new StringBuilder( " select f.*,(case when f.·check·  is null then 0"+
				 " else 1 end)is_checked  from  (SELECT * FROM ADMIN_AUTH_ROLE t0"+ 
					" left join (select t1.ID as ·check·,t1.ROLE_ID  from ADMIN_AUTH_ACCOUNT_ROLE t1 "+
					"left join ADMIN_AUTH_ACCOUNT t2 on t2.id=t1.ACCOUNT_ID where t1.ACCOUNT_ID= "+accountId+")e on e.ROLE_ID = t0.ID where t0.ROLE_LEVEL>='"+roleLevel+"')f where 1=1 order by f.ROLE_LEVEL" );
        
        addOracleLookup("ROLE_TYPE","ROLE_TYPE");
        addOracleLookup("ROLE_LEVEL","ROLE_LEVEL");
        
        SQL = sb.toString();
        datasource=ds;
	}
	
	 /**
	  * （自定义）批量删除，根据前台传递的idStr删除相应角色信息
	  * @return 成功标识
	  */
    public String batchDestroy(){
	   	ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		String jql="DELETE FROM AdminAuthRole C WHERE C.id IN ("+idStr+")";
		Map<String,Object> values=new HashMap<String,Object>();
		service.batchUpdateByName(jql, values);
		addActionMessage("batch removed successfully");
		
        return "success";
    }
}


