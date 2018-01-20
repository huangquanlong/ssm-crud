package com.yuchengtech.crm.dataauth.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.vo.AuthUser;

/**
 * @describtion: 客户视图树授权
 *
 * @author : lhqheli (email: lhqheli@gmail.com) chenmeng
 * @date : 2014年7月15日 下午3:32:58
 */
@ParentPackage("json-default")
@Action(value = "/queryCustViewAuthorize", results = { @Result(name = "success", type = "json") })
public class QueryCustViewAuthorizeAction {
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	private HttpServletRequest request;

	private Map<String, Object> JSON;

	public Map<String, Object> getJSON() {
		return JSON;
	}

	public void setJSON(Map<String, Object> jSON) {
		JSON = jSON;
	}
	
    /**
     * 查询所有的授权信息
     * @return
     */
	public String index() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		try {
//			StringBuilder sb = new StringBuilder("select o.* from OCRM_SYS_VIEW_MANAGER o  order by o.orders");
			StringBuilder sb = new StringBuilder("  select o.id ||'_0' as id, o.parentid ||'_0' as parentid, o.name, '0' as nodetype,'' as control_code  from OCRM_SYS_VIEW_MANAGER o" +
					"  union " +
					" select c.id ||'_1' as id, c.manager_id||'_0' as parentid, c.control_name,'1' as nodetype,c.control_code   from OCRM_SYS_VIEW_MANAGER_CONTROL c");		
			setJSON(new QueryHelper(sb.toString(), ds.getConnection()).getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
    /**
     * 查询角色保存授权信息
     * @return
     */
	public String queryAuthorizeData() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		try {
			StringBuilder sb = new StringBuilder("select o.view_id || '_' ||if_flag as view_id from OCRM_SYS_VIEW_USER_RELATION o  where 1=1");
			sb.append(" and role_id='" + request.getParameter("role_id")+"'");
			setJSON(new QueryHelper(sb.toString(), ds.getConnection()).getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
    /**
     * 查询客户视图树形菜单，根据视图类型或客户号
     * 1、零售客户视图，2、对公客户视图，3、客户群视图，4、集团客户视图，5、客户经理视图
     * @return
     */
	public String queryCustViewTree() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String viewType = request.getParameter("viewtype");
		String custId = request.getParameter("custId");
		
		StringBuilder roleIdSb = new StringBuilder("");
		for (int i = 0; i < auth.getAuthorities().size(); i++) {
			if (!"".equals(auth.getAuthorities().get(i).toString()) && auth.getAuthorities().get(i) != null) {
				if (i == 0){
					roleIdSb.append("'"+ auth.getAuthorities().get(i).toString() + "'");
				}else{
					roleIdSb.append(",'"+ auth.getAuthorities().get(i).toString() + "'");
				}
			}
		}

		Map<?, ?> numMap = searchCustViewTree(roleIdSb.toString());
		List<?> numList = (List<?>) numMap.get("data");
		Map<?, ?> numMap2 = (Map<?, ?>) numList.get(0);
		StringBuilder sb = new StringBuilder("");
		try {
			if (!"0".equals(numMap2.get("NUM_ID").toString())) {
				sb.append("select distinct o1.ID,o1.NAME,o1.ADDR,o1.PARENTID,o1.ORDERS,o1.VIEWTYPE from OCRM_SYS_VIEW_MANAGER o1 inner join OCRM_SYS_VIEW_USER_RELATION o2  on  o1.ID=o2.VIEW_ID  where 1=1");

				sb.append(" and o2.ROLE_ID IN (");
				sb.append(roleIdSb.toString());
				sb.append(")");
				if(viewType != null && !"".equals(viewType)){
					sb.append(" and o1.viewtype='" + viewType + "'");
				}
				//传入参数是客户号时，获取客户类型
				if(custId != null && !"".equals(custId)){
					sb.append(" and o1.viewtype = (SELECT case when cust_type = '1' then '2' when cust_type = '2' then  '1' end  FROM ACRM_F_CI_CUSTOMER WHERE CUST_ID = '" + custId + "') ");
				}
				sb.append(" order by o1.orders");
			} else {
				sb.append("select o.ID,o.NAME,o.ADDR,o.PARENTID,o.ORDERS,o.VIEWTYPE from OCRM_SYS_VIEW_MANAGER o where 1=1");
				if(viewType != null && !"".equals(viewType)){
					sb.append(" and o.viewtype='" + viewType + "'");
				}
				//传入参数是客户号时，获取客户类型
				if(custId != null && !"".equals(custId)){
					sb.append(" and o.viewtype = (SELECT case when cust_type = '1' then '2' when cust_type = '2' then  '1' end FROM ACRM_F_CI_CUSTOMER WHERE CUST_ID = '" + custId + "') ");
				}
				sb.append(" order by o.orders");
			}
			setJSON(new QueryHelper(sb.toString(), ds.getConnection()).getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
    * 查询我的视图树形菜单，根据视图类型或客户号
    * 1、零售客户视图，2、对公客户视图，3、客户群视图，4、集团客户视图，5、客户经理视图
    * @return
    */
	public String queryMyViewTree(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String viewType = request.getParameter("viewtype");
		String custId = request.getParameter("custId");
		
		StringBuilder roleIdSb = new StringBuilder("");
		for (int i = 0; i < auth.getAuthorities().size(); i++) {
			if (!"".equals(auth.getAuthorities().get(i).toString()) && auth.getAuthorities().get(i) != null) {
				if (i == 0){
					roleIdSb.append("'"+ auth.getAuthorities().get(i).toString() + "'");
				}else{
					roleIdSb.append(",'"+ auth.getAuthorities().get(i).toString() + "'");
				}
			}
		}

		Map<?, ?> numMap = searchCustViewTree(roleIdSb.toString());
		List<?> numList = (List<?>) numMap.get("data");
		Map<?, ?> numMap2 = (Map<?, ?>) numList.get(0);
		StringBuilder sb = new StringBuilder("");
		try {
			if (!"0".equals(numMap2.get("NUM_ID").toString())) {
				sb.append("SELECT T1.ID,T1.NAME,T1.ADDR,T1.ORDERS,T1.VIEWTYPE,'0' AS PARENTID,t.id as MY_VIEW_ID from ocrm_sys_view_manager_comm t inner join ( ");
				sb.append(" select distinct o1.ID,o1.NAME,o1.ADDR,o1.PARENTID,o1.ORDERS,o1.VIEWTYPE from OCRM_SYS_VIEW_MANAGER o1 inner join OCRM_SYS_VIEW_USER_RELATION o2  on  o1.ID=o2.VIEW_ID  where 1=1");
				sb.append(" and o2.ROLE_ID IN ("+roleIdSb.toString()+")");
				if(viewType != null && !"".equals(viewType)){
					sb.append(" and o1.viewtype='" + viewType + "'");
				}
				//传入参数是客户号时，获取客户类型
				if(custId != null && !"".equals(custId)){
					sb.append(" and o1.viewtype = (SELECT case when cust_type = '1' then '2' when cust_type = '2' then  '1' end FROM ACRM_F_CI_CUSTOMER WHERE CUST_ID = '" + custId + "') ");
				}
				sb.append(") t1 on t1.id = t.view_id ");
				sb.append(" and t.user_id = '"+auth.getUserId()+"'");
				sb.append(" order by t1.orders");
			} else {
				sb.append("SELECT T1.ID,T1.NAME,T1.ADDR,T1.ORDERS,T1.VIEWTYPE,'0' AS PARENTID,t.id as MY_VIEW_ID from ocrm_sys_view_manager_comm t inner join ( ");
				sb.append("select o.ID,o.NAME,o.ADDR,o.PARENTID,o.ORDERS,o.VIEWTYPE from OCRM_SYS_VIEW_MANAGER o where 1=1");
				if(viewType != null && !"".equals(viewType)){
					sb.append(" and o.viewtype='" + viewType + "'");
				}
				//传入参数是客户号时，获取客户类型
				if(custId != null && !"".equals(custId)){
					sb.append(" and o.viewtype = (SELECT case when cust_type = '1' then '2' when cust_type = '2' then  '1' end FROM ACRM_F_CI_CUSTOMER WHERE CUST_ID = '" + custId + "') ");
				}
				sb.append(") t1 on t1.id = t.view_id ");
				sb.append(" and t.user_id = '"+auth.getUserId()+"'");
				sb.append(" order by t1.orders");
			}
			setJSON(new QueryHelper(sb.toString(), ds.getConnection()).getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	
	/**
	 * 查角色是否被授权过
	 * @param useId
	 * @return
	 */
	public Map<String, Object> searchCustViewTree(String useId) {
		String s = " select count(t1.id) as num_id  from OCRM_SYS_VIEW_USER_RELATION t1  where t1.role_id in("+ useId + ")";
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, ds.getConnection());
			return qh.getJSON();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询客户基础信息
	 * @return
	 */
	public String queryCustBase(){
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
			String custId = request.getParameter("custId");
			StringBuilder sb = new StringBuilder("SELECT CUST_ID,CUST_NAME,CUST_TYPE FROM ACRM_F_CI_CUSTOMER WHERE CUST_ID = '"+custId+"'");
			setJSON(new QueryHelper(sb.toString(), ds.getConnection()).getJSON());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * 查询视图基本信息
	 * @return
	 */
	public String queryViewBase(){
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
			String busiId = request.getParameter("busiId");
			String viewType = request.getParameter("viewtype");
			StringBuffer sb = new StringBuffer();
			if("0".equals(viewType)){
				sb.append("SELECT ID,NAME,TYPE FROM ACRM_F_CI_CUSTOMER WHERE CUST_ID = '"+busiId+"'");
			}else if("1".equals(viewType)){
				sb.append("select ID,CUST_BASE_NAME AS NAME,GROUP_TYPE AS TYPE from OCRM_F_CI_BASE WHERE ID = '"+busiId+"'");
			}else if("2".equals(viewType)){
				sb.append("select ID,GROUP_NAME AS NAME,GROUP_TYPE AS TYPE from OCRM_F_CI_GROUP_INFO WHERE ID = '"+busiId+"'");
			}else if("3".equals(viewType)){
				sb.append("select ACCOUNT_NAME AS ID,USER_NAME AS NAME,'' AS TYPE from ADMIN_AUTH_ACCOUNT WHERE ACCOUNT_NAME = '"+busiId+"'");
			}
			setJSON(new QueryHelper(sb.toString(), ds.getConnection()).getJSON());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

}
