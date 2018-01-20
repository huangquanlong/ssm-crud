package com.yuchengtech.bcrm.system.action;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AdminAuthRole;
import com.yuchengtech.bcrm.system.service.RoleManagerService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.common.DataType;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;
import com.yuchengtech.crm.sec.common.SystemUserConstance;

/**
 * 角色管理Action,查询及删除角色信息
 * @author weilh
 * @since 2012-09-25 
 */

@SuppressWarnings("serial")
@Action("/roleManagerQuery")
public class RoleManagerQueryAction extends CommonAction {
	
	private static Logger log = Logger.getLogger(RoleManagerQueryAction.class);
	
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
		
		//查询出角色信息
		StringBuilder sb = new StringBuilder("SELECT DISTINCT R.*,AR.ROLE_ID FROM  ADMIN_AUTH_ROLE R " +
			"LEFT JOIN ADMIN_AUTH_ACCOUNT_ROLE AR ON R.ID=AR.ROLE_ID " +
			"WHERE R.APP_ID=");
		sb.append(SystemConstance.LOGIC_SYSTEM_APP_ID);
		
		if (accountId != null&&accountId.equals("root")==false) {//判断，根据机构号不同，查询出对应角色信息
			if (accountId.length() > 0) {
				sb.append(" AND R.ACCOUNT_ID = '"+accountId+"'");
			}
		}
		
        for (String key : this.getJson().keySet()) {//查询条件判断
            if (null != this.getJson().get(key)&& !this.getJson().get(key).equals("")) {
                if (key.equals("ROLE_NAME")){//判断查询条件是否为角色名称
                	sb.append(" AND R."+key+" LIKE"+" '%"+this.getJson().get(key)+"%'");
                }else if (key.equals("ROLE_TYPE")&& !this.getJson().get(key).equals("")){//判断查询条件是否为角色类型
            		sb.append(" AND R."+key+" ='"+this.getJson().get(key)+"'");
                }else if(key.equals("id")){ //获得id，用于查看、修改
            		sb.append(" AND R."+key+" ="+this.getJson().get(key));
            	}	else if (key.equals("searchForRoleType")){
					sb.append("  AND (AR.ROLE_ID in ( "+this.getJson().get(key)+"))");
				} else if(key.equals("accountId")){
					sb.append(" AND R.ACCOUNT_ID = '"+this.getJson().get(key)+"'");
				}
            }
        }
        
        addOracleLookup("ROLE_TYPE","ROLE_TYPE");
        addOracleLookup("ROLE_LEVEL","ROLE_LEVEL");
        
        SQL = sb.toString();
        this.setPrimaryKey("R.ID");
        datasource=ds;
   		//configCondition("R.ROLE_NAME","=","ROLE_NAME",DataType.String);
   		configCondition("R.ROLE_CODE","=","ROLE_CODE",DataType.String);
   		configCondition("R.ROLE_TYPE","=","ROLE_TYPE",DataType.String);
   		configCondition("R.ROLE_LEVEL","=","ROLE_LEVEL",DataType.String);
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
    
    /**
     * 保存角色信息，判断角色编码是否重复
     * @return 成功标识
     */
    public DefaultHttpHeaders create() {
    	String message = "";//抛出异常信息
    	try{
    		AdminAuthRole adminAuthRole = (AdminAuthRole) model;
    		String roleCode = adminAuthRole.getRoleCode();//获取新增角色编码
    		//按照角色编码查询的对象
    		AdminAuthRole rCode = (AdminAuthRole)service.findUniqueByRoleCode("roleCode",roleCode);
    		
    		//判断获取角色表主键，如果主键值为null,新增的操作
    		if ((null != roleCode && !roleCode.equals(""))&& null == adminAuthRole.getId()) {
    			if (null != rCode) {
    				message = "角色编码重复，请重新填写!"; 
    				throw new BizException(0,0,"1002",message);
    			} 
    			else {
    				service.save(model);
    			}
    		}//判断获取角色表主键，如果主键值不为null,修改的操作 
    		else if ((null != roleCode && !roleCode.equals(""))&& null != adminAuthRole.getId()) {
    			{
    				service.save(model);
    			}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new BizException(0,0,"1002",message);
    	}
    	
    	return new DefaultHttpHeaders("success");
    }
    /***
     * 从现有角色复制到新角色，包括授权菜单、控制点和数据权限
     * ？？数据权限不应该在数据权限功能点复制？？
     */
	public void copyNewRole() {
			
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		String oldRoleCode = request.getParameter("oldRoleCode");// 旧的角色编码
		String newRoleCode = request.getParameter("newRoleCode");// 新的角色编码
		String newRoleName = request.getParameter("newRoleName");// 新的角色名称
		service.copyNewRole(oldRoleCode, newRoleCode, newRoleName);
	}
	/***
	 * 现有角色之间的授权菜单和控制点复制
	 */
	public void copyRoleToRole(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		String oldRoleCode = request.getParameter("oldRoleCode");// 角色复制源
		String toRoleCodes = request.getParameter("toRoleCodes");// 复制到角色IDS
		service.copyRoleToRole(oldRoleCode, toRoleCodes);
	}
	
	/**
	 * 获取所有普通用户角色
	 * @return
	 */
	public String getAuthRoles() {
		try {
			StringBuffer sql = new StringBuffer(" SELECT a.*,'0' AS PARENT_ID FROM ADMIN_AUTH_ROLE A WHERE A.ROLE_TYPE = '"+ SystemUserConstance.NORMAL_MANAGER_ROLE + "'"+ " ORDER BY A.ID");
			log.info("QUERY SQL: " + sql.toString());
			Connection conn = ds.getConnection();
			QueryHelper query;
			query = new QueryHelper(sql.toString(), conn);
			if (!"ID".equals(primaryKey)) {
				query.setPrimaryKey(primaryKey);
			}
			if (this.json != null)
				this.json.clear();
			else
				this.json = new HashMap<String, Object>();
			this.json.put("json", query.getJSON(this.transNames));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(1, 2, "1002", e.getMessage());
		}
		return "success";
	}
}


