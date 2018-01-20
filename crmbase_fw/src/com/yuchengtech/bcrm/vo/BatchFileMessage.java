package com.yuchengtech.bcrm.vo;
//批量文件包含的属性
public class BatchFileMessage {
	// ( channel, serviceID, branch, batchCount, startDate, startTime, endDate,
	// endTime, phone, content)
	private String serviceID;
	private String branch;
	private String batchCount;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String phone;
	private String content;
	
	public BatchFileMessage(){
		
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		if(serviceID==null){
			this.serviceID ="";
		}else{
		this.serviceID = serviceID;
		}
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		if(branch==null){
			this.branch="";
		}else
		this.branch = branch;
	}

	public String getBatchCount() {
		return batchCount;
	}

	public void setBatchCount(String batchCount) {
		if(batchCount==null){
			this.batchCount="";
		}else
		this.batchCount = batchCount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		if(startDate==null){
			this.startDate="";
		}else
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		if(startTime==null){
			this.startTime="";
		}else
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		if(endDate==null){
			this.endDate="";
		}else
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		if(endTime==null){
			this.endTime="";
		}else
		this.endTime = endTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		if(phone==null){
			this.phone="";
		}else
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if(content==null){
			this.content="";
		}else
		this.content = content;
	}

}
