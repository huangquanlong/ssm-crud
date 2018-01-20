package com.yuchengtech.crm.constance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuTreeNode implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id; 

	private String name; 
	
	private String parentId; 
	
	private Long nodeLevel; 
	
	private String nodeType;
	
	private Boolean isLeaf; 
	
	private Map attributes = new HashMap();	
	
	private List<MenuTreeNode> children = new ArrayList<MenuTreeNode>(0);
	
	/**
	 * 设置节点的属性。
	 * 
	 * @param name 属性名称。
	 * @param value 属性值。
	 */
	public void addAttribute(String name, Object value) {
		if(name != null && value != null) {
			attributes.put(name, value);
		}
	}
	
	/**
	 * 返回指定名称的属性。
	 * 
	 * @param name 所返回属性的名称。
	 * @return attribute 元素的属性。
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public List<MenuTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<MenuTreeNode> children) {
		this.children = children;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Long getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(Long nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

}