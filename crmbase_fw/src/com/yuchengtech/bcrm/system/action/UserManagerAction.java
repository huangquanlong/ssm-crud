package com.yuchengtech.bcrm.system.action;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AdminAuthAccount;
import com.yuchengtech.bcrm.system.service.UserManagerService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.EndecryptUtils;
import com.yuchengtech.crm.constance.SystemConstance;

/*
 * 用户管理Action,维护用户信息
 * 2014-07-19,增加新增用户登录名重复校验,及去除查询时两次调用查询
 * 
 * @author wangwan
 * @since 2012-10-08
 * 
 */
@SuppressWarnings("serial")
@Action("/userManager")
public class UserManagerAction extends CommonAction {
	
	@Autowired
	private UserManagerService service;//定义UserManagerService属性
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	
	@Autowired
	public void init() {
		model = new AdminAuthAccount();
		setCommonService(service);
		needLog = true;//新增修改删除记录是否记录日志,默认为false，不记录日志
	}

	/**
	 * 用户查询拼装SQL
	 */
	public void prepare() {
		StringBuffer sb = new StringBuffer(
				"SELECT DISTINCT t1.*, t2.ORG_NAME  FROM ADMIN_AUTH_ACCOUNT t1"
						+ " LEFT JOIN ADMIN_AUTH_ORG t2 ON t1.ORG_ID = t2.ORG_ID"
						+ " LEFT JOIN ADMIN_AUTH_ACCOUNT_ROLE t3 ON t1.ID = t3.ACCOUNT_ID"
						+ " WHERE t1.APP_ID = ");
		sb.append(SystemConstance.LOGIC_SYSTEM_APP_ID);

		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key) && !this.getJson().get(key).equals("")) {
				if (null != key && key.equals("ACCOUNT_NAME")) {
					sb.append("  AND (t1.ACCOUNT_NAME like" + " '%" + this.getJson().get(key)
							+ "%') OR (t1.USER_NAME like '%" + this.getJson().get(key) + "%')");
				} else if (key.equals("TREE_STORE")) {
					sb.append("  AND (t1.ORG_ID IN (SELECT UNITID FROM SYS_UNITS WHERE UNITSEQ LIKE (SELECT UNITSEQ FROM SYS_UNITS WHERE UNITID='"
							+ (String) this.getJson().get(key) + "')||'%'))");
				} else if (key.equals("userId")) {
					sb.append("  AND (t1.ID like" + " '%" + this.getJson().get(key) + "%')");
				}
			}
		}

		addOracleLookup("SEX", "DEM0100005");
		addOracleLookup("USER_STATE", "SYS_USER_STATE");
		SQL = sb.toString();
		datasource = ds;
	}

	/**
	 * 新增用户基本信息方法
	 * 
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	public String save() throws ParseException {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		if (!"".equals(((AdminAuthAccount) model).getId()) && ((AdminAuthAccount) model).getId() != null) {
			// 修改入口
			service.save(model);
		} else {
			// 新增入口
			AdminAuthAccount aaa = service.saveBaseInfo((AdminAuthAccount)model);
			AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			auth.setPid(aaa.getId().toString());
		}
		return "success";
	}
	
	
	/**
	 * 修改密码方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateNew() throws Exception {
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
			long idStr = Long.parseLong((request.getParameter("idStr")));// 获取用户信息主键ID
			String key = request.getParameter("password");// 获取用户密码
			String password = EndecryptUtils.encrypt(key);
			String jql = "update  AdminAuthAccount a set a.password ='"+ password + "'" + " where a.id in (" + idStr + ")";
			Map<String, Object> values = new HashMap<String, Object>();
			super.executeUpdate(jql, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 修改启用停用状态方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateState() throws Exception {
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
			String idStr = request.getParameter("idStr");// 获取用户信息主键ID
			String state = request.getParameter("userState");// 获取用户状态
			String jql = "update  AdminAuthAccount a set a.userState ='"+ state + "'" + " where a.id in (" + idStr + ")";
			Map<String, Object> values = new HashMap<String, Object>();
			super.executeUpdate(jql, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * （自定义）批量删除，根据前台传递的idStr删除相应用户信息
	 * 
	 * @return 成功标识
	 */
	public String batchDestroy() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		//List list = service.getEntityManager().createNativeQuery("SELECT DISTINCT 1 FROM OCRM_F_CI_BELONG_CUSTMGR MGR WHERE exists (select 1 from admin_auth_account a where a.account_name = mgr.mgr_id and a.id in ("+idStr+"))").getResultList();
		//if(list != null && list.size() > 0){
			//throw new BizException(1,0,"","待删除用户名下还有客户,请先将其客户移交出去！");
		//}
		String jql = "DELETE FROM AdminAuthAccount C WHERE C.id IN (" + idStr+ ")";
		Map<String, Object> values = new HashMap<String, Object>();
		service.batchUpdateByName(jql, values);
		addActionMessage("batch removed successfully");
		return "success";
	}
}


