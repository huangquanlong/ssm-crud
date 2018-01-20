package com.yuchengtech.bob.upload.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yuchengtech.bob.core.LookupManager;
import com.yuchengtech.bob.core.TaskExportCSV;
import com.yuchengtech.bob.upload.ExportXLSCellFormat;
import com.yuchengtech.bob.upload.FileTypeConstance;

public class DataMetaBean {
	
	private static Logger log = Logger.getLogger(DataMetaBean.class);
	/***工作薄*/
	private Workbook wb;
	/***表格类型*/
	private String exportType;	
    /**Oracle字典映射 */
    private Map<String, String> oracleMapping = new HashMap<String, String>();
	/**导出SQL*/
	private String sql;
	/**导出文件名*/
	private String filename;
	/**任务名称，作为文件名生成依据*/
	private String taskName;
	/**导出数据总数*/
	private int total = 0;
	/**导出列标签*/
	private Map<String, String> fieldTreeMap;
	/**当前写入的SHEET页号*/
	private int currentSheet = 0;
	/**当前写入数据编号*/
	private int currentData = 0;

	/**每页导出数据数，取自静态变量*/
	private int dataCountPerSheet = Integer.valueOf(FileTypeConstance.getSystemProperty("EXP_XLS_PER_SHEET"));
	/**数据页表头*/
	private CellStyle fmtColumnNames = null;
	/**首页信息*/
	private CellStyle fmtIndexInfo = null;
	/**偶数行样式*/
	private CellStyle fmtRowDataOdd = null;
	/**奇数行样式*/
	private CellStyle fmtRowDataEve = null;
	
	/**样式*/
	private ExportCellStyle excellStyle = null;
	
	ResultSetMetaData rsmd ; 
	/**
	 * 入口
	 * @param rs
	 * @throws Exception
	 */
	public void writeXLS(ResultSet rs, TaskExportCSV task, String exportType) throws Exception{
		File file = createFile(exportType);
		FileOutputStream fileOut = new FileOutputStream(file);
		if ("XLSX".equals(exportType.toUpperCase())) {
			this.exportType = exportType;
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		} 
		try{
			createIndex(wb);
			writeData(rs,wb,task);
			task.setMessage("正在生成文件");
			wb.write(fileOut);
		}finally{
			fileOut.close();
		}
		
	}
	/**
	 * 写入数据
	 * @param rs
	 * @param wb
	 * @throws Exception
	 */
	private void writeData(ResultSet rs,Workbook wb, TaskExportCSV task) throws Exception{
		Sheet cus = null;
		int currentRow = 1;
		int currentColumn = 0;
		LookupManager manager = LookupManager.getInstance();
		if(rs != null){
			rsmd = rs.getMetaData();
		}
		fmtRowDataOdd = wb.createCellStyle();
		fmtRowDataEve = wb.createCellStyle();
		Font font = wb.createFont();
		while(rs.next()){   //循环结果集
			if(currentData%dataCountPerSheet == 0){
				cus = wb.createSheet(getDataSheetName());
				createDataHead(cus);
				currentRow = 1;
				currentColumn = 0;
				currentSheet++;				
			}
			Row row = cus.createRow(currentRow);
			Iterator<String> fIt = new TreeMap<String, String>(fieldTreeMap).keySet().iterator();
			while(fIt.hasNext()){//循环列
				String columnName = fIt.next();
				//log.info("columnName["+currentColumn+"] = [" + columnName + "]");
                String columnSubName2 = columnName.length()>4?columnName.subSequence(columnName.length()-4, columnName.length()).toString():"";
                Cell cell = row.createCell(currentColumn);
                cell.setCellStyle(getCurrRowStyle(currentRow, font));
                if (!columnSubName2.equals("_ORA")) {
                    if(rs.getObject(columnName) != null) {
                    	POIUtils.setCellValue(cell, rs.getObject(columnName));
                    	//cus.addCell(new Label(currentColumn,currentRow,rs.getObject(columnName).toString(),getCurrRowStyle(currentRow)));
                    } else {
                    	POIUtils.setCellValue(cell, "");
                    	//cus.addCell(new Label(currentColumn,currentRow," ",getCurrRowStyle(currentRow)));
                    }
                } else {
                	if (rs.getObject(columnName.substring(0, columnName.length() - 4)) != null) {
                		String lookupName = oracleMapping.get(columnName.substring(0, columnName.length()-4));
                		String value = manager.getOracleValue(lookupName, rs.getString(columnName.substring(0, columnName.length()-4)));
                		if (value == null) {
                			POIUtils.setCellValue(cell, rs.getString(columnName.substring(0, columnName.length()-4)));
                			//cus.addCell(new Label(currentColumn,currentRow,rs.getString(columnName.substring(0, columnName.length()-4)),getCurrRowStyle(currentRow)));
                		} else {
                			POIUtils.setCellValue(cell, value); 
                			//cus.addCell(new Label(currentColumn,currentRow,value,getCurrRowStyle(currentRow)));
                		}
                	} else {
                		POIUtils.setCellValue(cell, ""); 
                		//cus.addCell(new Label(currentColumn,currentRow," ",getCurrRowStyle(currentRow)));
                	}
                }
                currentColumn++;
			}
			currentRow++;
			currentColumn = 0;
			currentData++;
			task.setExpRecNum(task.getExpRecNum() + 1);
		}
	}
	
	/**
	 * 数据页表头
	 * @param ws
	 * @throws Exception
	 */
	private void createDataHead(Sheet ws) throws Exception{
		Iterator<String> fIt = new TreeMap<String, String>(fieldTreeMap).keySet().iterator();
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
			ws.getRow(0).getCell(currentColumn).setCellStyle(this.getFmtColumnNames());
			currentColumn++;
		}
	}
	
	/**
	 * 创建首页
	 * @param wwb
	 * @throws Exception
	 */
	private void createIndex(Workbook wwb) throws Exception{
		Sheet ws = wwb.createSheet(ExportXLSCellFormat.EXPROT_INDEX_SHEET_NAME);
		currentSheet ++;
		Cell cell = ws.createRow((short)2).createCell((short)2);
		cell.setCellStyle(getFmtIndexInfo());
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
        builder.append(taskName + "." + exportType.toLowerCase());
        filename = builder.toString();
        file = new File(filename);
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
		return (currentRow&1)==1? getFmtRowDataOdd(fmtRowDataOdd, font) : getFmtRowDataEve(fmtRowDataEve, font);
	}

	public final String getSql() {
		return sql;
	}

	public final void setSql(String sql) {
		this.sql = sql;
	}

	public final String getFilename() {
		return filename;
	}

	public final void setFilename(String filename) {
		this.filename = filename;
	}

	public final int getTotal() {
		return total;
	}

	public final void setTotal(int total) {
		this.total = total;
	}

	public final Map<String, String> getFieldTreeMap() {
		return fieldTreeMap;
	}

	public final void setFieldTreeMap(Map<String, String> fieldTreeMap) {
		this.fieldTreeMap = fieldTreeMap;
	}

	public final String getTaskName() {
		return taskName;
	}

	public final void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public final Map<String, String> getOracleMapping() {
		return oracleMapping;
	}

	public final void setOracleMapping(Map<String, String> oracleMapping) {
		this.oracleMapping = oracleMapping;
	}
	
	public int getCurrentData() {
		return currentData;
	}
	
	public void setCurrentData(int currentData) {
		this.currentData = currentData;
	}
	
	public CellStyle getFmtColumnNames() {
		return ExportCellStyle.getFmtColumnNames(wb, exportType);
	}
	public void setFmtColumnNames(CellStyle fmtColumnNames) {
		this.fmtColumnNames = fmtColumnNames;
	}
	public CellStyle getFmtIndexInfo() {
		return ExportCellStyle.getFmtIndexInfo(wb, exportType);
	}
	
	public void setFmtIndexInfo(CellStyle fmtIndexInfo) {
		this.fmtIndexInfo = fmtIndexInfo;
	}
	
	public CellStyle getFmtRowDataOdd(CellStyle currRowStyle,Font font) {
		return getExcellStyle().getFmtRowDataOdd(currRowStyle, font);
	}
	
	public CellStyle getFmtRowDataEve(CellStyle currRowStyle,Font font) {
		 return getExcellStyle().getFmtRowDataEve( currRowStyle, font);
	}
	 
	public ExportCellStyle getExcellStyle() {
		if (excellStyle == null) {
			excellStyle = new ExportCellStyle();
		}
		return excellStyle;
	}	
}
