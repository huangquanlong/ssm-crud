package com.yuchengtech.crm.sec.credentialstrategy;


import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.sec.common.SystemUserConstance;
import com.yuchengtech.crm.sec.vo.CredentialInfo;
/**
 * 登录IP策略类
 * @author changzh@yuchengtech.com
 * @date 2012-11-05
 *
 **/
public class UserIPStrategy extends CredentialStrategy {
	
	UserIPStrategy () {
		CreStrategyID = SystemUserConstance.CS_LOGIN_IP_ID;
	}
	
	public void setCreStrategyID (String ID) {
		CreStrategyID = ID;
	}
	
	public boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked) {
		boolean isCredentialStrategy = false;
		if (isAuthenticationChecked) {
			
			boolean commonIP = false;
			if (userDetails.getOffenIP() != null) {
				String[] ipArray = userDetails.getOffenIP().split(",");
				/**遍历常用IP*/
				if (ipArray != null) {
					for (String ip : ipArray) {
						if (userDetails.getCurrentIP().equals(ip)) {
							commonIP = true;
							break;
						}
					}
				}
			}
			
			/**是否非常用IP*/
			if (!commonIP) {
				if (ICredentialStrategy.ACTIONTYPE_WARN.equals(ActionType)) {
					this.doActionType(ActionType, "非常用IP登录策略警告", userDetails);
				} else {
					super.doActionType(ActionType, "非常用IP登录策略警告", userDetails);
				}
				
				isCredentialStrategy = true;
			}
		}
		
		return isCredentialStrategy;
	}
	
	public void doActionType (String ActionType, String message, AuthUser userDetails) {
			CredentialInfo credentialInfo = new CredentialInfo();
			credentialInfo.setInfoType(ICredentialStrategy.INFOTYPE_ONE);
			credentialInfo.setMessage(message);
			userDetails.setCredentialInfo(credentialInfo);
	}

}

