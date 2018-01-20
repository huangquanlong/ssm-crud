package com.yuchengtech.crm.constance.fusioncharts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChartCategoriesHelper {
	
	List<Map<String,Object>> cates = new ArrayList<Map<String,Object>>();
	
	public void setCategories(List<String> labels){
		cates.clear();
		cates.add(createCategories(labels));
		return;
	}
	
	public void setCategories(String labels,String seprator){
		cates.clear();
		String[] labelsList = labels.split(seprator);
		cates.add(createCategories(labelsList));
		return;
	}
	
	public void addCategories(List<String> labels){
		cates.add(createCategories(labels));
		return;
	}
	
	public void addCategories(String labels,String seprator){
		String[] labelsList = labels.split(seprator);
		cates.add(createCategories(labelsList));
		return;
	}
	
	private Map<String,Object> createCategories(List<String> labels){
		Map<String,Object> categories = new HashMap<String,Object>();
		ArrayList<Map<String,String>> categoriesX = new ArrayList<Map<String,String>>();
		for(String label: labels){
			Map<String,String> category = new HashMap<String,String>();
			category.put("label", label);
			categoriesX.add(category);
		}
		categories.put("category", categoriesX);
		return categories;
	}
	
	private Map<String,Object> createCategories(String[] labels){
		Map<String,Object> categories = new HashMap<String,Object>();
		ArrayList<Map<String,String>> categoriesX = new ArrayList<Map<String,String>>();
		for(String label: labels){
			Map<String,String> category = new HashMap<String,String>();
			category.put("label", label);
			categoriesX.add(category);
		}
		categories.put("category", categoriesX);
		return categories;
	}
	
	public List<Map<String,Object>> getCategories() {
		return cates;
	}
	
	
	
}
