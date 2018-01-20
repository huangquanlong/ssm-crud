package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 客户视图项维护的model
 * @author zhangsxin
 * @since 2012-11-30
 *
 */
@Entity
@Table(name="OCRM_SYS_VIEW_MANAGER")
public class OcrmSysViewManager implements Serializable {
	private static final Long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;//主键
	
	private String addr;//链接地址


	private String name;//名称

	private Long orders;//顺序号

	private Long parentid;//客户类型

	private Long viewtype;

    public OcrmSysViewManager() {
    }

	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrders() {
		return this.orders;
	}

	public void setOrders(Long orders) {
		this.orders = orders;
	}

	public Long getParentid() {
		return this.parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public Long getViewtype() {
		return this.viewtype;
	}

	public void setViewtype(Long viewtype) {
		this.viewtype = viewtype;
	}

}