package com.yuchengtech.bcrm.system.action;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.service.RoleMenuOptionService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;

/**
 * 
 * @author wz
 * @since 2012-11-25
 */
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value="/menuInitAuthortication", results={
    @Result(name="success", type="json"),
})
public class MenuInitAction extends CommonAction{
	
	private static Logger log = Logger.getLogger(MenuInitAction.class);
	//数据源
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	
	@Autowired
	private RoleMenuOptionService roleMenuOptionService;//资源权限设置service
	
	/**
	 * SQL 脚本构造
	 */
	public void prepare(){//oracle sql 调整适应mysql ，mender：luoyd
		StringBuffer sb = new StringBuffer();
		sb.insert(0, "select * from (SELECT c.id as ID,c.parent_id,'' as icon, '0' as nodetype,'' as OPCODE," +
				//查询真正的叶子菜单
				" (case when (select count(t.id) from CNT_MENU t where t.id in (select parent_id from CNT_MENU) and t.id=c.id) > 0 then 0 else 1 end) " +
				"as leaf_flag," +
				//菜单栏中添加每个菜单拥有的控制点个数统计数
				"concat(c.name, '(',(select count(*) from auth_res_controllers r where r.fw_fun_id = c.mod_func_id),')') as countname " +
				"FROM CNT_MENU C LEFT JOIN FW_FUNCTION F ON C.MOD_FUNC_ID = F.ID WHERE ");
		sb.append(" c.APP_ID = '"+SystemConstance.LOGIC_SYSTEM_APP_ID+"' ORDER BY C.ORDER_ ASC) AS tb1 ");
		//sb.append(" UNION ALL SELECT RC.ID,CM.ID AS PARENT_ID,'/images/fw/icon_menu_326.gif' as icon," +
		sb.append(" UNION ALL SELECT concat('ctr_',RC.ID,CM.ID) AS ID,CM.ID AS PARENT_ID,'/images/fw/icon_menu_326.gif' as icon," +
				  " '1' as nodetype,con_code as OPCODE,0 as leaf_flag,RC.NAME AS COUNTNAME  " +
				  " FROM AUTH_RES_CONTROLLERS RC LEFT JOIN CNT_MENU CM ON RC.FW_FUN_ID=CM.MOD_FUNC_ID WHERE PARENT_ID IS NOT NULL");
		SQL=sb.toString();
		datasource = ds;
	}
	
	/**
	 * 功能授权信息导出
	 */
	public String exportAuthInfo() {
		//String fileName = "授权信息";
		String fileName = "authInfo" + new Date().getTime();
		ActionContext ctx = ActionContext.getContext();
        request           = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        String rolesStr   = request.getParameter("rolesStr");
		try {
			if (rolesStr != null && !"".equals(rolesStr)) {
				roleMenuOptionService.exportAuthInfo(fileName,rolesStr);
				if(this.json!=null)
            		this.json.clear();
            	else 
            		this.json = new HashMap<String,Object>(); 
				this.getJson().put("filename", fileName+".xls");
			}
        }catch(Exception e){
        	e.printStackTrace();
        	throw new BizException(1,2,"1002",e.getMessage());
        }		
		return "success";
	}
}	







