package com.yuchengtech.crm.constance.fusioncharts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class LineChart extends FusionChartBasicObject{

	private Map<String, String> dataColumns = new HashMap<String, String>();
	
	public void addLineData(List<String> dataset,String datasetName){
		cdsh.addLineData(dataset, datasetName);
	}
	public void addLineData(String dataset,String seperator,String datasetName){
		cdsh.addLineData(dataset, seperator, datasetName);
	}
	public void addLineData(String dataset,String seperator,String seriesname,Map<String, String> properties){
		cdsh.addLineData(dataset, seperator, seriesname);
	}
	public void addLineData(List<String> dataset,String seriesname,Map<String, String> properties){
		cdsh.addLineData(dataset, seriesname, properties);
	}
	
	public void setCategories(List<String> labels){
		cch.setCategories(labels);
	}
	public void setCategories(String labels,String seprator){
		cch.setCategories(labels, seprator);
	}
	public void addCategories(List<String> labels){
		cch.addCategories(labels);
	}
	public void addCategories(String labels,String seprator){
		cch.addCategories(labels, seprator);
	}
	
	public Map<String, Object> getJson(){
		Map<String , Object> json = new HashMap<String, Object>();
		json.put("chart", cph.getProperties());
		json.put("categories", cch.getCategories());
		json.put("dataset", cdsh.getDatasets());
		return json;
	}
	
	public void addDataColumn(String columnName, String dataName){
		dataColumns.put(columnName, dataName);
	}

	public void removeDataColumn(String columnName){
		dataColumns.remove(columnName);
	}
	
	public void addDataColumns(Map<String,String> columns){
		dataColumns.putAll(columns);
	}
	
	public void setJsonData(JSONObject jData) {
		JSONArray ja = jData.getJSONObject("json").getJSONArray("data");
		createJsonData(ja);
	}
	
	public void setJsonData(JSONObject jData,Map<String,String> columns) {
		addDataColumns(columns);
		JSONArray ja = jData.getJSONObject("json").getJSONArray("data");
		createJsonData(ja);
	}
	
	private void createJsonData(JSONArray ja){
		Iterator<String> it = dataColumns.keySet().iterator();
		while(it.hasNext()){
			List<String> tDataSet = new ArrayList<String>();
			String column = it.next();
			for(int i = 0; i<ja.size(); i++){
				JSONObject o = (JSONObject) ja.get(i);
				String tData = o.getString(column);
				tDataSet.add(tData);
			}
			this.addLineData(tDataSet, dataColumns.get(column));
		}
	}
	
}
