package com.yuchengtech.bob.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

@ParentPackage("json-default")
@Action(value="/lookup_gp", results={
    @Result(name="success", type="json"),
})
public class LookupGreenplumAction {
    
    public class KeyValuePairGP {
        
        private String key;
        private String value;
        
        public KeyValuePairGP(String key, String value) {
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

    private List<KeyValuePairGP> JSON;
    
    private String name;
    
    public LookupGreenplumAction() {
        JSON = new ArrayList<KeyValuePairGP>();
     }

    public String index() {
        return "success";
    }

    public List<KeyValuePairGP> getJSON() {
        ConcurrentHashMap<String, String> map = new  ConcurrentHashMap<String, String>();//LookupManager.getInstance().getGreenplumValues(name);
        if (map != null) {
            for(Entry<String, String> item : map.entrySet()) {
                JSON.add(new KeyValuePairGP(item.getKey(), item.getValue()));
            }
        }
        return JSON;
    }

    public void setName(String name) {
        this.name = name;
    }

}
