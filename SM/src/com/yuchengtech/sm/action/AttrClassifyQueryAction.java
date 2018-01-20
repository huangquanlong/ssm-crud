package com.yuchengtech.sm.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

@ParentPackage("json-default")
@Action(value = "attrClassifyQuery", results = { @Result(name = "success", type = "json"), })
public class AttrClassifyQueryAction extends CommonAction{

	private static final long serialVersionUID = 544881968842152835L;
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	
	public void prepare() {
        StringBuilder sb = new StringBuilder("select t.id, t.name,t.comm,t.crt_by,t.crt_time,t.upd_by,t.upd_time from sm_attr_classify t where 1>0");
        for(String key:this.getJson().keySet()){
            if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){
                if(key.equals("NAME"))
                    sb.append(" and t."+key+" like '%"+this.getJson().get(key)+"%'");
                else if(key.equals("COMM"))
                    sb.append(" and t."+key+" like '%"+this.getJson().get(key)+"%'");
                else{
                	sb.append(" and t."+key+" = "+this.getJson().get(key));
                }
            }
        }
        setPrimaryKey("t.ID");
        
        SQL=sb.toString();
        datasource = ds;
    }
	
	public String addAttrClassify() {
		return "success";
	}
}
