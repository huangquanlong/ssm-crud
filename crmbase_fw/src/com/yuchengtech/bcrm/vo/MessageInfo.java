package com.yuchengtech.bcrm.vo;
//通讯报文包含属性
public class MessageInfo {
	
//channel,渠道标志   2;| tranType,业务代码4;| branch,机构代码6|content,内容
		private String channel;
		private String tranType;
		private String branch;
		private String content;
		private String returnCode;
		
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getTranType() {
			return tranType;
		}
		public void setTranType(String tranType) {
			this.tranType = tranType;
		}
		public String getBranch() {
			return branch;
		}
		public void setBranch(String branch) {
			this.branch = branch;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getReturnCode() {
			return returnCode;
		}
		public void setReturnCode(String returnCode) {
			this.returnCode = returnCode;
		}
		
}
