package com.yuchengtech.bcrm.common.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.upload.FileTypeConstance;

/**
 * @describtion: 图片展示调用action
 *
 * @author : lhqheli (email: lhqheli@gmail.com)
 * @date : 2014-7-25 下午15:30:44
 */
@Action("/imgshow")
public class ImgShowAction{
	
	private static Logger log = Logger.getLogger(ImgShowAction.class);
	
    /**
     * 获取要展示的图片并输出到客户端
     */
    public void index(){
    	ActionContext ctx = ActionContext.getContext();
    	HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
    	HttpServletResponse response = (HttpServletResponse)ctx.get(ServletActionContext.HTTP_RESPONSE);
    	
    	String filepath = FileTypeConstance.getSystemProperty("uploadImgPath")+File.separator + request.getParameter("path");
		File file =new File(filepath);
		
        String fileName=file.getName();
        try {
			fileName= new String(fileName.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setContentType("text/plain;charset=ISO8859-1");  //设置 response 的编码方式
		response.setContentLength((int)file.length());  //写明要下载的文件的大小
        response.setHeader("Content-disposition","attachment;filename="+fileName);  //设定输出文件头 
		FileInputStream fis = null; //读出文件到 i/o 流
		BufferedInputStream buff = null;
		OutputStream myout = null;
		try {
			fis = new FileInputStream(file);
			buff = new BufferedInputStream(fis);
			byte [] b=new byte[1024];             //相当于我们的缓存
			long k=0;                             //该值用于计算当前实际下载了多少字节
			myout = response.getOutputStream();   //从 response 对象中得到输出流,准备下载
			while( k < file.length()){   //开始循环下载
				int j=buff.read(b,0,1024);
				k+=j;
				myout.write(b,0,j);     //将 b 中的数据写到客户端的内存
			}
			myout.flush();
		} catch (FileNotFoundException e) {
			log.error("文件不存在: " + filepath);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				myout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				buff.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
}
