package com.yuchengtech.sm.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
@SuppressWarnings("serial")
@Action("/typeWarrantDefinitionQuery")
public class TypeWarrantDefinitionQueryAction extends CommonAction {
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性

	/**
	 * 角色授权用户信息查询拼装SQL
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		String typeId = request.getParameter("typeId");
		
		//查询出角色授权用户信息
		StringBuilder sb = new StringBuilder("SELECT sr.`id`, sa.`name`,sa.`comm` FROM sm_type_attr_rel sr,"
								+ " sm_attr_definition sa,sm_type_definition st "
								+" WHERE sr.`attr_id`=sa.`id` AND sr.`type_id`=st.`id` ");
        if(typeId!=null){
        	if(typeId.length()>0){
        		sb.append(" AND  st.`id` = "+typeId);
        	}
        }
        SQL = sb.toString();
        this.setPrimaryKey("sr.id");
        datasource=ds;
	}
}
