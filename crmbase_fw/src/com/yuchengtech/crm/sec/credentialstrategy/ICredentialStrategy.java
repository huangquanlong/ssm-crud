package com.yuchengtech.crm.sec.credentialstrategy;

import com.yuchengtech.bob.vo.AuthUser;
/**
 * @author changzh@yuchengtech.com
 * @date 2012-11-05
 * 说明 ：  认证策略接口
 **/
public interface ICredentialStrategy {
	
	/**
	 * 执行策略方法
	 * @param  userDetails 用户信息
     * @param  isAuthenticationChecked 是否通过安全认证
	 * @return isCredentialStrategy 是否符合当前策略
	 * */
	public boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked);
	
	/**执行动作标识：警告*/
	public String ACTIONTYPE_WARN      = "3";
	
	/**执行动作标识：禁止*/
	public String ACTIONTYPE_FORBIDDEN = "2";
	
	/**执行动作标识：冻结*/
	public String ACTIONTYPE_FREEZING  = "1";
	
	/**执行警告动作_默认类型*/
	public String INFOTYPE_DEFAULT = "0";
	
	/**执行警告动作_类型一*/
	public String INFOTYPE_ONE 	   = "1";
	
	/**执行警告动作_类型二*/
	public String INFOTYPE_TOW     = "2";
	
	/**执行警告动作_类型三*/
	public String INFOTYPE_THREE   = "3";
	
}
