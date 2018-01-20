package com.yuchengtech.bcrm.vo;

public class ConstantUtil {
	public final static Long GROUP_REL_USERTYPE_USER = 1L;//代表用户
	public final static Long GROUP_REL_USERTYPE_CUST = 2L;//代表客户
	
	public final static Long GROUP_TYPE_USER = 1L;// 用户创建群组
	public final static Long GROUP_TYPE2_SYS = 2L;// 系统自动创建群组
	//消息收发方式
	public final static Long MESSAGE_SEND_TYPE_MESSAGE =0L;//收发类型：站内信
	public final static Long MESSAGE_SEND_TYPE_TEL = 1L;//收发类型：短信
	public final static Long MESSAGE_SEND_TYPE_EMAIL =2L;//收发类型：邮件
	//收信人用户类型
	public final static Long RECEIVE_USER_TYPE_USER =0L;//用户
	public final static Long RECEIVE_USER_TYPE_CUST =1L;//客户
	public final static Long RECEIVE_USER_TYPE_GROUP = 2L;
	//发送人类型
	public final static Long SEND_USER_TYPE_USER =0L;//用户
	public final static Long SEND_USER_TYPE_CUST =1L;//客户
	public final static Long SEND_USER_TYPE_GROUP = 2L;
	//读取标志
	public final static Long MESSAGE_SEND_IS_READ_FLAG_NO = 0L;//未读
	public final static Long MESSAGE_SEND_IS_READ_FLAG_YES = 1L;//已读
	//发送标识
	public final static String MESSAGE_SEND_IS_SEND_FLAG_NO ="0";//未发送
	public final static String MESSAGE_SEND_IS_SEND_FLAG_YES ="1";//已发送
	public final static Long  SEND_CHANNEL_USER =0L;//手动发送
	public final static Long  SEND_CHANNEL_AUTO =1L;//自动发送
	//删除标识
	public final static String MESSAGE_SEND_IS_DEL_FLAG_NO = "0";//未删除状态
	public final static String MESSAGE_SEND_IS_DEL_FLAG_YSE = "1";//删除状态
	//消息类别
	public final static Long  MESSAGE_TYPE_GENERAL = 0L;//普通消息
	public final static Long  MESSAGE_TYPE_BLESS = 1L;//祝福类消息
	public final static Long  MESSAGE_TYPE_PRODUCT = 2L;//产品类消息
	public final static Long  MESSAGE_TYPE_ACCOUNT = 3L;//账户类消息

//	returnCode,返回码4	
	/*0000--短信平台接收成功
	0001--数据过短
	0002--无效的业务代码和交易代码
	0003--无效的服务类型,该服务尚未被支持
	0004--定位服务时出错
	0005--未上送电话号码
	0006--日期长度不对
	0007--日期格式不对
	0008--无序列
	0009--提交业务接收表SQL出错
	0010—提交业务数据丢失表SQL出错
	0011--process的入口参数不是List，数据格式不对*/
	public final static String returnCode_0 = "0000";
	public final static String returnCode_1 = "0001";
	public final static String returnCode_2 = "0002";
	public final static String returnCode_3 = "0003";
	public final static String returnCode_4 = "0004";
	public final static String returnCode_5 = "0005";
	public final static String returnCode_6 = "0006";
	public final static String returnCode_7 = "0007";
	public final static String returnCode_8 = "0008";
	public final static String returnCode_9 = "0009";
	public final static String returnCode_10 = "0010";
	
	public final static String returnCode_up= "00";
}
