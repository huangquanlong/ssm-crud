package com.yuchengtech.crm.sec.credentialstrategy;


import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.security.authentication.BadCredentialsException;

import com.yuchengtech.bob.core.UserOnlineManager;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.sec.common.SystemUserConstance;
import com.yuchengtech.crm.sec.vo.CredentialInfo;
/**
 * 在线用户策略类
 * @author CHANGZH
 * @date 2013-03-14
 * 
 **/
public class UserOnlineStrategy extends CredentialStrategy {
	
	UserOnlineStrategy () {
		CreStrategyID = SystemUserConstance.CS_USERONLINE_ID;
	}
	
	public void setCreStrategyID (String ID) {
		CreStrategyID = ID;
	}
	
	@SuppressWarnings("unchecked")
	public boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked) {
		boolean isCredentialStrategy = false;
		if (isAuthenticationChecked) {
			Cache userOnlineCache = UserOnlineManager.getInstance().getUserOnlineCache();
	        Element element = userOnlineCache.get(UserOnlineManager.cacheKey);
	        List<AuthUser> list = new ArrayList();
	        if (element != null) {
				list = (List<AuthUser>)element.getObjectValue();
	        }	
	        int curUserNum = list.size();
	        if(curUserNum >= Integer.parseInt(this.CreStrategyDetail)) {
	        	userDetails.setUserId(userDetails.getUsername());
	        	UserOnlineManager.getInstance().refreshCache(true, userDetails);
	        	if (curUserNum < list.size()) {
	        		doActionType(ActionType, "当前在线用户数已达到最大用户数["+ this.CreStrategyDetail +"]", userDetails);
	        		isCredentialStrategy = true;
	        	} else {
	        		UserOnlineManager.getInstance().refreshCache(false, userDetails);
	        	}
	        	
	        }
		}
		return isCredentialStrategy;
	}
	
	/**策略执行动作*/
	public void doActionType (String ActionType, String message, AuthUser userDetails) {
		/**策略执行动作类型判断*/
		if (ICredentialStrategy.ACTIONTYPE_WARN.equals(ActionType)) {
			CredentialInfo credentialInfo = new CredentialInfo();
			credentialInfo.setInfoType(ICredentialStrategy.INFOTYPE_ONE);
			credentialInfo.setMessage("[" + CreStrategyName + "]:" + message);
			userDetails.setCredentialInfo(credentialInfo);
		} else if (ICredentialStrategy.ACTIONTYPE_FREEZING.equals(ActionType)) {
			getSecGrantService().freezingUser(userDetails);
			throw new BadCredentialsException("[" + CreStrategyName + "]:" + message+"用户已被冻结,请联系管理员", null);
		} else if (ICredentialStrategy.ACTIONTYPE_FORBIDDEN.equals(ActionType)) {
			throw new BadCredentialsException("[" + CreStrategyName + "]:" + message, null);
		}
	}

}

