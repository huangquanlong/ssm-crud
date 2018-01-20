package com.yuchengtech.bcrm.system.action;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 角色信息查询Action
 * @author wangwan
 * @since 2012-10-08 
 */

@SuppressWarnings("serial")
@Action("/roleQuery")
public class RoleQueryAction extends CommonAction {
	
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	
	/**
	 * 角色查询拼装SQL
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		
		//查询出角色信息
		StringBuilder sb = new StringBuilder( " select R.*,R.ID AS ROLE_ID from ADMIN_AUTH_ROLE R WHERE 1=1 " );
		
        for (String key : this.getJson().keySet()) {//查询条件判断
            if (null != this.getJson().get(key)&& !this.getJson().get(key).equals("")) {//查询条件判断
                if (null != this.getJson().get(key)&& !this.getJson().get(key).equals("")) {
                    if (key.equals("ROLE_NAME")){//判断查询条件是否为角色名称
                    	sb.append(" AND R."+key+" LIKE"+" '%"+this.getJson().get(key)+"%'");
                    }else if (key.equals("ROLE_TYPE")&& !this.getJson().get(key).equals("")){//判断查询条件是否为角色类型
                		sb.append(" AND R."+key+" ='"+this.getJson().get(key)+"'");
                    }else if(key.equals("id")){ //获得id，用于查看、修改
                		sb.append(" AND R."+key+" ="+this.getJson().get(key));
                	}	else if (key.equals("searchForRoleType")){
    					sb.append("  AND R.ID in ( "+this.getJson().get(key)+")");
    				}
                }
            }
        }
        addOracleLookup("ROLE_TYPE","ROLE_TYPE");
        addOracleLookup("ROLE_LEVEL","ROLE_LEVEL");
        setPrimaryKey("R.ID");
        SQL = sb.toString();
        datasource=ds;
	}
}


