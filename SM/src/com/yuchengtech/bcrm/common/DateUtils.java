package com.yuchengtech.bcrm.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @describtion: 
 *
 * @author : lhqheli (email: lhqheli@gmail.com)
 * @date : 2014年7月7日 下午2:22:31
 */
public class DateUtils {

	/**
	 * @describtion: 返回当前时间，格式 pattern
	 * @return
	 */
	public static String getCurrentTime(String pattern){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(pattern);
		df.setLenient(false);
		return df.format(date);
	}
	
	/**
	 * @describtion: 得到当前时间	格式yyyyMMdd
	 * @return	yyyyMMdd字符串
	 */
	public static String getCurrentDate(){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false);
        return df.format(date);
	}
	
	/**
	 * @describtion: 得到当前时间	格式yyyy-MM-dd HH:mm:ss
	 * @return	yyyy-MM-dd HH:mm:ss字符串
	 */
	public static String getCurrentDateTimeF(){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setLenient(false);
		return df.format(date);
	}
	
	/**
	 * @describtion: 得到当前时间	格式yyyy-MM-dd
	 * @return	yyyy-MM-dd字符串
	 */
	public static String getCurrentDateF(){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
		return df.format(date);
	}
	
	/**
	 * @describtion: 将字符串转换为日期
	 * @param dateS 日期格式 yyyy-MM-dd
	 * @return
	 */
	public static Date parseDate(String dateS){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
    	try {
			return df.parse(dateS);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	/**
	 * @describtion: 将字符串转换为日期
	 * @param dateS 日期
	 * @param format 格式
	 * @return
	 */
	public static Date parseDate(String dateS,String format){
		DateFormat df = new SimpleDateFormat(format);
		df.setLenient(false);
    	try {
			return df.parse(dateS);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	/**
	 * @describtion: 将格式'yyyy-MM-dd'转换为日期类型
	 * @param date  传入日期
	 * @return
	 */
	public static String formatDate(Date date){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
		return df.format(date);
	}
	
	/**
	 * @describtion: 将字符串转换为日期类型
	 * @param date 传入日期
	 * @param format 格式参数
	 * @return
	 */
	public static String formatDate(Date date,String format){
		DateFormat df = new SimpleDateFormat(format);
		df.setLenient(false);
		return df.format(date);
	}
	
	/**
	 * 返回当前毫秒,同步,唯一
	 * @return
	 */
	public static synchronized String currentTimeMillis(){
		long t = new Date().getTime();
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return String.valueOf(t);
	}
}
