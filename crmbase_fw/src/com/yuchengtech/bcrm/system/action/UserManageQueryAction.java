package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.common.DataType;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.SystemConstance;

/*
 * 用户信息查询Aciton
 * @author wangwan
 * @since 2012-10-08
 */
@ParentPackage("json-default")
@Action(value="/usermanagequery", results={
    @Result(name="success", type="json"),
})
public class UserManageQueryAction  extends CommonAction {
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;  
	private HttpServletRequest request;
    public void prepare () {

    	AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currendOrgId = auth.getUnitId();
        	StringBuffer sb = new StringBuffer("SELECT DISTINCT t1.*, t2.ORG_NAME,t4.DPT_NAME FROM ADMIN_AUTH_ACCOUNT t1"+
        	         " LEFT JOIN ADMIN_AUTH_ORG t2 ON t1.ORG_ID = t2.ORG_ID"+
        	         " LEFT JOIN ADMIN_AUTH_DPT t4 ON t4.dpt_id=t1.dir_id"+
        	         " LEFT JOIN ADMIN_AUTH_ACCOUNT_ROLE t3 ON t1.ID = t3.ACCOUNT_ID"+
        				" WHERE t1.APP_ID = ");
    		sb.append(SystemConstance.LOGIC_SYSTEM_APP_ID);
        	for(String key : this.getJson().keySet()){
        		if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){	
					if(null!=key&&key.equals("ACCOUNT_NAME")){
						sb.append("  AND (t1.ACCOUNT_NAME like"+" '%"+this.getJson().get(key)+"%' OR t1.USER_NAME like '%"+this.getJson().get(key)+"%')");
					}
					else if (key.equals("TREE_STORE")){
						if("DB2".equals(SystemConstance.DB_TYPE)){
							sb.append(" AND   (t1.ORG_ID IN (   SELECT a.UNITID FROM SYS_UNITS a,SYS_UNITS b WHERE  b.UNITID='"+(String)this.getJson().get(key)+"' and locate(b.UNITSEQ,a.UNITSEQ)>0))");
						}else{
						sb.append("  AND (t1.ORG_ID IN (SELECT UNITID FROM SYS_UNITS WHERE UNITSEQ LIKE (SELECT UNITSEQ FROM SYS_UNITS WHERE UNITID='"+
								(String)this.getJson().get(key)+"')||'%'))");
						}
					}	else if (key.equals("userId")){
						sb.append("  AND (t1.ID like"+" '%"+this.getJson().get(key)+"%')");
					}
        		}
			}
      
        	setBranchFileldName("t1.ORG_ID");
            addOracleLookup("SEX","DEM0100005");
            addOracleLookup("USER_STATE","SYS_USER_STATE");
        	SQL=sb.toString();
        	datasource = ds;
       		configCondition("t1.EMAIL","=","EMAIL",DataType.String);
       	 	configCondition("t1.ACCOUNT_NAME","like","ACCOUNT_NAME",DataType.String);
    		configCondition("t1.USER_NAME","like","USER_NAME",DataType.String);
    	   	configCondition("t1.ORG_ID","=","ORG_ID",DataType.String);
    	   	configCondition("t2.ORG_NAME","=","ORG_NAME",DataType.String);
    		configCondition("t1.BIRTHDAY","=","BIRTHDAY",DataType.Date);
    	   	configCondition("t1.DEADLINE","=","DEADLINE",DataType.Date);
    		configCondition("t1.MOBILEPHONE","=","MOBILEPHONE",DataType.String);
    	   	configCondition("t1.OFFICETEL","=","OFFICETEL",DataType.String);
     		configCondition("t1.SEX","=","SEX",DataType.String);
    		configCondition("t1.USER_STATE","=","USER_STATE",DataType.String);
    	   	configCondition("t1.OFFENIP","=","OFFENIP",DataType.String);
    		configCondition("t1.DIR_ID","=","DIR_ID",DataType.String);
        	try{
        		json=new QueryHelper(SQL, ds.getConnection()).getJSON();
     
        	}catch(Exception e){}
       
    }
    

    
}
