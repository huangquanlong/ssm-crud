package com.yuchengtech.bcrm.system.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.yuchengtech.crm.constance.OperateTypeConstant;

/**
 * 查询日志事件类型
 * @author weijl
 * @since 2012-09-24
 */
@ParentPackage("json-default")
@Action(value="/AdminLogType", results={
    @Result(name="success", type="json", params = {"actionName", "AdminLogType" }),
})
public class AdminLogTypeAction {
	
    private List<HashMap<String, String>> JSON;//声明返回值（日志事件类型列表）
    
    /**
     * 构造方法 ，创建空的日志事件类型列表
     */
    public AdminLogTypeAction() {
    	JSON = new ArrayList<HashMap<String, String>>();
    }

    public String index() {
        return "success";
    }
    
    /**
     * 获取日志事件类型列表
     * @return 日志事件类型列表
     */
    public List<HashMap<String, String>> getJSON() {
    	
    	Map<Integer,String> operMap = OperateTypeConstant.OPERATE_TYPE_MAP;
    	HashMap<String,String> eventMap;
    	Set<Map.Entry<Integer,String>> set = operMap.entrySet();//获取操作类型map的键值对对象
    	Iterator<Map.Entry<Integer, String>> it = set.iterator();//为操作类型map的键值对对象创建迭代器
    	
    	while(it.hasNext()){//遍历操作类型map并拆分，组装成返回的事件类型列表
    		eventMap = new HashMap<String,String>();
    		Map.Entry<Integer, String> entry = (Entry<Integer, String>) it.next();
    		eventMap.put("key",entry.getKey().toString());
    		eventMap.put("value", entry.getValue());
			JSON.add(eventMap);
    	}
        return JSON;
    }

}
