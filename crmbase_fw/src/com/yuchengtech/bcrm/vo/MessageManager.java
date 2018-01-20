package com.yuchengtech.bcrm.vo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.yuchengtech.bob.upload.FileTypeConstance;

public class MessageManager {
	/*
	 * 通讯报文	
	 channel,渠道标志   2;| tranType,业务代码4;| branch,机构代码6|returnCode,返回码4|content,内容
	*/
	public String reqMessage(List<MessageInfo> list){
		   StringBuffer messStr = new StringBuffer();
		for(MessageInfo info : list){
			if(info.getChannel()!=null&&info.getChannel().length()>2){
				return ConstantUtil.returnCode_2;
			}else if(info.getTranType()!=null&&info.getTranType().length() >4){
				return ConstantUtil.returnCode_2;
			}else if(info.getBranch()!=null&&info.getBranch().length() >6){
				return ConstantUtil.returnCode_2;
			}else if(info.getContent()!=null&&info.getContent().length()>255){
				return ConstantUtil.returnCode_2;
			}else if(info.getContent()==null||"".equals(info.getContent())){
				 return ConstantUtil.returnCode_1;
			 }else{
				 if(info.getChannel()==null){
					 for(int i=2;i>0;i--){
						 messStr.append(" ");
					 }
				 }if(info.getChannel().length()<2){
					 messStr.append(info.getChannel());
					 for(int i=2;i>info.getChannel().length();i--){
						 messStr.append(" ");
					 }
				 }
				 if(info.getTranType()==null){
					 for(int i=4;i>0;i--){
						 messStr.append(" ");
					 }
				 }else if(info.getTranType().length()<4){
					 messStr.append(info.getTranType());
					 for(int i=4;i>info.getTranType().length();i--){
						 messStr.append(" ");
					 }
				 }
				 if(info.getBranch()==null){
					 for(int i=6;i>0;i--){
						 messStr.append(" ");
					 }
				 }else if(info.getBranch().length()<6){
					 messStr.append(info.getBranch());
					 for(int i=6;i>info.getBranch().length();i--){
						 messStr.append(" ");
					 }
				 }
				 if(info.getContent().length()<255){
					 messStr.append(info.getContent());
					 for(int i=255;i>info.getContent().length();i--){
						 messStr.append(" ");
					 }
				 }
				 messStr.append("\r\n");
				 //发送报文
				 return ConstantUtil.returnCode_0;
			 }
			 
		}
		return null;
	}
//	短信上行报文
	public String reqMessageUp(String channel,String mobile,String content){
		if(channel!=null&&channel.length()>2){
			return ConstantUtil.returnCode_2;
		}else if(mobile!=null&&mobile.length()>12){
			return ConstantUtil.returnCode_2;
		}else if(content!=null&&content.length()>255){
			return ConstantUtil.returnCode_2;
		}else if(mobile==null||"".equals(mobile)||content==null||"".equals(content)){
			return ConstantUtil.returnCode_1;
		}else{
			StringBuffer  str =new StringBuffer();
			if(channel==null){
				for(int i=2;i>0;i--){
					 str.append(" ");
				 }
			}else if(channel.length()<2){
				str.append(channel);
				 for(int i=2;i>channel.length();i--){
					 str.append(" ");
				 }
			 }
			if(mobile.length()<12){
				str.append(mobile);
				for(int i=12;i>mobile.length();i--){
					str.append(" ");
				}
			}
			if(content.length()<255){
				str.append(content);
				for(int i=255;i>content.length();i--){
					str.append(" ");
				}
			}
			str.append("\r\n");
			return ConstantUtil.returnCode_up;
		}
	}
	//批量文件
	public String batchFile(String channel,List<BatchFileMessage> list) throws IOException{
		 StringBuilder fileName = new StringBuilder();
		 fileName.append(FileTypeConstance.getUploadPath());
		 if (!fileName.toString().endsWith(File.separator)) {
			 fileName.append(File.separator);
         }
		 fileName.append(File.separator);
		 if (! new File(fileName.toString()).exists()) {
	            new File(fileName.toString()).mkdir();
	        }
		 if(channel==null&&"".equals(channel)){
			 return ConstantUtil.returnCode_1;
		 }else if(channel!=null&&channel.length()>2){
			 return ConstantUtil.returnCode_2;
		 }else if(channel.length()<2){
			fileName.append(channel);
			for(int i=2;i>channel.length();i--){
				fileName.append(" ");
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timeStamp=sdf.format(new Date());
		fileName.append(timeStamp);
		fileName.append(".txt");
		
		File  fp = new File(fileName.toString());
		
		
		for(BatchFileMessage obj:list){
			if(obj.getServiceID()!=null&&obj.getServiceID().length()>6){
				return ConstantUtil.returnCode_2;
			}else if(obj.getBranch()!=null&&obj.getBranch().length()>6){
				return ConstantUtil.returnCode_2;
			}else if(obj.getBatchCount()!=null&&obj.getBatchCount().length()>6){
				return ConstantUtil.returnCode_2;
			}else if(obj.getStartDate()!=null&&obj.getStartDate().length()>8){
				return ConstantUtil.returnCode_2;
			}else if(obj.getStartTime()!=null&&obj.getStartTime().length()>6){
				return ConstantUtil.returnCode_2;
			}else if(obj.getEndDate()!=null&&obj.getEndDate().length()>8){
				return ConstantUtil.returnCode_2;
			}else if(obj.getEndTime()!=null&&obj.getEndTime().length()>6){
				return ConstantUtil.returnCode_2;
			}else if(obj.getPhone()!=null&&obj.getPhone().length()>6){
				return ConstantUtil.returnCode_2;
			}else if(obj.getContent()!=null&&obj.getContent().length()>6){
				return ConstantUtil.returnCode_2;
			}else if(obj.getPhone()==null||"".equals(obj.getPhone())||obj.getContent()==null||"".equals(obj.getContent())){
				return ConstantUtil.returnCode_1;
			}else {
				StringBuffer fl = new StringBuffer();
				 if(obj.getServiceID()==null){
					 for(int i=6;i>0;i--){
						 fl.append(" ");
					 }
				 }else if(obj.getServiceID().length()<6){
					 fl.append(obj.getServiceID());
					 for(int i=6;i>obj.getServiceID().length();i--){
						 fl.append(" ");
					 }
				 }
				 if(obj.getBranch()==null){
					 for(int i=6;i>0;i--){
						 fl.append(" ");
					 }
				 }else if(obj.getBranch().length()<6){
					 fl.append(obj.getBranch());
					 for(int i=6;i>obj.getBranch().length();i--){
						 fl.append(" ");
					 }
				 }
				 if(obj.getBatchCount()==null){
					 for(int i=6;i>0;i--){
						 fl.append(" ");
					 }
				 }else if(obj.getBatchCount().length()<6){
					 fl.append(obj.getBatchCount());
					 for(int i=6;i>obj.getBatchCount().length();i--){
						 fl.append(" ");
					 }
				 }
				 if(obj.getStartDate()==null){
					 for(int i=8;i>0;i--){
						 fl.append(" ");
					 }
				 }else if(obj.getStartDate().length()<8){
					 fl.append(obj.getStartDate());
					 for(int i=8;i>obj.getStartDate().length();i--){
						 fl.append(" ");
					 }
				 }
				 if(obj.getStartTime()==null){
					 for(int i=6;i>0;i--){
						 fl.append(" ");
					 }
				 }else if(obj.getStartTime().length()<6){
					 fl.append(obj.getStartTime());
					 for(int i=6;i>obj.getStartTime().length();i--){
						 fl.append(" ");
					 }
				 }
				 if(obj.getEndDate()==null){
					 for(int i=8;i>0;i--){
						 fl.append(" ");
					 }
				 }else if(obj.getEndDate().length()<8){
					 fl.append(obj.getEndDate());
					 for(int i=8;i>obj.getEndDate().length();i--){
						 fl.append(" ");
					 }
				 }
				 if(obj.getEndTime()==null){
					 for(int i=6;i>0;i--){
						 fl.append(" ");
					 }
				 }else if(obj.getEndTime().length()<6){
					 fl.append(obj.getEndTime());
					 for(int i=6;i>obj.getEndTime().length();i--){
						 fl.append(" ");
					 }
				 }
				 if(obj.getPhone().length()<12){
					 fl.append(obj.getPhone());
					 for(int i=12;i>obj.getPhone().length();i--){
						 fl.append(" ");
					 }
				 }
				 if(obj.getContent().length()<255){
					 fl.append(obj.getContent());
					 for(int i=255;i>obj.getContent().length();i--){
						 fl.append(" ");
					 }
				 }
				 fl.append("\r\n");
				 byte[] b = fl.toString().getBytes();
				 FileOutputStream ois = new FileOutputStream(fp,true);
				 ois.write(b);
			}
		}
		return null;
	}
	//普通转发类短信接口,请求报文体
	public String tranPort(String tranName,String date,String time,String content,String phone){
		if(tranName!=null&&tranName.length()>30){
			return ConstantUtil.returnCode_2;
		}else if(date!=null&&date.length()>8){
			return ConstantUtil.returnCode_2;
		}else if(time!=null&&time.length()>8){
			return ConstantUtil.returnCode_2;
		}else if(phone==null||"".equals(phone)||content==null||"".equals(content)){
			return ConstantUtil.returnCode_1;
		}else{
			StringBuffer  str =new StringBuffer();
			if(tranName==null){
				for(int i=30;i>0;i--){
					 str.append(" ");
				 }
			}else if(tranName.length()<30){
				str.append(tranName);
				 for(int i=30;i>tranName.length();i--){
					 str.append(" ");
				 }
			 }
			if(date==null){
				for(int i=8;i>0;i--){
					str.append(" ");
				}
			}else if(date.length()<8){
				str.append(date);
				for(int i=8;i>date.length();i--){
					str.append(" ");
				}
			}
			if(time==null){
				for(int i=8;i>0;i--){
					str.append(" ");
				}
			}else if(time.length()<8){
				str.append(time);
				for(int i=8;i>time.length();i--){
					str.append(" ");
				}
			}
			if(phone.length()<12){
				str.append(phone);
				for(int i=12;i>phone.length();i--){
					str.append(" ");
				}
			}
			if(content.length()<255){
				str.append(content);
				for(int i=255;i>content.length();i--){
					str.append(" ");
				}
			}
			str.append("\r\n");
			return ConstantUtil.returnCode_up;	
		}
	}
}
