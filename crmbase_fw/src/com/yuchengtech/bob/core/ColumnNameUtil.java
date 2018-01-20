package com.yuchengtech.bob.core;

/**
 * 基础字符串操作：JAVA驼峰命名与数据库下划线风格命名相互转化
 * @author WILLJOE
 * @since 2013-03-04
 */
public class ColumnNameUtil {
	
	/**分隔符*/
	public static String SPLIT_CHAR = "_";
	
	/**
	 * 转换下划线风格命名为驼峰命名
	 * @param columnName：下划线分割字符串
	 * @return 驼峰命名字符串
	 */
	public static String getModelField(String columnName){
		String[] words = columnName.toLowerCase().split(ColumnNameUtil.SPLIT_CHAR);
		String fieldName = "";
		int i = 0;
		while(i < words.length){
			if(i==0){
				fieldName += words[0];
			}else{
				fieldName += words[i].charAt(0) >= 97 && words[i].charAt(0)<=122 ? (char)(words[i].charAt(0)-32)+words[i].substring(1):words[i];
			}
			i++;
		}
		return fieldName;
	}
	
	/**
	 * 转换驼峰命名字符串为下划线风格命名
	 * @param modelField：驼峰命名字符串
	 * @return 下划线分割字符串
	 */
	public static String getColumnName(String modelField){
		int i = 0;
		String finalString = "";
		String word = "";
		while(i<modelField.length()){
			if((modelField.charAt(i) >= 65 && modelField.charAt(i) <= 90) || modelField.charAt(i) == 95){
				if(!word.equals(""))
					finalString += finalString.equals("")?word.toUpperCase():ColumnNameUtil.SPLIT_CHAR+word.toUpperCase();
				word = "";
				if(modelField.charAt(i) != 95)
					word += (char)modelField.charAt(i);
			}else{
				word += (char)modelField.charAt(i);
			}
			i++;
		}
		finalString += finalString.equals("")?word.toUpperCase():ColumnNameUtil.SPLIT_CHAR+word.toUpperCase();
		return finalString;
	}

}
