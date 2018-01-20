package com.yuchengtech.crm.dataauth.action;


import java.util.HashMap;
import java.util.List;

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
import com.yuchengtech.bob.core.QueryHelper;

/**
 * 模块查询
 * @author GUOCHI
 * @since 2012-10-09
 */


@ParentPackage("json-default")
@Action(value = "/treeGridEditorquery", results = { @Result(name = "success", type = "json") })
public class TreeGridEditorQueryAction extends CommonAction {

    @Autowired
    @Qualifier("dsOracle")
    private DataSource ds;//数据源
    public void prepare(){}
    /**
     * 创建查询SQL并为父类中相应属性赋值
     */
    @SuppressWarnings("unchecked")
    public void getFilters() {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        String cName="";
        String className=null;
        className = request.getParameter("className");
        System.out.println(!className.equals("")&&className!=null);
        if(className!=null)
        {
             cName=" WHERE CLASS_NAME='"+className +"'" ; 
        }
        StringBuilder querySql = new StringBuilder("select ID,DESCRIBETION from AUTH_SYS_FILTER "+cName);//创建查询SQL
        SQL = querySql.toString(); //为父类SQL属性赋值（设置查询SQL）
        try {
            QueryHelper qh = new QueryHelper(SQL.toString(), ds.getConnection());
            json = qh.getJSON();
            ((List)json.get("data")).clear();
            
            HashMap<String, Object> map = new HashMap<String, Object>();
           
            map.put("id", "11");
            map.put("city", "市区");
            map.put("popu", "43000");
            map.put("overlapped", "1");
            map.put("created", "2008-11-07");
            map.put("expanded", "true");
            map.put("checked", "false");
            HashMap<String, Object> map0 = new HashMap<String, Object>();
            map0.put("id", "1");
            map0.put("city", "北京");
            map0.put("popu", "60000");
            map0.put("overlapped", "0");
            map0.put("created", "2008-11-07");
            map0.put("expanded", "true");
            map0.put("checked", "false");
            map0.put("children", map);
            ((List)json.get("data")).add(map0);
           
            
        } catch (Exception e) {}
    }
}
