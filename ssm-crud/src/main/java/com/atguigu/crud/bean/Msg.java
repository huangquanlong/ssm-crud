package com.atguigu.crud.bean;
/*
 * 消息类
 * 
 * */

import java.util.HashMap;
import java.util.Map;

public class Msg {
	//1000代表成功success 1001代表fail
	private int code;
	//处理信息
	private String msg;
	//处理对象
	private Map<String, Object> map=new HashMap<String, Object>();
	
	public static Msg success(){
		Msg result=new Msg();
		result.setCode(1000);
		result.setMsg("处理成功！");
		return result;
	}
	public static Msg fail(){
		Msg result=new Msg();
		result.setCode(1001);
		result.setMsg("处理失败！");
		return result;
	}
	public  Msg add(String key,Object value){
		this.getMap().put(key, value);
		return this;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
}
