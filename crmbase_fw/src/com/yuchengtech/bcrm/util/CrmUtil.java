package com.yuchengtech.bcrm.util;

import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONObject;

/**
 * <p> Description: 与业务无关的方法工具类</p>
 * <p> Copyright: Copyright (c) 2013 </p>
 * <p> Create Date: 2013-08-14 </p>
 * <p> Company: YTEC </p> 
 * @author WuDi
 */
public class CrmUtil {
	
    /**
     * 判断数据是否为空,""及null都认为是空
     * @param str 字符串
     * @return true-为空,false-非空
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断数组是否为空,null或长度为零
     * @param str 字符串
     * @return true-为空,false-非空
     */
    public static boolean isEmpty(Object[] arrray) {
        if (arrray == null || arrray.length == 0) {
            return true;
        }
        return false;
    }
    
    /**
     * json格式的字符串，转化成Map
     * @param str json格式的字符串
     * @return Map
     */
    public static Map jsonObjectToMap(String str) {
    	JSONObject jsonObject = JSONObject.fromObject(str);
    	Map jsonMap = (Map)jsonObject;
    	return jsonMap;
    }
    
	/**
	 * 分割字符串处理
	 * @param src 原字符串
	 * @param spit 分割符
	 * @return
	 */
	public static String[] strSplit(String src, String spit) {
		Object obj = null;
		int i = 0;
		Vector vector = new Vector();
		String as[] = new String[1];
		if (src == null)
			return new String[0];
		if (spit == null)
			return null;
		if (src.trim().equals(""))
			return new String[0];
		i = src.indexOf(spit);
		String s3 = "";
		while (i != -1)
			if (i != 0 && src.substring(i - 1, i).equals("\\")) {
				s3 = s3 + src.substring(0, i - 1) + spit;
				src = src.substring(i + 1);
				i = src.indexOf(spit);
			} else {
				String s2 = s3.equals("") ? src.substring(0, i).trim() : s3
						+ src.substring(0, i).trim();
				src = src.substring(i + 1);
				vector.addElement(s2);
				s3 = "";
				i = src.indexOf(spit);
			}
		vector.addElement(src.trim());
		as = new String[vector.size()];
		for (int j = 0; j < vector.size(); j++)
			as[j] = (String) vector.elementAt(j);
		return as;
	}
	
	/**
	 * 将格式如：aa,bb,cc 或'aa','bb','cc'的字符串转换为数组
	 * @param input 要转换的字符串
	 * @return 字符串数组
	 */
	public static String[] convertStringToArray(String input){
		String result = input.replaceAll("'", "");
		
		return result.split(",");
	}
	
	/**
	 * 将格式如： aa,bb,cc的字符串转换为'aa','bb','cc'
	 * @param input 要转换的字符串
	 * @return 字符串数组
	 */
	public static String addStringQuotation(String input){
		String result = input.replaceAll(",", "','");
		
		return "'"+result+"'";
	}
	
	public static void main(String[] args) {
		String[] s = CrmUtil.convertStringToArray("12,345,66");
		System.out.println(s.toString());
	}
}
