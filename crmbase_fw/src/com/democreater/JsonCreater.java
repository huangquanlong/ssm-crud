package com.democreater;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.struts2.json.JSONUtil;

import com.yuchengtech.bob.core.LookupManager;

public class JsonCreater {
	
	private String jsonPath = "C:/crmFiles/jsons/";
	private Object result = null;
	private String fileName = null;
	
	public JsonCreater(Object result, String fileName){
		this.result = result;
		this.fileName = fileName;
	}
	
	public void createJson() throws Exception{
		File jsonFile = buildFile();
		FileWriter writer = new FileWriter(jsonFile);
		writer.write("var responseText = "+buildFileContent()+";");
		writer.close();
	}
	
	private String buildFileContent() throws Exception{
		return JSONUtil.serialize(result);		
	}
	
	
	private File buildFile() throws Exception{
		File file = new File(jsonPath);
		if (! file.exists()) {
			file.mkdir();
		}
		file = new File(jsonPath+this.fileName+".json");
		if (! file.exists()) {
			file.createNewFile();
		}
		return file;
	}
	
	public class LookupGroup{
		private String name ;
		private List items  = new ArrayList();
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List getItems() {
			return items;
		}
		public void setItems(List items) {
			this.items = items;
		}
		
		public void addItem(String key, String value){
			Map i = new HashMap();
			i.put("key", key);
			i.put("value", value);
			items.add(i);
		}
		
	}
	
	
	public void createLookupJSON() throws Exception{
		
			
		
		StringBuilder d = new StringBuilder("var _lookups={");
		LookupManager lm =LookupManager.getInstance();
		ConcurrentHashMap<String, ConcurrentHashMap<String, String>> a = lm.getAll();
		List<LookupGroup> iii = new ArrayList<LookupGroup>();
		for(String key : a.keySet()){
			LookupGroup lg = new LookupGroup();
			lg.setName(key);
			for(String ik: a.get(key).keySet()){
				lg.addItem(ik, a.get(key).get(ik));
			}
			iii.add(lg);
		}
		
		
		
		File jsonFile = buildFile();
		FileWriter writer = new FileWriter(jsonFile);
		
		for(int i=0;i<iii.size();i++){
			try{
				d.append(iii.get(i).getName()+":");
				d.append(JSONUtil.serialize(iii.get(i)));
				if(i<iii.size()-1){
					d.append(",");
				}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("name="+iii.get(i).getName());
				
			}
		}
		d.append("};");
		writer.write(d.toString());
		
		writer.close();
	}
	
	
}
