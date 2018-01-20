package com.yuchengtech.bcrm.common.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;


@ParentPackage("json-default")
@Action(value="queryanna", results={
    @Result(name="success", type="json"),
})
public class WorkPlatAnnaexeQueryAction extends CommonAction{

    @Autowired
    @Qualifier("dsOracle")
    private DataSource ds;
    
    @Override
    public void prepare() {
        // TODO Auto-generated method stub
		String querySql = "SELECT ANNEXE_ID,ANNEXE_NAME,ANNEXE_SER_NAME,ANNEXE_SIZE,ANNEXE_TYPE,CLIENT_NAME,CREATE_TIME,LAST_LOAD_TIME,LAST_LOADER,LOAD_COUNT,PHYSICAL_ADDRESS,RELATION_INFO,RELATIOIN_MOD"
				+ " FROM OCRM_F_WP_ANNEXE WHERE 1=1 ";
        for(String key : this.getJson().keySet()){
            if(key.equals("relationInfo")){
                querySql += " AND RELATION_INFO='"+this.getJson().get(key)+"' ";
                continue;
            }else if(key.equals("relationMod")){
                querySql += " AND RELATIOIN_MOD='"+this.getJson().get(key)+"' ";
                continue;
			} else {
				continue;
			}
        }
        SQL = querySql;
        setPrimaryKey("ANNEXE_ID");
        datasource = ds;
        setLimit(19870130);
    }
}
