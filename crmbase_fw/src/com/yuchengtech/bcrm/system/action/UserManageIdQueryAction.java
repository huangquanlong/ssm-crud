package com.yuchengtech.bcrm.system.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.core.QueryHelper;

/*
 * 用户ID查询Action
 * @author wangwan
 * @since 2012-10-08
 */
@ParentPackage("json-default")
@Action(value="/usermanageidquery", results={
    @Result(name="success", type="json"),
})
public class UserManageIdQueryAction  extends CommonAction {
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;  
	public void prepare()  {}
    public void userlist () {
        	StringBuffer sb = new StringBuffer("SELECT t1.ACCOUNT_NAME,t1.USER_NAME,t1.SEX,t1.BIRTHDAY,t1.EMAIL,t1.OFFICETEL,t1.MOBILEPHONE,t1.USER_STATE,t1.PASSWORD,t1.ORG_ID,t2.ORG_NAME FROM ADMIN_AUTH_ACCOUNT t1 left join ADMIN_AUTH_ORG t2 on t1.ORG_ID = t2.ORG_ID WHERE 1=1");
		
        	for(String key : this.getJson().keySet()){
        		if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){	
					if(null!=key&&key.equals("userName")){
						sb.append("  AND (t1.ACCOUNT_NAME like '"+this.getJson().get(key)+"')");
					}
        		}
			}
        	SQL=sb.toString();
        	
        	try{
        		json=new QueryHelper(SQL, ds.getConnection()).getJSON();
        	}catch(Exception e){}
    }
    

    
}
