package com.yuchengtech.bob.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.constance.SystemConstance;

@ParentPackage("json-default")
@Action(value="/orgusermanage", results={
    @Result(name="success", type="json"),
})
/**
 * 
 * @修改记录：wzy，20150129，modify：将oracle的“||”改成mysql的“concat”
 *            wzy，20150215，modify：将“用户名/登录名”查询条件改成模糊查询，增加查询所有系统用户的判断逻辑
 *
 */
public class OrgUserManageAction  extends CommonAction {
	
	@Autowired
	@Qualifier("dsOracle")	
	private DataSource ds;
	
	private HttpServletRequest request;
	
    public void prepare () {
    		
    	ActionContext ctx = ActionContext.getContext();
    	request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);

    	String role_id = request.getParameter("role_id");
    	String org_id = request.getParameter("org_id");
    	String queryAllUser = request.getParameter("queryAllUser");//是否查询所有用户
    	StringBuffer sb = new StringBuffer("SELECT DISTINCT t3.ID,t2.ORG_ID,t3.USER_NAME,t3.USER_CODE,t2.ORG_NAME,t3.ACCOUNT_NAME "+ 
	    	"FROM ADMIN_AUTH_ACCOUNT t3 "+
			"left join ADMIN_AUTH_ACCOUNT_ROLE t4 on t3.ID = t4.ACCOUNT_ID "+
			"left join ADMIN_AUTH_ORG t2 on t3.ORG_ID = t2.ORG_ID "+
			"left join ADMIN_AUTH_ROLE t1 on t4.ROLE_ID = t1.ID WHERE 1=1 ");
    	if("false".equals(queryAllUser)){//如果是查询所有系统用户，不加下面的机构和角色限制条件
        	if("DB2".equals(SystemConstance.DB_TYPE)){
    			sb.append(" AND   (t2.ORG_ID IN  (   SELECT a.UNITID FROM SYS_UNITS a,SYS_UNITS b WHERE  b.UNITID='"+org_id+"' and locate(b.UNITSEQ,a.UNITSEQ)>0))");
    		}else{
    			sb.append(" and (t2.ORG_ID IN (SELECT UNITID FROM SYS_UNITS WHERE UNITSEQ LIKE concat((SELECT UNITSEQ FROM SYS_UNITS WHERE UNITID='"+
    				org_id+"'),'%')))");
    		}
        	if(!role_id.equals("")){
        		sb.append("and t4.ROLE_ID in ("+ role_id+")");
        	}
    	}
    	
    	for(String key : this.getJson().keySet()){
    		if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){
				if(null!=key&&key.equals("USER_NAME")){
					//查询条件中的：登录名/用户名
					sb.append("  AND (t3.USER_NAME like '%"+this.getJson().get(key)+"%' OR t3.ACCOUNT_NAME like '%"+this.getJson().get(key)+"%')");
				}else if (key.equals("ROLE_ID")){
					//查询条件中的：用户角色
					sb.append("  AND (t4.ROLE_ID in ( "+this.getJson().get(key)+"))");
				}
				if (key.equals("TREE_STORE")){
					//查询条件中的：左侧组织机构树
					if("DB2".equals(SystemConstance.DB_TYPE)){
						sb.append(" AND   (t2.ORG_ID IN  (   SELECT a.UNITID FROM SYS_UNITS a,SYS_UNITS b WHERE  b.UNITID='"+(String)this.getJson().get(key)+"' and locate(b.UNITSEQ,a.UNITSEQ)>0))");
					}else{
						sb.append(" and (t2.ORG_ID IN (SELECT UNITID FROM SYS_UNITS WHERE UNITSEQ LIKE concat((SELECT UNITSEQ FROM SYS_UNITS WHERE UNITID='"+
							(String)this.getJson().get(key)+"'),'%')))");
					}
				}
				if("false".equals(queryAllUser)){//如果是查询所有系统用户，不加下面的机构和角色限制条件
					if (key.equals("ROLE_ID2")){
						sb.append("  AND (t4.ROLE_ID in ( "+this.getJson().get(key)+"))");
					}
					else if (key.equals("searchForRoleType")){
						sb.append("  AND (t4.ROLE_ID in ( "+this.getJson().get(key)+"))");
					}
					else if (key.equals("ORG_ID")){
						sb.append("  AND (t2.ORG_ID in ( '"+this.getJson().get(key)+"'))");
					}
				}
    		}
    	}
    	SQL=sb.toString();
		datasource = ds;
    }
    
}
