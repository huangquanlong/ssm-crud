package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * 客户视图项维护的控制项model
 * @author chenmeng
 * @since 2014-12-19
 *
 */
@Entity
@Table(name="OCRM_SYS_VIEW_MANAGER_CONTROL")
public class OcrmSysViewManagerControl implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="OCRM_SYS_VIEW_MANAGER_CONTROL_GENERATOR", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.SEQUENCE)
	@Column(name="ID")
	private Long id;
	
	@Column(name="CONTROL_CODE")
	private String controlCode;

	@Column(name="CONTROL_NAME")
	private String controlName;


	@Column(name="MANAGER_ID")
	private BigDecimal managerId;

    public OcrmSysViewManagerControl() {
    }

	public String getControlCode() {
		return this.controlCode;
	}

	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}

	public String getControlName() {
		return this.controlName;
	}

	public void setControlName(String controlName) {
		this.controlName = controlName;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getManagerId() {
		return this.managerId;
	}

	public void setManagerId(BigDecimal managerId) {
		this.managerId = managerId;
	}

}