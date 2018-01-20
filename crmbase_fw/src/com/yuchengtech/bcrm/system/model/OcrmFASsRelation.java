package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the OCRM_F_A_SS_RELATION database table.
 * 
 */
@Entity
@Table(name="OCRM_F_A_SS_RELATION")
public class OcrmFASsRelation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	@Column(name="JOIN_LEFT_ALIAS")
	private String joinLeftAlias;

	@Column(name="JOIN_LEFT_COL")
	private Long joinLeftCol;

	@Column(name="JOIN_LEFT_TABLE")
	private Long joinLeftTable;

	@Column(name="JOIN_RIGHT_ALIAS")
	private String joinRightAlias;

	@Column(name="JOIN_RIGHT_COL")
	private Long joinRightCol;

	@Column(name="JOIN_RIGHT_TABLE")
	private Long joinRightTable;

	@Column(name="SS_COL_LEFT")
	private String ssColLeft;

    public OcrmFASsRelation() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJoinLeftAlias() {
		return this.joinLeftAlias;
	}

	public void setJoinLeftAlias(String joinLeftAlias) {
		this.joinLeftAlias = joinLeftAlias;
	}

	public Long getJoinLeftCol() {
		return this.joinLeftCol;
	}

	public void setJoinLeftCol(Long joinLeftCol) {
		this.joinLeftCol = joinLeftCol;
	}

	public Long getJoinLeftTable() {
		return this.joinLeftTable;
	}

	public void setJoinLeftTable(Long joinLeftTable) {
		this.joinLeftTable = joinLeftTable;
	}

	public String getJoinRightAlias() {
		return this.joinRightAlias;
	}

	public void setJoinRightAlias(String joinRightAlias) {
		this.joinRightAlias = joinRightAlias;
	}

	public Long getJoinRightCol() {
		return this.joinRightCol;
	}

	public void setJoinRightCol(Long joinRightCol) {
		this.joinRightCol = joinRightCol;
	}

	public Long getJoinRightTable() {
		return this.joinRightTable;
	}

	public void setJoinRightTable(Long joinRightTable) {
		this.joinRightTable = joinRightTable;
	}

	public String getSsColLeft() {
		return this.ssColLeft;
	}

	public void setSsColLeft(String ssColLeft) {
		this.ssColLeft = ssColLeft;
	}

}