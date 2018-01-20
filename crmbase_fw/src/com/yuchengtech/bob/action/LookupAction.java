package com.yuchengtech.bob.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.democreater.JsonCreater;
import com.yuchengtech.bob.core.LookupManager;

@ParentPackage("json-default")
@Action(value="/lookup", results={
    @Result(name="success", type="json"),
})
public class LookupAction {
    
    public class KeyValuePair {
        
        private String key;
        private String value;
        
        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }
               
        public String getValue() {
            return value;
        }
                
    }

    private List<KeyValuePair> JSON;
    
    private String name;
    
    public LookupAction() {
        JSON = new ArrayList<KeyValuePair>();
     }

    public String index() {
//    	LookupManager lm =LookupManager.getInstance();
//		Map d = new HashMap();
//		ConcurrentHashMap<String, ConcurrentHashMap<String, String>> a = lm.getAll();
//		
//		for(String key : a.keySet()){
//			d.put(key, a.get(key));
//		}
//		
//    	try {
//			new JsonCreater(d,"lookup").createLookupJSON();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        return "success";
    }

    public List<KeyValuePair> getJSON() {
        ConcurrentHashMap<String, String> map = LookupManager.getInstance().getOracleValues(name);
        if (map != null) {
            for(Entry<String, String> item : map.entrySet()) {
                JSON.add(new KeyValuePair(item.getKey(), item.getValue()));
            }
        }
        return JSON;
    }

    public void setName(String name) {
        this.name = name;
    }

}
