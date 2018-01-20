package com.yuchengtech.bob.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.yuchengtech.bob.core.LookupManager;
import com.yuchengtech.bob.upload.ExportXLSCellFormat;
import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.upload.poi.POIUtils;
/**
 * POI方式导出数据bean
 * @author CHANGZH
 * @date 2013-07-03
 */
public class PoiMetaBean {
	
	private static Logger log = Logger.getLogger(PoiMetaBean.class);
	/***工作薄*/
	private Workbook wb;
	/***表格类型*/
	private String exportType;	
	/**导出文件名*/
	private String filename;
	/***数据文件名*/
	private String dataFileName;
	/***数据文件名*/
	private String titleFileName;	
	/**任务名称，作为文件名生成依据*/
	private String taskName;
	/**每页导出数据数，取自静态变量*/
	private int dataCountPerSheet = Integer.valueOf(FileTypeConstance.getSystemProperty("EXP_XLS_PER_SHEET"));
	/**Oracle字典映射 */
    private Map<String, String> oracleMapping = new HashMap<String, String>();
	/**导出列标签*/
	private Map<String, String> fieldTreeMap;
	/**当前写入的SHEET页号*/
	private int currentSheet = 0;
	/**当前写入数据编号*/
	private int currentData = 0;
	/**导出数据总数*/
	private int total = 0;
	
	ResultSetMetaData rsmd ; 
	/**数据页表头*/
	private CellStyle cellStyle = null;
	/**数据页表头*/
	private CellStyle fmtColumnNames = null;
	/**首页信息*/
	private CellStyle fmtIndexInfo = null;
	/**偶数行样式*/
	private CellStyle fmtRowDataOdd = null;
	/**奇数行样式*/
	private CellStyle fmtRowDataEve = null;
	/**样式*/
	private PoiCellStyle excellStyle = null;
	
	/**
	 * 入口
	 * @param task
	 * @throws Exception
	 */
	public void writeFile(ResultSet rs, DownloadThread downloadThread) throws Exception{
		createFile(exportType);
		FileOutputStream fileOut = null;
		BufferedInputStream in = null;
		String filePath = getFilePath("") + this.getTaskName() + "."+ exportType.toLowerCase();
		try {
			in = new BufferedInputStream(new FileInputStream(new File(filePath)));
			if ("XLSX".equals(exportType.toUpperCase())) {
				wb = new SXSSFWorkbook(1000);
			} else {
				wb = new HSSFWorkbook();
			} 
		} finally {
			in.close();
		}
		try {
			writeData(rs, downloadThread);
			downloadThread.setMessage("正在生成文件");
			log.info("正在生成文件");
			fileOut = new FileOutputStream(new File(filePath));
			wb.write(fileOut);
		} finally {
			fileOut.close();
		}		
	}
	
	/**
	 * 写入数据
	 * @param rs
	 * @param wb
	 * @throws Exception
	 */
	private void writeData(ResultSet rs, DownloadThread downloadThread) throws Exception{
		Sheet cus = null;
		Cell cell = null;
		Font font = wb.createFont();
		fmtRowDataOdd  = wb.createCellStyle();
		fmtRowDataEve  = wb.createCellStyle();
		fmtIndexInfo   = wb.createCellStyle();
		fmtColumnNames = wb.createCellStyle();
		int currentRow = 1;
		int currentColumn = 0;
		LookupManager manager = LookupManager.getInstance();
		if(rs != null){
			rsmd = rs.getMetaData();
		}
		createIndex(wb, font);
		while(rs.next()){   //循环结果集
			if(currentData%dataCountPerSheet == 0){
				cus = wb.createSheet(getDataSheetName());
				createDataHead(cus, font);
				currentRow = 1;
				currentColumn = 0;
				currentSheet++;				
			}
			Row row = cus.createRow(currentRow);
			Iterator<String> fIt = fieldTreeMap.keySet().iterator();
			while(fIt.hasNext()){//循环列
				String columnName = fIt.next();
                String columnSubName2 = columnName.length()>4?columnName.subSequence(columnName.length()-4, columnName.length()).toString():"";
                cell = row.createCell(currentColumn);
                cell.setCellStyle(getCurrRowStyle(currentRow, font));
                try {
					if (!columnSubName2.equals("_ORA")) {
					    if(rs.getObject(columnName) != null) {
					    	POIUtils.setCellValue(cell, rs.getObject(columnName));
					    } else {
					    	POIUtils.setCellValue(cell, "");
					    }
					} else {
						if (rs.getObject(columnName.substring(0, columnName.length() - 4)) != null) {
							String lookupName = oracleMapping.get(columnName.substring(0, columnName.length()-4));
							String value = manager.getOracleValue(lookupName, rs.getString(columnName.substring(0, columnName.length()-4)));
							if (value == null) {
								POIUtils.setCellValue(cell, rs.getString(columnName.substring(0, columnName.length()-4)));
							} else {
								POIUtils.setCellValue(cell, value); 
							}
						} else {
							POIUtils.setCellValue(cell, ""); 
						}
					}
				} catch (Exception e) {
					log.info("【导出失败,导出字段在SQL查询列中不存在】："+columnName);
					throw e;
				}
                currentColumn++;
			}
			currentRow++;
			currentColumn = 0;
			currentData++;
			downloadThread.setExpRecNum(downloadThread.getExpRecNum() + 1);
		}
	}
	
	/**
	 * 数据页表头
	 * @param ws
	 * @throws Exception
	 */
	private void createDataHead(Sheet ws, Font font) throws Exception{
		Iterator<String> fIt = fieldTreeMap.keySet().iterator();
		int currentColumn = 0;
		ws.createRow(0);
		while(fIt.hasNext()){
			ws.getRow(0).createCell(currentColumn);
			String key = fIt.next();
			int length = 0;
			int i = 1;
			while(i < rsmd.getColumnCount()){
				if(rsmd.getColumnName(i).equals(key)){
					length = rsmd.getColumnDisplaySize(i);
					break;
				}
				i++;
			}
			/**
			 * TODO 列宽定义，根据数据字段长度定义EXCEL文件中列的宽度
			 */
			if(length >= 1000) {
				ws.setColumnWidth(currentColumn, 5000);
			} else {
				ws.setColumnWidth(currentColumn, 3000);
			}
			ws.getRow(0).getCell(currentColumn).setCellValue(fieldTreeMap.get(key));
			ws.getRow(0).getCell(currentColumn).setCellStyle(getFmtColumnNames(font));
			currentColumn++;
		}
	}
	
	private String getFilePath(String subDir) {
		StringBuilder filePath = new StringBuilder();
		filePath.append(FileTypeConstance.getExportPath());
		if (!filePath.toString().endsWith(File.separator)) {
        	filePath.append(File.separator);
        }
		filePath.append(subDir);
        if (!filePath.toString().endsWith(File.separator)) {
        	filePath.append(File.separator);
        }
        return filePath.toString();
	}
	
	/**
	 * 创建首页
	 * @param wwb
	 * @throws Exception
	 */
	private void createIndex(Workbook wb, Font font) throws Exception{
		Sheet ws = wb.createSheet(ExportXLSCellFormat.EXPROT_INDEX_SHEET_NAME);
		currentSheet ++;
		Cell cell = ws.createRow((short)2).createCell((short)2);
		cell.setCellStyle(getFmtIndexInfo(font));
		POIUtils.setCellValue(cell, getIndexInfo());
		ws.addMergedRegion(new CellRangeAddress(2, 12, 2, 10));		
	}
	
	/**
	 * 创建文件
	 * @return
	 * @throws IOException
	 */
	private File createFile(String exportType) throws Exception{
		StringBuilder builder = new StringBuilder();
        builder.append(FileTypeConstance.getExportPath());
        if (!builder.toString().endsWith(File.separator)) {
            builder.append(File.separator);
        }
        File file = new File(builder.toString());
        if (! file.exists()) {
            file.mkdirs();
        }        
        file = new File(getFilePath("") + this.getTaskName() + "."+ exportType.toLowerCase());
        if (! file.exists()) {
        	file.createNewFile();
        }
        return file;
	}
	
	/**
	 * 数据页签名称，可根据客户需求修改
	 * @return
	 */
	private String getDataSheetName(){
		int start = (currentSheet-1)*dataCountPerSheet+1;
		int end = currentSheet*dataCountPerSheet;
		if(end>total)
			end = total;
		return "第"+start+"行至第"+end+"行数据";
	}
	
	/**
	 * 获取首页整体信息，可根据客户需求修改
	 * @return
	 */
	private String getIndexInfo(){
		StringBuilder info = new StringBuilder("");
		info.append("本次导出共导出："+total+"条数据");
		return info.toString();
	}
	
	/**
	 * 根据行号返回单元格样式
	 * @param currentRow
	 * @return
	 */
	private CellStyle getCurrRowStyle(int currentRow, Font font){
		return (currentRow&1)==1? getFmtRowDataOdd(font) : getFmtRowDataEve(font);
	}

	public final String getFilename() {
		return filename;
	}

	public final void setFilename(String filename) {
		this.filename = filename;
	}

	public final String getTaskName() {
		return taskName;
	}

	public final void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public CellStyle getFmtRowDataOdd(Font font) {
		return getExcellStyle().getFmtRowDataOdd(fmtRowDataOdd, font);
	}
	
	public CellStyle getFmtRowDataEve(Font font) {
		 return getExcellStyle().getFmtRowDataEve(fmtRowDataEve, font);
	}
	 
	public CellStyle getFmtColumnNames(Font font) {
		return getExcellStyle().getFmtColumnNames(fmtColumnNames, font);
	}
	
	public CellStyle getFmtIndexInfo(Font font) {
		return getExcellStyle().getFmtIndexInfo(fmtIndexInfo, font);
	}	
	
	public PoiCellStyle getExcellStyle() {
		if (excellStyle == null) {
			excellStyle = new PoiCellStyle();
		}
		return excellStyle;
	}
	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}
	public String getDataFileName() {
		return dataFileName;
	}
	public void setTitleFileName(String titleFileName) {
		this.titleFileName = titleFileName;
	}
	public String getTitleFileName() {
		return titleFileName;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	
	public final Map<String, String> getFieldTreeMap() {
		return fieldTreeMap;
	}

	public final void setFieldTreeMap(Map<String, String> fieldTreeMap) {
		this.fieldTreeMap = fieldTreeMap;
	}
	
	public final Map<String, String> getOracleMapping() {
		return oracleMapping;
	}

	public final void setOracleMapping(Map<String, String> oracleMapping) {
		this.oracleMapping = oracleMapping;
	}

	public CellStyle getCellStyle() {
		if (cellStyle == null) {
			cellStyle = wb.createCellStyle();
		} 
		return cellStyle;
	}
}
