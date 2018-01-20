package com.yuchengtech.bcrm.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AdminAuthOrg;
import com.yuchengtech.bcrm.system.service.RoleManagerService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.exception.BizException;

/**
 * 角色管理Action,查询机构信息表
 * @author weilh
 * @since 2012-09-25 
 */

@SuppressWarnings("serial")
@Action("/roleManager")
public class RoleManagerAction extends CommonAction {

	@Autowired
	private RoleManagerService roleManagerService;//定义RoleManagerService属性
	
	@Autowired
	public void init() {
		model = new AdminAuthOrg();
		setCommonService(roleManagerService);
		needLog = true;// 新增修改删除记录是否记录日志,默认为false，不记录日志
	}
	
	/**
	 * 查询机构信息拼装SQL
	 * @exception BizException
	 */
	public HttpHeaders indexPage() throws BizException {
		try {
			StringBuilder sb = new StringBuilder("SELECT c FROM AdminAuthOrg c WHERE 1=1 ");
			Map<String, Object> values = new HashMap<String, Object>();
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			this.setJson(request.getParameter("condition"));
			// 获取机构号
			if (request.getParameter("orgId") != null) {
				sb.append(" AND c.orgId = " + Long.parseLong(request.getParameter("orgId")));
			}
			for (String key : this.getJson().keySet()) {
				if (null != this.getJson().get(key)&& !this.getJson().get(key).equals("")) {
					sb.append(" AND c." + key + " = :" + key);
					values.put(key, this.getJson().get(key));
				}
			}
			return super.indexPageByJql(sb.toString(), values);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(1,2,"1002",e.getMessage());
		}
	}
}
