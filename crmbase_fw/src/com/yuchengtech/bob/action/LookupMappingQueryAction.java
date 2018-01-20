package com.yuchengtech.bob.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

@ParentPackage("json-default")
@Action(value="/lookupMappingQuery", results={
    @Result(name="success", type="json"),
})

public class LookupMappingQueryAction extends CommonAction{

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	
    public void prepare() {
        StringBuilder sb = new StringBuilder("select t.F_ID,t.F_NAME,t.F_COMMENT,(select count(*) from ocrm_sys_lookup_item t1 where t1.F_LOOKUP_ID= t.F_NAME)as COUNTT from ocrm_sys_lookup t where 1>0");
        for(String key:this.getJson().keySet()){
            if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){
                if(key.equals("F_NAME"))
                    sb.append(" and t."+key+" like '%"+this.getJson().get(key)+"%'");
                else if(key.equals("F_COMMENT"))
                    sb.append(" and t."+key+" like '%"+this.getJson().get(key)+"%'");
                else{
                	sb.append(" and t."+key+" = "+this.getJson().get(key));
                }
            }
        }
        setPrimaryKey("t.F_ID");
        
        SQL=sb.toString();
        datasource = ds;
    }
}
