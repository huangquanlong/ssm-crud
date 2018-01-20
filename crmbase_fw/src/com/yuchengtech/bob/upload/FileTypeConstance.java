package com.yuchengtech.bob.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @describe This map defines the file types for loading attachment files.
 * @author WillJoe
 */
public class FileTypeConstance {

    /**
     * Data type array.
     */
    
    public static List<String> dataTypes = new ArrayList<String>();
    static {
        dataTypes.add("NUMBER");
        dataTypes.add("VARCHAR2");
        dataTypes.add("DATE");
    }
    /**
     * file types mapping.
     */
    public static Map<String, String> fileTypes = new HashMap<String, String>();
    
    static {
        fileTypes.put(".ppt", "application/mspowerpoint");
        fileTypes.put(".ai", "application/postscript");
        fileTypes.put(".aif", "audio/x-aiff");
        fileTypes.put(".aifc", "audio/x-aiff");
        fileTypes.put(".aiff", "audio/x-aiff");
        fileTypes.put(".asc", "text/plain");
        fileTypes.put(".au", "audio/basic");
        fileTypes.put(".avi", "video/x-msvideo");
        fileTypes.put(".bcpio", "application/x-bcpio");
        fileTypes.put(".bin", "application/octet-stream");
        fileTypes.put(".c", "text/plain");
        fileTypes.put(".cc", "text/plain");
        fileTypes.put(".ccad", "application/clariscad");
        fileTypes.put(".cdf","application/x-netcdf");
        fileTypes.put(".class","application/octet-stream");
        fileTypes.put(".cpio","application/x-cpio");
        fileTypes.put(".cpt","application/mac-compactpro");
        fileTypes.put(".csh","application/x-csh");
        fileTypes.put(".css","text/css");
        fileTypes.put(".dcr","application/x-director");
        fileTypes.put(".dir","application/x-director");
        fileTypes.put(".dms","application/octet-stream");
        fileTypes.put(".doc","application/msword");
        fileTypes.put(".drw","application/drafting");
        fileTypes.put(".dvi","application/x-dvi");
        fileTypes.put(".dwg","application/acad");
        fileTypes.put(".dxf","application/dxf");
        fileTypes.put(".dxr","application/x-director");
        fileTypes.put(".eps","application/postscript");
        fileTypes.put(".etx","text/x-setext");
        fileTypes.put(".exe","application/octet-stream");
        fileTypes.put(".ez","application/andrew-inset");
        fileTypes.put(".f","text/plain");
        fileTypes.put(".f90","text/plain");
        fileTypes.put(".fli","video/x-fli");
        fileTypes.put(".gif","image/gif");
        fileTypes.put(".gtar","application/x-gtar");
        fileTypes.put(".gz","application/x-gzip");
        fileTypes.put(".h","text/plain");
        fileTypes.put(".hdf","application/x-hdf");
        fileTypes.put(".hh","text/plain");
        fileTypes.put(".hqx","application/mac-binhex40");
        fileTypes.put(".htm","text/html");
        fileTypes.put(".html","text/html");
        fileTypes.put(".ice","x-conference/x-cooltalk");
        fileTypes.put(".ief","image/ief");
        fileTypes.put(".iges","model/iges");
        fileTypes.put(".igs","model/iges");
        fileTypes.put(".ips","application/x-ipscript");
        fileTypes.put(".ipx","application/x-ipix");
        fileTypes.put(".jpe","image/jpeg");
        fileTypes.put(".jpeg","image/jpeg");
        fileTypes.put(".jpg","image/jpeg");
        fileTypes.put(".js","application/x-javascript");
        fileTypes.put(".kar","audio/midi");
        fileTypes.put(".latex","application/x-latex");
        fileTypes.put(".lha","application/octet-stream");
        fileTypes.put(".lsp","application/x-lisp");
        fileTypes.put(".lzh","application/octet-stream");
        fileTypes.put(".m","text/plain");
        fileTypes.put(".man","application/x-troff-man");
        fileTypes.put(".me","application/x-troff-me");
        fileTypes.put(".mesh","model/mesh");
        fileTypes.put(".mid","audio/midi");
        fileTypes.put(".midi","audio/midi");
        fileTypes.put(".mif","application/vnd.mif");
        fileTypes.put(".mime","www/mime");
        fileTypes.put(".mov","video/quicktime");
        fileTypes.put(".movie","video/x-sgi-movie");
        fileTypes.put(".mp2","audio/mpeg");
        fileTypes.put(".mp3","audio/mpeg");
        fileTypes.put(".mpe","video/mpeg");
        fileTypes.put(".mpeg","video/mpeg");
        fileTypes.put(".mpg","video/mpeg");
        fileTypes.put(".mpga","audio/mpeg");
        fileTypes.put(".ms","application/x-troff-ms");
        fileTypes.put(".msh","model/mesh");
        fileTypes.put(".nc","application/x-netcdf");
        fileTypes.put(".oda","application/oda");
        fileTypes.put(".pbm","image/x-portable-bitmap");
        fileTypes.put(".pdb","chemical/x-pdb");
        fileTypes.put(".pdf","application/pdf");
        fileTypes.put(".pgm","image/x-portable-graymap");
        fileTypes.put(".pgn","application/x-chess-pgn");
        fileTypes.put(".png","image/png");
        fileTypes.put(".pnm","image/x-portable-anymap");
        fileTypes.put(".pot","application/mspowerpoint");
        fileTypes.put(".ppm","image/x-portable-pixmap");
        fileTypes.put(".pps","application/mspowerpoint");
        fileTypes.put(".ppt","application/mspowerpoint");
        fileTypes.put(".ppz","application/mspowerpoint");
        fileTypes.put(".pre","application/x-freelance");
        fileTypes.put(".prt","application/pro_eng");
        fileTypes.put(".ps","application/postscript");
        fileTypes.put(".qt","video/quicktime");
        fileTypes.put(".ra","audio/x-realaudio");
        fileTypes.put(".ram","audio/x-pn-realaudio");
        fileTypes.put(".ras","image/cmu-raster");
        fileTypes.put(".rgb","image/x-rgb");
        fileTypes.put(".rm","audio/x-pn-realaudio");
        fileTypes.put(".roff","application/x-troff");
        fileTypes.put(".rpm","audio/x-pn-realaudio-plugin");
        fileTypes.put(".rtf","text/rtf");
        fileTypes.put(".rtx","text/richtext");
        fileTypes.put(".scm","application/x-lotusscreencam");
        fileTypes.put(".set","application/set");
        fileTypes.put(".sgm","text/sgml");
        fileTypes.put(".sgml","text/sgml");
        fileTypes.put(".sh","application/x-sh");
        fileTypes.put(".shar","application/x-shar");
        fileTypes.put(".silo","model/mesh");
        fileTypes.put(".sit","application/x-stuffit");
        fileTypes.put(".skd","application/x-koan");
        fileTypes.put(".skm","application/x-koan");
        fileTypes.put(".skp","application/x-koan");
        fileTypes.put(".skt","application/x-koan");
        fileTypes.put(".smi","application/smil");
        fileTypes.put(".smil","application/smil");
        fileTypes.put(".snd","audio/basic");
        fileTypes.put(".sol","application/solids");
        fileTypes.put(".spl","application/x-futuresplash");
        fileTypes.put(".src","application/x-wais-source");
        fileTypes.put(".step","application/STEP");
        fileTypes.put(".stl","application/SLA");
        fileTypes.put(".stp","application/STEP");
        fileTypes.put(".sv4cpio","application/x-sv4cpio");
        fileTypes.put(".sv4crc","application/x-sv4crc");
        fileTypes.put(".swf","application/x-shockwave-flash");
        fileTypes.put(".t","application/x-troff");
        fileTypes.put(".tar","application/x-tar");
        fileTypes.put(".tcl","application/x-tcl");
        fileTypes.put(".tex","application/x-tex");
        fileTypes.put(".texi","application/x-texinfo");
        fileTypes.put(".texinfo","application/x-texinfo");
        fileTypes.put(".tif","image/tiff");
        fileTypes.put(".tiff","image/tiff");
        fileTypes.put(".tr","application/x-troff");
        fileTypes.put(".tsi","audio/TSP-audio");
        fileTypes.put(".tsp","application/dsptype");
        fileTypes.put(".tsv","text/tab-separated-values");
        fileTypes.put(".txt","text/plain");
        fileTypes.put(".unv","application/i-deas");
        fileTypes.put(".ustar","application/x-ustar");
        fileTypes.put(".vcd","application/x-cdlink");
        fileTypes.put(".vda","application/vda");
        fileTypes.put(".viv","video/vnd.vivo");
        fileTypes.put(".vivo","video/vnd.vivo");
        fileTypes.put(".vrml","model/vrml");
        fileTypes.put(".wav","audio/x-wav");
        fileTypes.put(".wrl","model/vrml");
        fileTypes.put(".xbm","image/x-xbitmap");
        fileTypes.put(".xlc","application/vnd.ms-excel");
        fileTypes.put(".xll","application/vnd.ms-excel");
        fileTypes.put(".xlm","application/vnd.ms-excel");
        fileTypes.put(".xls","application/vnd.ms-excel");
        fileTypes.put(".xlw","application/vnd.ms-excel");
        fileTypes.put(".xml","text/xml");
        fileTypes.put(".xpm","image/x-xpixmap");
        fileTypes.put(".xwd","image/x-xwindowdump");
        fileTypes.put(".xyz","chemical/x-pdb");
        fileTypes.put(".zip","application/zip");    
    }
    
    public static List<String> allowFileType = new ArrayList<String>();
    
    static{
    	allowFileType.add("ppt");
    	allowFileType.add("pptx");
    	allowFileType.add("doc");
    	allowFileType.add("docx");
    	allowFileType.add("xls");
    	allowFileType.add("xlsx");
    	allowFileType.add("txt");
    	allowFileType.add("jpg");
    	allowFileType.add("jpeg");
    	allowFileType.add("gif");
    	allowFileType.add("pdf");
    }
    
    /**
     * 校验文件后缀名是否合法文件类型
     * @param ext：文件后缀
     * @return
     */
    public static boolean fileTypeAllowed(String suff){
    	return allowFileType.contains(suff);
    }
    
    /**
     * Get the file name without the extension name.
     * @param filename
     * @return
     */
    public static String getExtFileName(String filename){
        if ((filename != null) && (filename.length() > 0)) { 
            int i = filename.lastIndexOf('.'); 
            if ((i >-1) && (i < (filename.length()))) { 
                return filename.substring(i, filename.length()); 
            } 
        } 
        return "";
    }
    /**
     * Get the extension name of the file.U will get a empty string if the file has no extension name.
     * @param filename
     * @return
     */
    public static String getFileNameWithoutExt(String filename){
        if ((filename != null) && (filename.length() > 0)) { 
            int i = filename.lastIndexOf('.'); 
            if ((i >-1) && (i < (filename.length()))) { 
                return filename.substring(0, i); 
            } 
        } 
        return "";
    }
    
    /**
     * Get the http head define string for the file.
     */
    public static String getHttpheaderofFile(String filename){
        return null==fileTypes.get(getExtFileName(filename).toLowerCase())?"text/plain":fileTypes.get(getExtFileName(filename).toLowerCase());
    }
    
    /**
     * Get the attachment file name.
     */
    public static String getSeqFileName(){
        return "attachment"+System.currentTimeMillis();
    }

    
    /**
     * @describe Adapter for the ItemFile object's method getName();
     *           If the files are uploaded from the Windows OS ,Unix OS or Linux OS;
     *           And if the files are uploaded from the different browsers, e.g. IE 6.x or higher version ,Opera , NN or FF....
     *           But I'm sorry to say that , I've never use it.
     * @param itemFileName
     * @return
     */
    public static String getFileName(String itemFileName){
        if(itemFileName.indexOf("\\")>0){
            return itemFileName.substring(itemFileName.indexOf("\\")+1,itemFileName.length());
        }
        if(itemFileName.indexOf("/")>0){
            return itemFileName.substring(itemFileName.indexOf("/")+1,itemFileName.length());
        }
        return itemFileName;
    }
    
    /**
     * 从配置文件中获取系统上传路径
     */
    public static String getUploadPath(){
        
        Properties props = new Properties();
        {
            try {
            	props.load(Thread.currentThread().getContextClassLoader().getResource("systemProperties.properties").openStream());
//                props.load(FileTypeConstance.class.getResourceAsStream("../../../../systemProperties.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props.getProperty("sysUpload");
    }
    
    
    /**
     * 从配置文件中获取server地址
     */
    public static String getServerIP(){
        
        Properties props = new Properties();
        {
            try {
            	props.load(Thread.currentThread().getContextClassLoader().getResource("systemProperties.properties").openStream());
//                props.load(FileTypeConstance.class.getResourceAsStream("../../../../systemProperties.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props.getProperty("serverIP");
    }
    
    /**
     * 从配置文件中获取导出文件存储路径
     */
    public static String getExportPath(){
        
        Properties props = new Properties();
        {
            try {
            	props.load(Thread.currentThread().getContextClassLoader().getResource("systemProperties.properties").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props.getProperty("sysExport");
    }
    
    /**
     * 从配置文件中获取导出文件存储路径
     */
    public static String getImportTmpPath(){
        
        Properties props = new Properties();
        {
            try {
            	props.load(Thread.currentThread().getContextClassLoader().getResource("systemProperties.properties").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props.getProperty("impTempPath");
    }
    
    /**
     * 从配置文件中获取导入模版文件存储路径
     */
    public static String getImportTempaltePath(){
        
        Properties props = new Properties();
        {
            try {
            	props.load(Thread.currentThread().getContextClassLoader().getResource("systemProperties.properties").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props.getProperty("impTemplatePath");
    }
    /**
     * 从配置文件中获取上传文件最大值
     */
    public static Long getMaxUploadFileSize(){
        
        Properties props = new Properties();
        {
            try {
            	props.load(Thread.currentThread().getContextClassLoader().getResource("systemProperties.properties").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Long.parseLong(props.getProperty("maxUploadFileSize"));
    }    
    /**
     * 从systemProperties.properties配置文件中获取相应属性
     */
    public static String getSystemProperty(String property){
        
        Properties props = new Properties();
        {
            try {
            	props.load(Thread.currentThread().getContextClassLoader().getResource("systemProperties.properties").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props.getProperty(property);
    }
    
    
    /**
     * 从bip.properties配置文件中获取相应属性
     */
    public static String getBipProperty(String property){
        
        Properties props = new Properties();
        {
            try {
            	props.load(Thread.currentThread().getContextClassLoader().getResource("bip.properties").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props.getProperty(property);
    }
    public static void main(String[] args) {
		//system.out.printlnln(getImportTmpPath());
	}
}
