package com.yuchengtech.crm.sec.credentialstrategy;


import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.sec.common.SystemUserConstance;
/**
 * 首次登录策略类
 * @author changzh@yuchengtech.com
 * @date 2012-11-05
 * 
 **/
public class FirstLoginStrategy extends CredentialStrategy {
	
	FirstLoginStrategy () {
		CreStrategyID = SystemUserConstance.CS_FIRST_LOGIN_ID;
	}
	
	public void setCreStrategyID (String ID) {
		CreStrategyID = ID;
	}
	
	public boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked) {
		boolean isCredentialStrategy = false;
		if (isAuthenticationChecked) {
			//首次登录逻辑判断,20141026根据密码是否修改标志判断
			if ("".equals(userDetails.getTemp2())
					|| null == userDetails.getTemp2()) {
				doActionType(ActionType, "请注意及时更新密码。", userDetails);
				isCredentialStrategy = true;
			}
		}
		return isCredentialStrategy;
	}

}

