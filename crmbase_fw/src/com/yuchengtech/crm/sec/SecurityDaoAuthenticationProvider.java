package com.yuchengtech.crm.sec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.yuchengtech.bcrm.common.service.OrgSearchService;
import com.yuchengtech.bcrm.common.service.SecGrantService;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.OperateTypeConstant;
import com.yuchengtech.crm.dataauth.managerment.DataAuthManager;
import com.yuchengtech.crm.dataauth.service.DataAuthInfo;
import com.yuchengtech.crm.sec.common.SecLoaderManager;
import com.yuchengtech.crm.sec.common.SystemUserConstance;
import com.yuchengtech.crm.sec.credentialstrategy.CredentialStrategy;
import com.yuchengtech.crm.sec.ctxsession.ICtxSessionManager;
/**
 * 扩展DaoAuthenticationProvider 实现认证具体功能
 * 可扩展此类或实现方法beforChecks、 afterChecks
 * @author changzh@yuchengtech.com
 * @date 2012-11-05
 * 
 **/
public class SecurityDaoAuthenticationProvider extends DaoAuthenticationProvider { 
	
	private static Logger log = Logger.getLogger(SecurityDaoAuthenticationProvider.class);
	
	@Autowired
	private OrgSearchService orgSearchService;
	
	@Autowired
	private DataAuthInfo dataAuthInfo;
	
	@Autowired
	private SecGrantService secGrantService;

	private List<CredentialStrategy>  credentialStrategy;
	
	private List<ICtxSessionManager>  ctxSessionManager;
	
	SecurityDaoAuthenticationProvider () {
		super();
	}
	
	protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		try {
			/**check前方法*/
			beforChecks(userDetails, authentication);
			SecLoaderManager.getInstance().setPasswordEncoder(getPasswordEncoder());
			SecLoaderManager.getInstance().setSaltSource(getSaltSource());
			/**执行认证策略方法*/
			doCredentialStrategy(userDetails, authentication);
			/**check后方法*/
			afterChecks(userDetails, authentication);
		} catch (AuthenticationException authException) {
			secGrantService.addLoginExceptionInfo((AuthUser) userDetails, authException);
			log.info(OperateTypeConstant.getOperateText(OperateTypeConstant.LOGIN_SYS)+":["+userDetails.getUsername()+"]"+authException.getMessage());
			throw authException;
		} 
    }
	
	private void doCredentialStrategy (UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) {
 
		boolean isAuthenticationChecked = true;
		WebAuthenticationDetails  webAuthenDetail =  (WebAuthenticationDetails) authentication.getDetails();
		String CurrentIP = webAuthenDetail.getRemoteAddress();
		AuthUser authUser = (AuthUser) userDetails;
		authUser.setCurrentIP(CurrentIP);
		secGrantService.initCreStrategy(getCredentialStrategy());
		try {
			super.additionalAuthenticationChecks(userDetails, authentication); 
			
		} catch (AuthenticationException e) {
			isAuthenticationChecked = false;
			//e.printStackTrace();
			throw e;
		} 
		//自定义认证策略类
		if (getCredentialStrategy() != null) {
			for (CredentialStrategy cs : getCredentialStrategy()) {
				if (cs.enable) {
					if (cs.doCredentialStrategy(authUser, isAuthenticationChecked))
						break;
				}
			}
		}
		
		/**用户信息初始化(菜单及权限等)*/
		initUserInfo(authUser);
	}
	/**check之前方法*/
	public void beforChecks (UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException  {
		//TODO 待扩展check之前方法
	}
	/**check之后方法*/
	public void afterChecks (UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException  {
		/**可增加check后方法，但此时[SecurityContextHolder.getContext().getAuthentication().getPrincipal()]不可用*/
	}
	
	public void setCredentialStrategy(List<CredentialStrategy> credentialStrategy) {
		this.credentialStrategy = credentialStrategy;
	}

	public List<CredentialStrategy> getCredentialStrategy() {
		return credentialStrategy;
	}
	
	public List<ICtxSessionManager> getCtxSessionManager() {
		return ctxSessionManager;
	}
	
	public void setCtxSessionManager(List<ICtxSessionManager> ctxSessionManager) {
		this.ctxSessionManager = ctxSessionManager;
	}
	
	/**初始化用户信息 */
	@SuppressWarnings("unchecked")
	public void initUserInfo(AuthUser iaUser) {
		
		try {
			/**角色个数为1的时候强制修改为多角色登录 */	
			if (iaUser.getAuthorities().size() == 1) {
				iaUser.setLoginType(SystemUserConstance.MULTI_ROLE_LOGIN);
			}
			/**是否单角色登录 */			
			if (SystemUserConstance.MULTI_ROLE_LOGIN.equals(iaUser.getLoginType())) {
				iaUser.setRolesInfo(orgSearchService.getRoleInfo(iaUser.getAuthorities()));
				iaUser.setGrant(secGrantService.getGrantMenus("MENU_RES",iaUser));
				iaUser.setAuthInfos(dataAuthInfo.LoadAuthInfo(iaUser));
				iaUser.setDataAuthInfo(DataAuthManager.getInstance().getDataAuthInfo(iaUser));
				
				//Map map=iaUser.getCRMNavigationMap();
				//iaUser.setCRMNavigationMap(map);
			} else {
				iaUser.setRolesInfo(new ArrayList());
				iaUser.setAuthorities(new ArrayList<GrantedAuthority>());
			}
			
			iaUser.setUserId(iaUser.getUsername());			
			String user_name=orgSearchService.getUserName(iaUser.getUsername());
			iaUser.setUsername(user_name);
			iaUser.setCname(user_name);
			Map orgMap=orgSearchService.getOrgInfo(iaUser.getUnitId());
			if(orgMap!=null){
				iaUser.setUnitName(orgMap.get("UNITNAME").toString());
				iaUser.setUnitlevel(orgMap.get("LEVELUNIT").toString());
				iaUser.setUnitInfo(orgMap);
			}
			
			iaUser.setAuthOrgList(orgSearchService.searchSubOrgTree(iaUser.getUnitId()));
			iaUser.setUpOrgList(orgSearchService.searchParentOrg(iaUser.getUnitId()));
			iaUser.setSubOrgList(orgSearchService.searchSubOrgs(iaUser.getUnitId()));
			iaUser.setPathOrgList(orgSearchService.searchPathInOrgTree(iaUser.getUnitId()));
			/** 用户动态设置session级别信息列表初始化*/
			SecLoaderManager.getInstance().setCtxSessionManager(getCtxSessionManager());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
