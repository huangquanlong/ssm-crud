package com.yuchengtech.bcrm.system.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.upload.FileTypeConstance;

/**
 * 系统状态监控
 * @author zm
 * @since 2013-03-20
 */
@ParentPackage("json-default")
@Action(value="/sysStatusMonitoring", results={
    @Result(name="success", type="json"),
})
public class SysStatusMonitoringAction  {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	private HttpServletRequest request;
	private long usedSum = 0;// 附件目录已用空间大小
	private long sumCount = 0;// 附件空间总大小
	private long freeCount = 0;// 附件目录剩余空间大小
    public void index(){
    }
    /***
     * 获取附件空间大小，标题
     * @return
     */
    public Map getChart(){
    	String sysUploadUrl = FileTypeConstance.getSystemProperty("sysUpload");
        long startTime = System.currentTimeMillis();
        String os = System.getProperty("os.name");
        Map<String, Object> chart = new HashMap<String, Object>();
        try
        {
            String path = sysUploadUrl;//"C:\\WINDOWS";
            File contents = new File(path);// 文件夹目录
            
          //如果文件夹不存在则创建    
            if  (!contents.exists()  && !contents.isDirectory())      
            {       
            	contents.mkdirs();
            } 
            if("Linux".equals(os))
            {
            	List<String> resetlist = getLinuxFreeDiskSpace(path);
            	// 因为命令查询出来的空间数据时K为单位，所以除以1024
            	sumCount = Long.parseLong(resetlist.get(0)) * 1024;
            	usedSum = Long.parseLong(resetlist.get(1)) * 1024;
            	freeCount = Long.parseLong(resetlist.get(2)) * 1024;
            }
            if("Windows NT".equals(os) ||  "Windows 2000".equals(os) || "Windows 2003".equals(os) ||  "Windows XP".equals(os) || "Windows 7".equals(os))
            {
            	sumCount = getFreeDiskSpace(path.substring(0, 3));
            	usedSum = getFileSize(contents);
            }
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        chart.put("caption", "附件空间大小(单位：GB 总大小："+this.FormetFileSize(sumCount)+"GB)");
//    	chart.put("subcaption", (new Date()).toString());
    	chart.put("bgratio", "24");
    	chart.put("formatnumberscale", "0");
    	chart.put("basefontsize", "13");
    	
    	long endTime = System.currentTimeMillis();
        // 返回 chart到前台
    	return chart;
    }
    /***
     * 获取附件空间数据
     * @return
     */
    public List getData(){
    	List set = new ArrayList();
    	Map<String, String> setMap1 = new HashMap<String, String>();
    	setMap1.put("label", "已使用");
    	setMap1.put("value", this.FormetFileSize(usedSum));
    	
    	Map<String, String> setMap2 = new HashMap<String, String>();
    	setMap2.put("label", "未使用");
    	if(freeCount != 0)
    	{
    		setMap2.put("value", this.FormetFileSize(freeCount));
    	} else {
    		setMap2.put("value", this.FormetFileSize(sumCount - usedSum));
    	}
    	set.add(setMap1);
    	set.add(setMap2);
    	return set;
    }
    //  递归
    //	取得文件夹大小
    private long getFileSize(File f)throws Exception
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                size = size + getFileSize(flist[i]);
            } else
            {
                size = size + flist[i].length();
            }
        }
        return size;
    }
    //	转换文件大小
    private String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        fileSizeString = df.format((double) fileS / 1073741824) ;
        
        return fileSizeString;
    }
    /***
     * 通过系统命令获取磁盘空间剩余大小（WINDOWS）
     * @param dirName
     * @return
     * @throws Exception
     */
    public long getFreeDiskSpace(String dirName) throws Exception{
	    //如果目录不存在
	    File dir = new File(dirName); 
	    if(!dir.exists()) return -1;
        try{
            // guess correct 'dir' command by looking at the
            // operating system name
            String os = System.getProperty("os.name");
            String command = "";
            if ("Windows NT".equals(os) ||  "Windows 2000".equals(os) || "Windows 2003".equals(os) ||  "Windows XP".equals(os) || "Windows 7".equals(os)){
               command = "cmd.exe /c dir " + dir.getAbsolutePath();
            }else{
            	return -1;
            }
            // run the dir command on the argument directory name
            Runtime runtime = Runtime.getRuntime();
            Process process = null;
            process = runtime.exec(command);
            if (process == null){
                return -1;
            }
            // read the output of the dir command
            // only the last line is of interest
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String freeSpace = null;
            while ((line = in.readLine()) != null){
                freeSpace = line;
            }
            if (freeSpace == null){
                return -1;
            }
            process.destroy();
            
            freeSpace = freeSpace.trim();
            freeSpace = freeSpace.replaceAll("[\\.|,]", "");
            String[] items = freeSpace.split(" ");
            
            int index = 1;
            while (index < items.length) {
                try{
                    long bytes = Long.parseLong(items[index++]);
                    return bytes;
                   }catch (NumberFormatException nfe){}
            }
            return -1;
        }
        catch (Exception e){
        	throw e;
        }
    }
    
    /***
     * 通过系统命令获取磁盘空间剩余大小
     * @param dirName
     * @return
     * @throws Exception
     */
    public List<String> getLinuxFreeDiskSpace(String dirName) throws Exception{
    	List<String> nulllist=new ArrayList();
	    //如果目录不存在
	    File dir = new File(dirName); 
        try{
            // guess correct 'dir' command by looking at the
            // operating system name
            String os = System.getProperty("os.name");
            String command = "df";// 查询磁盘信息的命令
            // run the dir command on the argument directory name
            Runtime runtime = Runtime.getRuntime();
            Process process = null;
            process = runtime.exec(command);
            if (process == null){
                return nulllist;
            }
            // read the output of the dir command
            // only the last line is of interest
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String freeSpace = null;
            List<String> list=new ArrayList();
            while ((line = in.readLine()) != null){
                freeSpace = line;
                String[] lst = freeSpace.split(" ");
                
                for(int i=0; i<lst.length; i++){
               	 if (!lst[i].equals("")){
               		 list.add(lst[i]);
               	 }			
               	 
                }
                /***范例
                 * 文件系统               1K-块        已用     可用 已用% 挂载点
					/dev/mapper/VolGroup00-LogVol00
					                     928160260 245570168 634681640  28% /
					/dev/sda1               101086     12645     83222  14% /boot
					tmpfs                  8213308       968   8212340   1% /dev/shm
                 */
                if(list.size()==5)
                {
	                if(list.get(4).equals("/"))// 判断挂载点是否为根目录，若是则返回LIST
	              	 {
	                	return list;
	              	 }
                }
                if(list.size()==6)
                {
	                if(list.get(5).equals("/"))// 判断挂载点是否为根目录，若是则返回LIST
	              	 {
	                	return list;
	              	 }
                }
                // 如果不是遍历到预定的记录则 
                list=new ArrayList();
            }
            if (freeSpace == null){
                return nulllist;
            }
            process.destroy();
            return nulllist;
        }
        catch (Exception e){
        	throw e;
        }
    }
}


