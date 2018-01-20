package com.yuchengtech.crm.constance.fusioncharts;

import java.util.HashMap;
import java.util.Map;

/**
 * chart图标属性构建
 * @author WILLJOE
 * @since 2013-03-21
 */
public final class ChartPropertiesHelper {
	
	private Map<String,String> properties = new HashMap<String,String>();
	
	public void addAttribute(String key, String value){
		properties.put(key, value);
	}
	
	public void removeAttribute(String key){
		properties.remove(key);
	}
	
	public void addAttributes(Map<String,String> attributes){
		properties.putAll(attributes);
	}
	
	public Map<String, String> getProperties(){
		return properties;
	}
	
}
