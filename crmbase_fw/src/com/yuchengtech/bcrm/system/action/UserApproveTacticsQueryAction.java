package com.yuchengtech.bcrm.system.action;

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
@ParentPackage("json-default")
@Action(value="/userApproveTacticsQuery", results={
    @Result(name="success", type="json"),
})

public class UserApproveTacticsQueryAction extends CommonAction{

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	
    public void prepare() {
    	ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
        StringBuilder sb = new StringBuilder("SELECT T.ID, T.NAME, T.ENABLE_FLAG, T.DETAIL, T.ACTIONTYPE FROM OCRM_F_SYS_CREDENTIAL_STRATEGY T");
//        for(String key:this.getJson().keySet()){
//            if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){
//                if(key.equals("F_NAME"))
//                    sb.append(" and t."+key+" like '%"+this.getJson().get(key)+"%'");
//                else if(key.equals("F_COMMENT"))
//                    sb.append(" and t."+key+" like '%"+this.getJson().get(key)+"%'");
//                else{
//                	sb.append(" and t."+key+" = "+this.getJson().get(key));
//                }
//            }
//        }
        String tem1 = request.getParameter("tem1");
        String tem2 = request.getParameter("tem2");
		if (tem2 != null) {//判断
			sb.append(" WHERE T.ID BETWEEN "+tem1+" AND "+tem2+"");
		}
        setPrimaryKey("T.ID");
        
        SQL=sb.toString();
        datasource = ds;
    }
}
