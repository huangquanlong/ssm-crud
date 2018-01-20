package com.yuchengtech.crm.constance.fusioncharts;

import java.util.Map;

import net.sf.json.JSONObject;

public abstract class FusionChartBasicObject {
	
	public static final String CHART_LINE = "line";
	public static final String CHART_PIE = "pie";
	
	private JSONObject jData = null;
	
	ChartCategoriesHelper cch =  new ChartCategoriesHelper();
	ChartPropertiesHelper cph = new ChartPropertiesHelper();
	ChartDataSetHelper cdsh = new ChartDataSetHelper();
	
	public abstract Map<String, Object> getJson();
	public abstract void setJsonData(JSONObject jData);
	
	public void addAttribute(String key, String value){
		cph.addAttribute(key, value);
	}
	public void removeAttribute(String key){
		cph.removeAttribute(key);
	}
	public void addAttributes(Map<String,String> attributes){
		cph.addAttributes(attributes);
	}
	

	public JSONObject getJsonData(){
		return this.jData;
	}
}
