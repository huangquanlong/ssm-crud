package com.yuchengtech.bcrm.system.action;

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
 * 关联左表查询
 * @author songxs
 * @since 2012-12-6
 */
@ParentPackage("json-default")
@Action(value="/queryJoinLeftTable", results={
    @Result(name="success", type="json")
})
public class QueryJoinLeftTableAction extends CommonAction{
    
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	protected HttpServletRequest request;	
	

    @Override
    public void prepare() {
    	/***查询type为表的数据*/
    	ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        String type = request.getParameter("type");
        StringBuffer builder = new StringBuffer("SELECT C.ID AS VALUE,(C.VALUE||C.NAME) AS NAME FROM MTOOL_DBTABLE C WHERE TYPE = 1 ");
        if(type!=null&&type.equals("1")){
        	builder.append("  and VALUE='ACRM_F_CI_CUSTOMER'");
        }
       
        setPrimaryKey("C.ID");
        // 设置父类中的信息
        SQL = builder.toString();
        datasource = ds;
    }
    
}
