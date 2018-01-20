package com.yuchengtech.crm.dataauth.action;


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

/**
 * 数据授权查询
 * @author GUOCHI
 * @since 2012-11-18
 */
@ParentPackage("json-default")
@Action(value = "/datagrantquery", results = { @Result(name = "success", type = "json") })
public class DataGrantQueryAction extends CommonAction {

    @Autowired
    @Qualifier("dsOracle")
    private DataSource ds;//数据源

    /**
     * 创建查询SQL并为父类中相应属性赋值
     */
    public void prepare() {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        String roleId = request.getParameter("roleId");
        StringBuilder querySql = new StringBuilder("SELECT AID, " +//创建查询SQL
        		" CLASS_DESC, " +
        		" M.CLASS_NAME, " +
        		" FID, " +
        		" DESCRIBETION " +
        		" FROM AUTH_SYS_FILTER_MAP M " +
        		" LEFT JOIN (SELECT F.ID AS FID,A.ID AS AID, F.DESCRIBETION,CLASS_NAME " +
        		" FROM AUTH_SYS_FILTER F " +
        		" LEFT JOIN AUTH_SYS_FILTER_AUTH A " +
        		" ON F.ID = A.FILTER_ID WHERE A.ROLE_ID = '"+roleId +"') FF " +
        		" ON FF.CLASS_NAME = M.CLASS_NAME ");
        SQL = querySql.toString(); //为父类SQL属性赋值（设置查询SQL）
        setPrimaryKey("CLASS_DESC"); //设置查询排序条件
        datasource = ds; //为父类数据源赋值
    }
}
