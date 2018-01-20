package com.yuchengtech.bob.action;


import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;


@ParentPackage("json-default")
@Action(value="/lookupMappingItemQuery", results={
    @Result(name="success", type="json"),
})
public class LookupMappingItemQueryAction extends CommonAction{
    
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	
    public void prepare() {
        StringBuilder sb = new StringBuilder("select F_ID,F_VALUE,F_CODE,F_COMMENT,F_LOOKUP_ID  from ocrm_sys_lookup_item where 1>0");
        for(String key:this.getJson().keySet()){
            if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){
                if(key.equals("F_VALUE"))
                    sb.append(" and "+key+" like '%"+this.getJson().get(key)+"%'");
                else if(key.equals("F_CODE"))
                    sb.append(" and "+key+" like '%"+this.getJson().get(key)+"%'");
                else if(key.equals("F_COMMENT"))
                    sb.append(" and "+key+" like '%"+this.getJson().get(key)+"%'");
                else if(key.equals("F_LOOKUP_ID"))
                    sb.append(" and "+key+" like '%"+this.getJson().get(key)+"%'");
                else{
                	sb.append(" and "+key+" = "+this.getJson().get(key));
                }
            }
        }
        setPrimaryKey("F_ID");
        
        SQL=sb.toString();
        datasource = ds;
    }
}
