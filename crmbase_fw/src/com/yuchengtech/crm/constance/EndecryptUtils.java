package com.yuchengtech.crm.constance;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

/**
 * 
 * <pre>
 * Title:用于加解密操作的实用类
 * Description: 程序功能的描述
 * </pre>
 * 
 * @author luoshifei luosf@yuchengtech.com
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class EndecryptUtils {

    /**
     *  加密数据
     * 
     * @param data 待加密数据
     * @param key 密钥(Salt)
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key) {
        // 直接指定待采用的加密算法（MD5）
        MessageDigestPasswordEncoder mdpeMd5 = new MessageDigestPasswordEncoder("MD5");

        // 生成24位的Base64版
        mdpeMd5.setEncodeHashAsBase64(true);

        // 获得加密后的密码
        return mdpeMd5.encodePassword(data, key);
    }

    /**
     *  加密数据
     * 
     * @param data 待加密数据
     * @return 加密后的数据
     */
    public static String encrypt(String data) {
        return encrypt(data, null);
    }
    
    /**
     * 简单的字符串加密
     * @param str 要进行加密的字符串
     * @return 加密后的字符串
     */
    public static String simpleEncrypt(String str){
    	return simpleEncrypt(str,'');
    }
    
    /**
     * 简单的字符串加密的逆运算，即解密
     * @param str 要进行解密的字符串
     * @return 解密后的字符串
     */
    public static String simpleDencrypt(String str){
    	return simpleDencrypt(str,'');
    }
    
    /**
     * 简单的字符串加密
     * <br> 只加密33~126的字符，即26个大小写字母和数字，以及常见标点符号
     * @param str 要进行加密的字符串，
     * @param encrypt 进行加密所需要的字符
     * @return 加密后的字符串
     */
    public static String simpleEncrypt(String str,char encrypt){
    	if(str == null ) return str;
    	StringBuffer sb = new StringBuffer();
    	for(char c:str.toCharArray()){
    		if(c < 33 || c > 126){
    			sb.append(c);
    		}else{
	    		int i = c-encrypt;
	    		if(i < 33){
	    			i = 28 - (33 - i) ;
	    		}
	    		sb.append((char)i);
    		}
    	}
    	return sb.toString();
    }
    /**
     * 简单的字符串加密的逆运算，即解密
     * @param str 要进行解密的字符串，
     * @param encrypt 进行解密所需要的字符
     * @return 解密后的字符串
     */
    public static String simpleDencrypt(String str,char encrypt){
    	if(str == null ) return str;
    	StringBuffer sb = new StringBuffer();
    	for(char c:str.toCharArray()){
    		if(c < 14 || (c > 27 && c < 33) || c > 126){
    			sb.append(c);
    		}else{
    			int i = 0;
        		if(c < 33){
        			i = 33 + encrypt - (28 - c);
        		}else{
        			i = c + encrypt;
        		}
        		sb.append((char)i);
    		}
    	}
    	return sb.toString();
    }
    public static void main(String[] args) {
    	System.out.println(encrypt("123456",null));;
	}

}
