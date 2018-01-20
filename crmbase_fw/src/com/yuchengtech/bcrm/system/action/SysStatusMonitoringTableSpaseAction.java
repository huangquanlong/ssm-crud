package com.yuchengtech.bcrm.system.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.upload.FileTypeConstance;
/**
 * 系统状态监控-数据库表空间大小
 * @author zm
 * @since 2013-03-21
 */
@ParentPackage("json-default")
@Action(value="/sysStatusMonitoringTableSpase", results={
    @Result(name="success", type="json"),
})
public class SysStatusMonitoringTableSpaseAction  {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	private HttpServletRequest request;
	private String tablespace_name = "";// 表空间名
	private String total_size = "";// 总空间
	private String used_size = "";// 已用空间
	private String free_size = "";// 剩余空间
    public void index(){
    }
    /***
     * 获取表空间大小，标题
     * @return
     */
    public Map getChart() throws Exception{
    	 Properties props=System.getProperties(); //系统属性
    	 System.out.println("Java的运行环境版本："+props.getProperty("java.version"));
         System.out.println("Java的运行环境供应商："+props.getProperty("java.vendor"));
         System.out.println("Java供应商的URL："+props.getProperty("java.vendor.url"));
         System.out.println("Java的安装路径："+props.getProperty("java.home"));
         System.out.println("Java的虚拟机规范版本："+props.getProperty("java.vm.specification.version"));
         System.out.println("Java的虚拟机规范供应商："+props.getProperty("java.vm.specification.vendor"));
         System.out.println("Java的虚拟机规范名称："+props.getProperty("java.vm.specification.name"));
         System.out.println("Java的虚拟机实现版本："+props.getProperty("java.vm.version"));
         System.out.println("Java的虚拟机实现供应商："+props.getProperty("java.vm.vendor"));
         System.out.println("Java的虚拟机实现名称："+props.getProperty("java.vm.name"));
         System.out.println("Java运行时环境规范版本："+props.getProperty("java.specification.version"));
         System.out.println("Java运行时环境规范供应商："+props.getProperty("java.specification.vender"));
         System.out.println("Java运行时环境规范名称："+props.getProperty("java.specification.name"));
         System.out.println("Java的类格式版本号："+props.getProperty("java.class.version"));
         System.out.println("Java的类路径："+props.getProperty("java.class.path"));
         System.out.println("加载库时搜索的路径列表："+props.getProperty("java.library.path"));
         System.out.println("默认的临时文件路径："+props.getProperty("java.io.tmpdir"));
         System.out.println("一个或多个扩展目录的路径："+props.getProperty("java.ext.dirs"));
         System.out.println("操作系统的名称："+props.getProperty("os.name"));
         System.out.println("操作系统的构架："+props.getProperty("os.arch"));
         System.out.println("操作系统的版本："+props.getProperty("os.version"));
         System.out.println("文件分隔符："+props.getProperty("file.separator"));   //在 unix 系统中是”／”
         System.out.println("路径分隔符："+props.getProperty("path.separator"));   //在 unix 系统中是”:”
         System.out.println("行分隔符："+props.getProperty("line.separator"));   //在 unix 系统中是”/n”
         System.out.println("用户的账户名称："+props.getProperty("user.name"));
         System.out.println("用户的主目录："+props.getProperty("user.home"));
         System.out.println("用户的当前工作目录："+props.getProperty("user.dir"));
         
    	String tableSpase = FileTypeConstance.getSystemProperty("SYS_TABLESPASE");
    	
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> chart = new HashMap<String, Object>();
        if(tableSpase == null){// 获取不到表空间返回空MAP
        	return chart;
        }
        StringBuffer sql = new StringBuffer("select a.tablespace_name,                                   "
			+"       round(a.total_size) \"total_size\",                        "
			+"       round(a.total_size) - round(b.free_size, 3) \"used_size\", "
			+"       round(b.free_size, 3) \"free_size\",                       "
			+"       round(b.free_size / total_size * 100, 2) || '%' free_rate    "
			+"  from (select tablespace_name, sum(bytes) / 1024 / 1024 total_size "
			+"          from dba_data_files WHERE tablespace_name = '"+tableSpase+"'"
			+"         group by tablespace_name ) a,                              "
			+"       (select tablespace_name, sum(bytes) / 1024 / 1024 free_size  "
			+"          from dba_free_space                                       "
			+"         group by tablespace_name) b                                "
			+" where a.tablespace_name = b.tablespace_name(+)");
        QueryHelper indexInit = new QueryHelper(sql.toString(),ds.getConnection());
        result = indexInit.getJSON();
        List dataList = new ArrayList();
        dataList = (List)result.get("data");
    	for (int i = 0; i < dataList.size(); i++) {
    		Map map = new HashMap();
    		map = (Map)dataList.get(i);
    		Object tablespace_name_o = map.get("TABLESPACE_NAME");
    		Object total_size_o = map.get("total_size");
    		String used_size_o = (String)map.get("used_size");
    		String free_size_o = (String)map.get("free_size");
    		this.tablespace_name = tablespace_name_o.toString();
    		this.total_size = total_size_o.toString();
    		this.used_size = used_size_o;
    		this.free_size = free_size_o;
		}
        long endTime = System.currentTimeMillis();
        System.out.println("总共花费时间为：" + (endTime - startTime) + "毫秒...");
        
        chart.put("caption", tablespace_name+"表空间大小(单位：MB 总大小："+total_size+"MB)");
    	chart.put("subcaption", (new Date()).toString());
    	chart.put("bgratio", "24");
    	chart.put("formatnumberscale", "0");
    	chart.put("basefontsize", "13");
        // 返回 chart到前台
    	return chart;
    }
    /***
     * 获取表空间数据
     * @return
     */
    public List getData(){
    	List set = new ArrayList();
    	Map<String, Object> setMap1 = new HashMap<String, Object>();
    	setMap1.put("label", "已使用");
    	setMap1.put("value", used_size);
    	
    	Map<String, Object> setMap2 = new HashMap<String, Object>();
    	setMap2.put("label", "未使用");
    	setMap2.put("value", free_size);

    	set.add(setMap1);
    	set.add(setMap2);
    	return set;
    }
}


