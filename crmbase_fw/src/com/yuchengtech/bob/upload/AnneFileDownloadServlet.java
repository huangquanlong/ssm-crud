package com.yuchengtech.bob.upload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuchengtech.bob.download.FileUtil;

public class AnneFileDownloadServlet extends HttpServlet {

    private static final long serialVersionUID = -4671069818841645240L;
 
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
    	String filename = request.getParameter("filename");
        String annexeName = request.getParameter("annexeName");
//        annexeName = new String(annexeName.getBytes("ISO8859-1"), "UTF-8");
        String filepath;
        if(filename != null && !"".equals(filename)&& annexeName != null && !"".equals(annexeName)) {
        	//当文件名称为汉字的时候 该验证会出现问题 现除去后一项验证
        	//修改前：(!FileUtil.checkFileName(filename) || !FileUtil.checkFileName(annexeName))
        	//修改后 ：(!FileUtil.checkFileName(filename))
        	if(!FileUtil.checkFileName(filename))  {
        		request.getRequestDispatcher("./contents/pages/error/noFileError.jsp").forward(request, response);
        	}
            StringBuilder builder = new StringBuilder();
            builder.append(FileTypeConstance.getUploadPath());
            if (!builder.toString().endsWith(File.separator)) {
                builder.append(File.separator);
            }
            builder.append(File.separator);
            builder.append(filename);
            filepath = builder.toString();
            File file = new File(filepath);
            if (file.exists()) {
                response.setContentType(FileTypeConstance.getHttpheaderofFile(filename));
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Length", String.valueOf(file.length()));
                response.setHeader("Content-Disposition", "attachment; filename=\""+URLEncoder.encode(new String(FileTypeConstance.getFileNameWithoutExt(annexeName).getBytes("UTF-8"),"UTF-8"),"UTF-8") 
                       + FileTypeConstance.getExtFileName(annexeName) + "\"");
                ServletOutputStream outputStream = response.getOutputStream();
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
                int data;
                while((data = inputStream.read()) != -1) {
                    outputStream.write(data);
                }
                inputStream.close();
                outputStream.close();
            }else{
        		request.getRequestDispatcher("./contents/pages/error/noFileError.jsp").forward(request, response);
            }
        }
    }
}
