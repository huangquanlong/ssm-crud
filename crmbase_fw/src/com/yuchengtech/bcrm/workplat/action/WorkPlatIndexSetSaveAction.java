package com.yuchengtech.bcrm.workplat.action;

import java.sql.Connection;
import java.sql.ResultSet;
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

import com.yuchengtech.bcrm.workplat.service.IndexSetService;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value="indexset", results={
    @Result(name="success", type="json"),
})
public class WorkPlatIndexSetSaveAction{
    
    String layoutId = "";
    String modules = "";
    JSONArray ja;
    Map<String,Object> returns = new HashMap<String,Object>();
    
    @Autowired
    IndexSetService iss;
       
    @Autowired
    @Qualifier("dsOracle")
    private DataSource dsOracle;
    
    public String index() {
    	Connection conn=null;
        try {
            
            AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
            String userId = auth.getUserId();             
                       
            conn = dsOracle.getConnection();
            String sqlLayout = "SELECT LAYOUT_ID FROM OCRM_F_WP_USER_LAYOUT WHERE USER_ID='"+userId+"'";
            
            QueryHelper qhLayout = new QueryHelper(sqlLayout, conn);
            ResultSet rs = qhLayout.executeQuery();
            if(rs.next()){
            	String tmpLayoutId = rs.getString("LAYOUT_ID");
            	
            	String sqlModule = "SELECT MODULE_ID,COLUMN_SEQ,MODULE_SEQ FROM OCRM_F_WP_USER_MODULE WHERE USER_ID='"+userId+"'";
            	QueryHelper qhModule = new QueryHelper(sqlModule, conn);
            	returns = (Map<String,Object>)qhModule.getJSON();
            	returns.put("layoutId", tmpLayoutId);
            	
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
         		try {
					if(conn!=null&&!conn.isClosed())
						conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	return "success";
        }        
    }
    
    public HttpHeaders create() {
        iss.commitUserSet(ja, layoutId);
        return new DefaultHttpHeaders("success").setLocationId("");
     }

    public void setLayoutId(String layoutId) {
        this.layoutId = layoutId;
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
