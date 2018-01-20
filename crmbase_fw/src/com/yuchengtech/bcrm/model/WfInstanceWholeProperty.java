package com.yuchengtech.bcrm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the WF_INSTANCE_WHOLE_PROPERTY database table.
 * 
 */
@Entity
@Table(name="WF_INSTANCE_WHOLE_PROPERTY")
public class WfInstanceWholeProperty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6054026093511930171L;

	@Id
	private String instanceid;

	private String appsign;

	private String author;

	private String bdraft;

	private String bizseqno;

	private String bxstatus;

	private String clientsign;

	private String custid;

	private String custname;

	private String depid;

	private String exv10;

	private String exv100;

	private String exv19;

	private String exv32;

	private String exv50;

	private String flowtrace;

	private String formdata;

	private String iswfset;

	private String orgid;

	private String spstatus;

	private String sysid;

	private String wfadmin;

	private String wfagain;

	private String wfagent;

	private String wfappid;

	private String wfappname;

	private String wfchange;

	private String wfendtime;

	private String wfhangup;

	private String wfid;

	private String wfjobname;

	private String wfjump;

	private String wfmainformid;

	private String wfname;

	private String wfplanendtime;

	private String wfreaders;

	private String wfrecall;

	private String wfreturnback;

	private String wfsign;

	private String wfstarttime;

	private String wfstatus;

	private String wfurge;

	private String wfwake;

    public WfInstanceWholeProperty() {
    }

	public String getInstanceid() {
		return this.instanceid;
	}

	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	public String getAppsign() {
		return this.appsign;
	}

	public void setAppsign(String appsign) {
		this.appsign = appsign;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBdraft() {
		return this.bdraft;
	}

	public void setBdraft(String bdraft) {
		this.bdraft = bdraft;
	}

	public String getBizseqno() {
		return this.bizseqno;
	}

	public void setBizseqno(String bizseqno) {
		this.bizseqno = bizseqno;
	}

	public String getBxstatus() {
		return this.bxstatus;
	}

	public void setBxstatus(String bxstatus) {
		this.bxstatus = bxstatus;
	}

	public String getClientsign() {
		return this.clientsign;
	}

	public void setClientsign(String clientsign) {
		this.clientsign = clientsign;
	}

	public String getCustid() {
		return this.custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getCustname() {
		return this.custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getDepid() {
		return this.depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

	public String getExv10() {
		return this.exv10;
	}

	public void setExv10(String exv10) {
		this.exv10 = exv10;
	}

	public String getExv100() {
		return this.exv100;
	}

	public void setExv100(String exv100) {
		this.exv100 = exv100;
	}

	public String getExv19() {
		return this.exv19;
	}

	public void setExv19(String exv19) {
		this.exv19 = exv19;
	}

	public String getExv32() {
		return this.exv32;
	}

	public void setExv32(String exv32) {
		this.exv32 = exv32;
	}

	public String getExv50() {
		return this.exv50;
	}

	public void setExv50(String exv50) {
		this.exv50 = exv50;
	}

	public String getFlowtrace() {
		return this.flowtrace;
	}

	public void setFlowtrace(String flowtrace) {
		this.flowtrace = flowtrace;
	}

	public String getFormdata() {
		return this.formdata;
	}

	public void setFormdata(String formdata) {
		this.formdata = formdata;
	}

	public String getIswfset() {
		return this.iswfset;
	}

	public void setIswfset(String iswfset) {
		this.iswfset = iswfset;
	}

	public String getOrgid() {
		return this.orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getSpstatus() {
		return this.spstatus;
	}

	public void setSpstatus(String spstatus) {
		this.spstatus = spstatus;
	}

	public String getSysid() {
		return this.sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}

	public String getWfadmin() {
		return this.wfadmin;
	}

	public void setWfadmin(String wfadmin) {
		this.wfadmin = wfadmin;
	}

	public String getWfagain() {
		return this.wfagain;
	}

	public void setWfagain(String wfagain) {
		this.wfagain = wfagain;
	}

	public String getWfagent() {
		return this.wfagent;
	}

	public void setWfagent(String wfagent) {
		this.wfagent = wfagent;
	}

	public String getWfappid() {
		return this.wfappid;
	}

	public void setWfappid(String wfappid) {
		this.wfappid = wfappid;
	}

	public String getWfappname() {
		return this.wfappname;
	}

	public void setWfappname(String wfappname) {
		this.wfappname = wfappname;
	}

	public String getWfchange() {
		return this.wfchange;
	}

	public void setWfchange(String wfchange) {
		this.wfchange = wfchange;
	}

	public String getWfendtime() {
		return this.wfendtime;
	}

	public void setWfendtime(String wfendtime) {
		this.wfendtime = wfendtime;
	}

	public String getWfhangup() {
		return this.wfhangup;
	}

	public void setWfhangup(String wfhangup) {
		this.wfhangup = wfhangup;
	}

	public String getWfid() {
		return this.wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	public String getWfjobname() {
		return this.wfjobname;
	}

	public void setWfjobname(String wfjobname) {
		this.wfjobname = wfjobname;
	}

	public String getWfjump() {
		return this.wfjump;
	}

	public void setWfjump(String wfjump) {
		this.wfjump = wfjump;
	}

	public String getWfmainformid() {
		return this.wfmainformid;
	}

	public void setWfmainformid(String wfmainformid) {
		this.wfmainformid = wfmainformid;
	}

	public String getWfname() {
		return this.wfname;
	}

	public void setWfname(String wfname) {
		this.wfname = wfname;
	}

	public String getWfplanendtime() {
		return this.wfplanendtime;
	}

	public void setWfplanendtime(String wfplanendtime) {
		this.wfplanendtime = wfplanendtime;
	}

	public String getWfreaders() {
		return this.wfreaders;
	}

	public void setWfreaders(String wfreaders) {
		this.wfreaders = wfreaders;
	}

	public String getWfrecall() {
		return this.wfrecall;
	}

	public void setWfrecall(String wfrecall) {
		this.wfrecall = wfrecall;
	}

	public String getWfreturnback() {
		return this.wfreturnback;
	}

	public void setWfreturnback(String wfreturnback) {
		this.wfreturnback = wfreturnback;
	}

	public String getWfsign() {
		return this.wfsign;
	}

	public void setWfsign(String wfsign) {
		this.wfsign = wfsign;
	}

	public String getWfstarttime() {
		return this.wfstarttime;
	}

	public void setWfstarttime(String wfstarttime) {
		this.wfstarttime = wfstarttime;
	}

	public String getWfstatus() {
		return this.wfstatus;
	}

	public void setWfstatus(String wfstatus) {
		this.wfstatus = wfstatus;
	}

	public String getWfurge() {
		return this.wfurge;
	}

	public void setWfurge(String wfurge) {
		this.wfurge = wfurge;
	}

	public String getWfwake() {
		return this.wfwake;
	}

	public void setWfwake(String wfwake) {
		this.wfwake = wfwake;
	}

}