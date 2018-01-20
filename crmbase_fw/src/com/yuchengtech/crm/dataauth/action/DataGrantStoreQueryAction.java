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
import com.yuchengtech.bob.action.BaseQueryAction;
import com.yuchengtech.bob.core.QueryHelper;

/**
 * 数据权限下拉列表查询
 * @author GUOCHI
 * @since 2012-11-28
 */


@ParentPackage("json-default")
@Action(value = "/datagrantstorequery", results = { @Result(name = "success", type = "json") })
public class DataGrantStoreQueryAction extends BaseQueryAction {

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
        String className = request.getParameter("className");
        StringBuilder querySql = new StringBuilder("select ID,DESCRIBETION from AUTH_SYS_FILTER WHERE CLASS_NAME='"+className+"'" );//创建查询SQL
        SQL = querySql.toString(); //为父类SQL属性赋值（设置查询SQL）
        try {
            QueryHelper qh = new QueryHelper(SQL.toString(), ds.getConnection());
            json = qh.getJSON();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ID", "");
            map.put("DESCRIBETION", "无");
            ((List)json.get("data")).add(map);
            
        } catch (Exception e) {}
    }
}
