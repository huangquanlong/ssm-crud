package com.yuchengtech.bob.upload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuchengtech.bob.download.FileUtil;

public class TemplateFileDownServlet extends HttpServlet {

    private static final long serialVersionUID = -4671069818841645240L;
 
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getParameter("filename");
        String filepath;
        if(filename != null && !"".equals(filename)) {
        	if(!FileUtil.checkFileName(filename))  {
        		request.getRequestDispatcher("./contents/pages/error/noFileError.jsp").forward(request, response);
            }
            StringBuilder builder = new StringBuilder();
            builder.append(FileTypeConstance.getImportTempaltePath());
            if (!builder.toString().endsWith(File.separator)) {
                builder.append(File.separator);
            }
            builder.append(File.separator);
            builder.append(filename);
            filepath = builder.toString();
            File file = new File(filepath);
            if (file.exists()) {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Length", String.valueOf(file.length()));
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
        		request.getRequestDispatcher("./contents/pages/error/noFileError.jsp").forward(request, response);
            }
        }
    }
}
