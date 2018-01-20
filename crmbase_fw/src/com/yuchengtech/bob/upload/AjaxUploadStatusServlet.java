package com.yuchengtech.bob.upload;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuchengtech.bob.upload.FileUploadListener.FileUploadStats;

public class AjaxUploadStatusServlet extends HttpServlet {

    private static final long serialVersionUID = -7291696282160902842L;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FileUploadStats stats = (FileUploadStats) request.getSession().getAttribute("FILE_UPLOAD_STATS");
        if (stats != null) {
            response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.addHeader("Pragma", "no-cache");
            double percentComplete = stats != null ? percentComplete = stats.getPercentComplete() : 0.0;
            long bytesProcessed = stats.getBytesRead();
            long sizeTotal = stats.getTotalSize();
            DecimalFormat decFormatter = new DecimalFormat(".00");
            long elapsedTimeInMilliseconds = stats.getElapsedTimeInMilliseconds();
            double bytesPerMillisecond = bytesProcessed / (elapsedTimeInMilliseconds + 0.00001);
            long estimatedMillisecondsLeft = (long) ((sizeTotal - bytesProcessed) / (bytesPerMillisecond + 0.00001));
            String timeLeft = null;
            if ((estimatedMillisecondsLeft / 3600) > 24) {
                timeLeft = (long) (estimatedMillisecondsLeft / 3600) + "hours";
            } else {
                Calendar c = new GregorianCalendar();
                long ad = estimatedMillisecondsLeft - (c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET));
                timeLeft = new SimpleDateFormat("HH:mm:ss").format(ad);
            }
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            
            out.print("_annaSize='"+(double)sizeTotal+"';" +
            		"Ext.Msg.show({title : '上传成功',msg : ");
            out.print("\"<div class='prog-border'><div class='prog-bar' style='width: " + (percentComplete * 100) + "%;'></div></div>");
            out.print("<table><tr><td class='st' width='250px;'>" + timeLeft +
                    " (at " + decFormatter.format(bytesPerMillisecond) + "kb/sec)</td><td class='st'>" +
                    decFormatter.format((double) bytesProcessed / (1000 * 1000)) + 
                    " MB / " + decFormatter.format((double)sizeTotal / (1000 * 1000)) +
                    " MB ( " + Math.ceil((percentComplete * 100)) + "% )</td></tr></table>\"");
            out.print(",buttons : Ext.Msg.OK,icon : Ext.Msg.INFO});");
            out.flush();
        } else {
             PrintWriter out = response.getWriter();
             out.println("setTimeout(\"stoppage_checkUploadStatus()\", 1000); toggleButton('submit_button');");
             out.flush();
        }
    }
    
}
