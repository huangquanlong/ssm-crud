package com.yuchengtech.bcrm.echain;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;

import com.yuchengtech.bob.vo.AuthUser;

public class StudioJNLPServlet extends HttpServlet {
  private static final String CONTENT_TYPE = "application/x-java-jnlp-file;charset=UTF-8";
  //Initialize global variables
  public void init() throws ServletException {
  }
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession();
	AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
	String userid = auth.getUserId();
    String orgid= auth.getUnitId();
    //system.out.printlnln("orgid==="+orgid+";userid==="+userid);
    if(userid==null||userid.equals(""))
      userid="%userid%";
    if(orgid==null||orgid.equals(""))
      orgid="%orgid%";
    try {
      //读文件内容 
      String docpath = request.getSession().getServletContext().getRealPath("/echain/studio/eChainStudio.jnlp");
      FileInputStream in =  new FileInputStream(new File(docpath));
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      int intLength = 0;
      while((intLength = in.read())!=-1){
          bout.write(intLength);
        }
      byte[] bts = bout.toByteArray();
      String str = new String(bts,"UTF-8");
      str = str.replaceAll("%userid%",userid);
      str = str.replaceAll("%orgid%",orgid).trim();
      bout.close();
      in.close();
      out.println(str);
      out.flush();
      out.close();
    }
    catch (Exception ex) {}
  }

  //Clean up resources
  public void destroy() {
  }
}
