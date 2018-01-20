package com.yuchengtech.bob.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.yuchengtech.bob.upload.FileUploadListener.FileUploadStats;
import com.yuchengtech.bob.upload.FileUploadListener.FileUploadStatus;

/**
 * @describtion: 
 * 	20140828,增加客户、用户头像上传路径处理
 *
 */
public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = -4671069818841645240L;    
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ServletInputStream in = null;
    	String path = FileTypeConstance.getUploadPath();
    	String phoneId = request.getParameter("phoneId");
    	String MType = request.getParameter("MType");
    	FileOutputStream fs = null; 
    	try{
    		in = request.getInputStream();
    		if("1".equals(MType))//根据设备不同。录音文件不同
    			fs = new FileOutputStream(path+"\\"+"PHONE"+phoneId+".mp3");
    		else if("2".equals(MType))
    			fs = new FileOutputStream(path+"\\"+"PHONE"+phoneId+".wav");
    		 byte[] b = new byte[1024 * 5]; 
    		 int len; 
    		 while ( (len = in.read(b)) != -1) {
    			 fs.write(b, 0, len);                    
    		}  
    		 fs.flush();
    		 fs.close(); 
    		 in.close(); 
    	}catch(Exception ee){
			ee.printStackTrace();
		}finally{
			if(fs!=null)
				fs.close(); 
			if(in!=null)
				in.close(); 
		}
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String MType = request.getParameter("MType");
    	String phoneId = request.getParameter("phoneId");
		String fileName = "";
        String isImport = request.getParameter("isImport");
        //判断是否头像上传(客户、用户)
        String isImage = request.getParameter("isImage");
        try {
        	response.setContentType("text/HTML");
            PrintWriter out = response.getWriter();
            StringBuilder builder = new StringBuilder();
            if(null==isImport && null==isImage){
            	builder.append(FileTypeConstance.getUploadPath());
            }else if(null==isImport && "isImage".equals(isImage)){
            	builder.append(FileTypeConstance.getSystemProperty("uploadImgPath"));
            }else {
            	builder.append(FileTypeConstance.getImportTmpPath());
            }
            if (!builder.toString().endsWith(File.separator)) {
                builder.append(File.separator);
            }
            builder.append(File.separator);
            String saveFilePath = builder.toString();
            if (! new File(saveFilePath).exists()) {
                new File(saveFilePath).mkdir();
            }
            FileUploadListener listener = new FileUploadListener(request.getContentLength());
            request.getSession().setAttribute("FILE_UPLOAD_STATS", listener.getFileUploadStats());
            DiskFileItemFactory factory = new MonitoredDiskFileItemFactory(listener);
            factory.setRepository(new File(saveFilePath));
            ServletFileUpload upload = new ServletFileUpload(factory);
            //设置上传文件最大值，单位字节
            upload.setSizeMax(FileTypeConstance.getMaxUploadFileSize());
            List<FileItem> items = upload.parseRequest(request);
            for (Iterator<FileItem> i = items.iterator(); i.hasNext();) {
                FileItem fileItem = i.next();
                if("1".equals(MType)){//根据录音文件不同做区分
                	fileName =  "PHONE"+phoneId+".mp3";
        			fileItem.write(new File(saveFilePath + fileName));
                }else if("2".equals(MType)){
                	fileName =  "PHONE"+phoneId+".wav";
        			fileItem.write(new File(saveFilePath + fileName));
                }else if (!fileItem.isFormField()) {
                		fileName = FileTypeConstance.getSeqFileName() + FileTypeConstance.getExtFileName(fileItem.getName());
                		fileItem.write(new File(saveFilePath + fileName));
                }
            }
            
            /**
             * TODO 添加以下json语句可以消除form表单提交时的JS错误。
             */
            out.append("{success:true,realFileName:'"+fileName+"'}");
            out.flush();
        } catch (Exception e) {
        	FileUploadStats stats = new FileUploadListener.FileUploadStats();
            stats.setCurrentStatus(FileUploadStatus.ERROR);
            request.getSession().setAttribute("FILE_UPLOAD_STATS", stats);
            PrintWriter out = response.getWriter();
            /**
             * TODO 添加以下json语句可以消除form表单提交时的JS错误。
             */
            if("SizeLimitExceededException".equals(e.getClass().getSimpleName()))
            	out.append("{success:false,reason:'SizeLimitExceeded'}");
            else
            	out.append("{success:false,reason:'other'}");
            out.flush();
            e.printStackTrace();
        }
    }
}
