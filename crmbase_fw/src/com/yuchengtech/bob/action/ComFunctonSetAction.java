package com.yuchengtech.bob.action;
/**
 * @author wangwan
 * @since 20120924
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.service.ComFunctionSetService;
import com.yuchengtech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value="comfunctionset", results={
    @Result(name="success", type="json")
})
public class ComFunctonSetAction{
    
    String modules = "";
    JSONArray ja;
    Map<String,Object> returns = new HashMap<String,Object>();
    
    @Autowired
    ComFunctionSetService iss;
       
    @Autowired
    @Qualifier("dsOracle")
    private DataSource dsOracle;
    
    public String index() {
        try {
            
            AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
            String userId = auth.getUserId();             
                       
            Connection conn = dsOracle.getConnection();
            String sqlModule = "SELECT MODULE_ID,MODULE_SEQ,ICON_URL FROM OCRM_F_WP_MODULE WHERE USER_ID='"+userId+"' ORDER BY MODULE_SEQ ASC" ;
            QueryHelper qhModule = new QueryHelper(sqlModule, conn); 
            returns = (Map<String,Object>)qhModule.getJSON();
           
        } catch (SQLException e) {
            e.printStackTrace();
        }        
        return "success";
    }
    
    public HttpHeaders create() {
     iss.commitUserSet(ja);
        return new DefaultHttpHeaders("success").setLocationId("");
     }
    
    public void setModules(String modules) {
        JSONObject jb = JSONObject.fromObject(modules);
        this.ja = jb.getJSONArray("modules");
        this.modules = modules;
    }

    public Map<String, Object> getReturns() {
        return returns;
    }
    
}
