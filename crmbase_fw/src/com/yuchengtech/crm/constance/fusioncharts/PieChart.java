package com.yuchengtech.crm.constance.fusioncharts;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PieChart extends FusionChartBasicObject{
	
	private String labelColumn = null;
	private String valueColumn = null;

	public void addPieData(String value,String label){
		cdsh.addPieData(value, label);
	}
	public void addPieData(String value,String label,Map<String, String> properties){
		cdsh.addPieData(value, label, properties);
	}
	
	public Map<String, Object> getJson(){
		Map<String , Object> json = new HashMap<String, Object>();
		json.put("chart", cph.getProperties());
		json.put("data", cdsh.getDatasets());
		return json;
	}
	
	public void setJsonData(JSONObject jData) {
		JSONArray ja = jData.getJSONObject("json").getJSONArray("data");
		for(int i = 0; i<ja.size(); i++){
			JSONObject o = (JSONObject) ja.get(i);
			String value = o.getString(valueColumn);
			String label = o.getString(labelColumn);
			addPieData(value, label);
		}
	}
	public String getLabelColumn() {
		return labelColumn;
	}
	public void setLabelColumn(String labelColumn) {
		this.labelColumn = labelColumn;
	}
	public String getValueColumn() {
		return valueColumn;
	}
	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}
	
}
