package com.yuchengtech.crm.sec.credentialstrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.SpringSecurityMessageSource;

import com.yuchengtech.bcrm.common.service.SecGrantService;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.sec.vo.CredentialInfo;
/**
 * 认证策略虚类
 * @author changzh@yuchengtech.com
 * @date 2012-11-05
 * 
 **/
public abstract class CredentialStrategy implements ICredentialStrategy {
	
	@Autowired
	private SecGrantService secGrantService;
	
	public static MessageSourceAccessor message()
    {
        return SpringSecurityMessageSource.getAccessor();
    }
	
	public SecGrantService getSecGrantService() {
		return secGrantService;
	}
	
	/**执行策略方法*/
	public abstract boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked);
	
	/**启用状态*/
	public boolean enable;
	
	/**策略标识*/
	public String CreStrategyID;
	
	/**策略名称*/
	public String CreStrategyName;
	
	/**策略明细*/
	public String CreStrategyDetail;
	
	/**执行动作*/
	public String ActionType;
	
	/**策略执行动作*/
	public void doActionType (String ActionType, String message, AuthUser userDetails) {
		/**策略执行动作类型判断*/
		if (ICredentialStrategy.ACTIONTYPE_WARN.equals(ActionType)) {
			CredentialInfo credentialInfo = new CredentialInfo();
			credentialInfo.setInfoType(ICredentialStrategy.INFOTYPE_DEFAULT);
			credentialInfo.setMessage("[" + CreStrategyName + "]:" + message);
			userDetails.setCredentialInfo(credentialInfo);
			//doActionWarn(ActionType, message, userDetails);			
		} else if (ICredentialStrategy.ACTIONTYPE_FREEZING.equals(ActionType)) {
			secGrantService.freezingUser(userDetails);
			throw new BadCredentialsException("[" + CreStrategyName + "]:" + "用户已被冻结,请联系管理员", null);
		} else if (ICredentialStrategy.ACTIONTYPE_FORBIDDEN.equals(ActionType)) {
			throw new BadCredentialsException("[" + CreStrategyName + "]:" + "用户已被禁止登录,请联系管理员", null);
		}
	}
	
}
