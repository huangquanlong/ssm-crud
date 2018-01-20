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

//import com.yuchengtech.bob.download.FileUtil;

public class FileDownloadServlet extends HttpServlet {

    private static final long serialVersionUID = -4671069818841645240L;
 
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    //wzy,20150302,modify:
    //1、去掉文件名是否合法的校验，原因是影响中文名称文件的下载，而且，也没有必要校验文件名是否合法
    //2、如果文件不存在，给出提示
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getParameter("filename");
        String filepath;
        if(filename != null && !"".equals(filename)) {
//        	if(!FileUtil.checkFileName(filename))  {
//        		request.getRequestDispatcher("./contents/pages/error/noFileError.jsp").forward(request, response);
//            }
            StringBuilder builder = new StringBuilder();
            builder.append(FileTypeConstance.getExportPath());
            if (!builder.toString().endsWith(File.separator)) {
                builder.append(File.separator);
            }
            builder.append(File.separator);
            builder.append(filename);
            filepath = builder.toString();
            File file = new File(filepath);
            if (file.exists()) {
            	//文件存在，直接下载
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Length", String.valueOf(file.length()));
                filename = URLEncoder.encode(filename, "UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + 
                        filename + "\"");
                ServletOutputStream outputStream = response.getOutputStream();
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
                int data;
                while((data = inputStream.read()) != -1) {
                    outputStream.write(data);
                }
                inputStream.close();
                outputStream.close();
            }else{
            	//文件不存在，跳转到“文件不存在”页面
            	request.getRequestDispatcher("./contents/pages/error/noFileError.jsp").forward(request, response);
            }
        }
    }
}
