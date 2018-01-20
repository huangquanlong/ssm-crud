package com.yuchengtech.crm.constance;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
/**
 * 
 * <pre>
 * Title:用于加解密操作 
 * Description: MD5加密
 * </pre>
 * 
 * @author changzh changzh@yuchengtech.com
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:  
 * </pre>
 */
public class MD5PasswordEncoder implements PasswordEncoder {

	/**
	 * 这个方法供下面的方法isPasswordValid 调用 是用来进行盐加密的
	 *
	 **/
	public String encodePassword(String rawPass, Object salt)
			throws DataAccessException {
		//String salted = rawPass + "{" + salt.toString() + "}";
		//System.out.println(salted);
		return EndecryptUtils.encrypt(rawPass);
	}

	/**
	 * * 这里便是当用户输入用户名密码后security调用的你自己的密码编辑器的方法 encPass 应该是数据库中的值 rawPass
	 * 是你输入的密码值 * salt 盐值 *
	 */
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		if (encPass.equals(encodePassword(rawPass, salt))) {
			return true;
		}
		return false;
	}

}
