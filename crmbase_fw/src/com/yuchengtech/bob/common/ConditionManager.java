package com.yuchengtech.bob.common;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.yuchengtech.bob.upload.FileTypeConstance;
/***
 * 自动生成条件项配置
 * @author CHANGZHENHUA@YUCHENGTECH.COM
 * @version 2014-03-19
 **/
public class ConditionManager {
	/***构造方法*/
	public ConditionManager(Map<String, Object> conditon) {
		this.condition = conditon;
	}
	/***默认时间格式*/
	private String dateFormat = "yyyy-MM-dd";

	private Map<String, Object> condition;
	/**条件SQL*/
	private StringBuffer whereSQL;

	/***
	 *  拼接条件项 <br>
	 *  <p>向条件串中增加条件项(默认逻辑符为 and)<br>
	 *  @param conditionName 列名<br>
	 *  @param compareChar 比较符<br>
	 *  @param conditionValue 条件值<br>
	 **/
	public void addItem(String conditionName, String compareChar, String conditionIndex) {
		addItem(compareChar, conditionName, conditionIndex, -1);
	}
	
	/***
	 *  拼接条件项<br>
	 *  <p>向条件串中增加条件项<br>
	 *  @param conditionName       列名  <br>
	 *  @param compareChar 比较符 <br>
	 *  @param conditionValue      条件值  <br>
	 *  @param dataType数据类型<br>
	 **/
	public void addItem(String conditionName, String compareChar,
			String conditionValue, int dataType) {
		whereSQL = new StringBuffer();
		if (null == conditionValue || "".equals(conditionValue)) {
			
		} else if((condition.get(conditionValue) == null||("".equals(condition.get(conditionValue))))
				&& dataType != -1) {
			
		} else {
			if("like".equals(compareChar.toLowerCase())) {
				compareChar = " "+ compareChar + " ";
			} else if ("in".equals(compareChar.toLowerCase())) {
				compareChar = compareChar + " ";
			}
			whereSQL.append(conditionName + compareChar
						+ getItemValue(conditionName,compareChar, conditionValue, dataType));
		}
	}
	
	public StringBuffer getWhereSQL() {
		return whereSQL;
	}

	/***
	 *  返回查询条件值<br>
	 *  @param conditionName 列名<br>
	 *  @param conditionValue 条件值<br>
	 **/
	private String getItemValue(String conditionName,String compareChar, String conditionValue, int dataType) {
		if (dataType == -1) {
			return  conditionValue;
		}
		
		if(dataType == DataType.Date) {
			
			SimpleDateFormat dateformat = new SimpleDateFormat(getDateFormat());
			String dateStr = "";
			if (condition.get(conditionValue) instanceof String) {
				//增加查询日期格式处理:前台传过来的数据可能是：2014-06-24T00:00:00,此格式转化时会报参数异常,故作截取处理
				String value = (String) condition.get(conditionValue);
				if(value != null && !"".equals(value) && value.length() >= 10){
					value = value.substring(0, 10);
				}
				dateStr = dateformat.format(java.sql.Date.valueOf(value));
			} else if (condition.get(conditionValue) instanceof java.sql.Date){
				dateStr = dateformat.format(condition.get(conditionValue));
			} else if (condition.get(conditionValue) instanceof java.util.Date){
				dateStr = dateformat.format(condition.get(conditionValue));
			} else {
				//TODO 其它时间格式待增加待验证
			}
			
			if("DB2".equals(FileTypeConstance.getBipProperty("dbType"))) {
				return  "'" + dateStr + "'";
			} else if("ORACLE".equals(FileTypeConstance.getBipProperty("dbType"))) {
				return  "to_date('" + dateStr + "','" + getDateFormat() + "')";
			} else {
				return  "to_date('" + dateStr + "','" + getDateFormat() + "')";
			}
					 
		} else if (dataType == DataType.Number) {
			return condition.get(conditionValue).toString();
		} else {
			if("like".equalsIgnoreCase(compareChar.trim())){
				return  "'%" + condition.get(conditionValue) + "%'";
			}
			return  "'" + condition.get(conditionValue) + "'";
		}
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}
}
