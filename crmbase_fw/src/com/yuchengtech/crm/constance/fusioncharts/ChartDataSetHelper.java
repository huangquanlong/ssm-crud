package com.yuchengtech.crm.constance.fusioncharts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChartDataSetHelper {

	private List<Map<String,Object>> datasets = new ArrayList<Map<String,Object>>();
	
	public void addLineData(List<String> dataset,String seriesname){
		datasets.add(createDataMap(dataset,seriesname));
	}
	
	public void addLineData(List<String> dataset,String seriesname,Map<String, String> properties){
		Map<String,Object> dataMap = createDataMap(dataset,seriesname);
		dataMap.putAll(properties);
		datasets.add(dataMap);
	}
	
	public void addLineData(String dataset,String seperator,String seriesname){
		String[] datasplit = dataset.split(seperator);
		datasets.add(createDataMap(datasplit,seriesname));
	}
	
	public void addLineData(String dataset,String seperator,String seriesname,Map<String, String> properties){
		
		String[] datasplit = dataset.split(seperator);
		Map<String,Object> dataMap = createDataMap(datasplit,seriesname);
		dataMap.putAll(properties);
		datasets.add(dataMap);
	}
	
	public void addPieData(String value,String label){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("value", value);
		data.put("label", label);
		datasets.add(data);
	}
	
	public void addPieData(String value,String label,Map<String, String> properties){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("value", value);
		data.put("label", label);
		data.putAll(properties);
		datasets.add(data);
	}
	
	private Map<String, Object> createDataMap(List<String> dataset,String seriesname){
		List<Map<String, String>> datas = new ArrayList<Map<String,String>>(); 
		for(String value:dataset){
			Map<String, String> data = new HashMap<String,String>();
			data.put("value", value);
			datas.add(data);
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", datas);
		dataMap.put("seriesname", seriesname);
		return dataMap;
	}
	
	private Map<String, Object> createDataMap(String[] datasplit,String seriesname){
		
		List<Map<String, String>> datas = new ArrayList<Map<String,String>>(); 
		for(String value:datasplit){
			Map<String, String> data = new HashMap<String,String>();
			data.put("value", value);
			datas.add(data);
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", datas);
		dataMap.put("seriesname", seriesname);
		return dataMap;
	}
	
	public List<Map<String,Object>> getDatasets(){
		return this.datasets;
	}
	
}
