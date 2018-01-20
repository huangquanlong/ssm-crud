package com.yuchengtech.bcrm.common.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.common.service.OrgSearchService;
import com.yuchengtech.bcrm.common.service.SecGrantService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.dataauth.managerment.DataAuthManager;
import com.yuchengtech.crm.dataauth.service.DataAuthInfo;
import com.yuchengtech.crm.sec.common.SecLoaderManager;
/**
 * 初始化权限信息
 * @author changzh
 * @since 2012-12-04
 */
@ParentPackage("json-default")
@Action(value="/initMenuGrant", results={
    @Result(name="success", type="json"),
})
public class InitMenuGrantAction extends CommonAction{
	
	private static final long serialVersionUID = -4214741989946063675L;

	@Autowired
	private OrgSearchService orgSearchService;
	
	@Autowired
	private DataAuthInfo dataAuthInfo;
	
	@Autowired
	private SecGrantService secGrantService;

	@SuppressWarnings("unchecked")
	public String index(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String roleIds = request.getParameter("roleIds");
		
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<GrantedAuthority> authorities = new ArrayList();
		if (roleIds != null) {
			String[] arrayRoleIds = roleIds.split(",");
			for (String roleId : arrayRoleIds) {
				authorities.add(new GrantedAuthorityImpl(roleId));
			}
		}
				
		auth.setAuthorities(authorities);
		
		auth.setRolesInfo(orgSearchService.getRoleInfo(auth.getAuthorities()));
		auth.setGrant(secGrantService.getGrantMenus("MENU_RES",auth));
		auth.setAuthInfos(dataAuthInfo.LoadAuthInfo(auth));
		auth.setDataAuthInfo(DataAuthManager.getInstance().getDataAuthInfo(auth));
		/** 用户动态设置session级别信息加载*/
		SecLoaderManager.getInstance().initialize();
		
		return "success";
	}
	
}
