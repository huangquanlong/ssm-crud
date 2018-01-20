package com.yuchengtech.bcrm.system.action;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AdminAuthAccount;
import com.yuchengtech.bcrm.system.service.ChangePasswordService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.constance.EndecryptUtils;
import com.yuchengtech.crm.exception.BizException;

/**
 * 个人密码修改Action,查询及修改密码
 * @author wangwan
 * @since 2012-10-23
 */

@SuppressWarnings("serial")
@Action("/changePassword")
public class ChangePasswordAction extends CommonAction {
	
	@Autowired
	private ChangePasswordService service;//定义ChangePasswordService属性
	
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
	 * 用户密码信息查询拼装SQL
	 */
	public void prepare() {

		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		String userId = request.getParameter("userId");//获取当前登录用户名
		
		//查询出当前登录用户密码等信息
		StringBuilder sb = new StringBuilder( " select R.ID,R.PASSWORD from ADMIN_AUTH_ACCOUNT R" );
		
		if (userId != null) {//判断，根据当前登录用户，查询出对应用户密码,以及数据库中该用户对应数据的主键ID
			sb.append(" WHERE R.ACCOUNT_NAME = '"+userId+"'");
		}
        addOracleLookup("ROLE_TYPE","ROLE_TYPE");
        addOracleLookup("ROLE_LEVEL","ROLE_LEVEL");
        
        SQL = sb.toString();
//        this.setPrimaryKey("t0.ID");
        datasource=ds;
	
	}
	
	public String updateNew() throws Exception{
		String message = "";//抛出异常信息
		try{
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			long id= Long.parseLong((request.getParameter("id")));//获取原数据主键ID
			String key = request.getParameter("newPassword");  //获取新密码
			String oldPassword = request.getParameter("oldPassword");//获取原密码
			String oldPassword1 = EndecryptUtils.encrypt(oldPassword);
			String oldPassword2 = request.getParameter("oldPassword2");
			String password=EndecryptUtils.encrypt(key);
			String jql = "update  AdminAuthAccount a set a.password ='"+password+"'" +
					" where a.id in ("+id+")";
			Map<String,Object> values = new HashMap<String,Object>();
			if(oldPassword1.equals(oldPassword2)){//判断页面输入的原密码是否正确
				super.executeUpdate(jql, values);
			}else{
				message = "原密码输入有误，请重新输入!"; 
				throw new BizException(1,0,"0001",message);
			};
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "success";
	}
}


