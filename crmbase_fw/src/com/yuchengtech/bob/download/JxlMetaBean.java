package com.yuchengtech.bob.download;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.yuchengtech.bob.core.LookupManager;
import com.yuchengtech.bob.upload.ExportXLSCellFormat;
import com.yuchengtech.bob.upload.FileTypeConstance;
/**
 * JXL方式导出数据bean
 * @author CHANGZH
 * @date 2013-07-03
 */
public class JxlMetaBean {
	
	private static Logger log = Logger.getLogger(JxlMetaBean.class);
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
	private WritableCellFormat fmtColumnNames = JxlCellStyle.getFmtColumnNames();
	/**首页信息*/
	private WritableCellFormat fmtIndexInfo = JxlCellStyle.getFmtIndexInfo();
	/**偶数行样式*/
	private WritableCellFormat fmtRowDataOdd = JxlCellStyle.getFmtRowDataOdd();
	/**奇数行样式*/
	private WritableCellFormat fmtRowDataEve = JxlCellStyle.getFmtRowDataEve();
	
	ResultSetMetaData rsmd ; 
	/**
	 * 入口
	 * @param rs
	 * @throws Exception
	 */
	public void writeXLS(ResultSet rs, DownloadThread thread) throws Exception{
		File file = createFile();
		WritableWorkbook wwb = Workbook.createWorkbook(file);
		try{
			createIndex(wwb);
			writeData(rs,wwb,thread);
			thread.setMessage("正在生成文件");
			wwb.write();
		}finally{
			wwb.close();
			wwb = null;
		}
	}
	/**
	 * 写入数据
	 * @param rs
	 * @param wwb
	 * @throws Exception
	 */
	private void writeData(ResultSet rs,WritableWorkbook wwb,  DownloadThread thread) throws Exception{
		WritableSheet cus = null;
		int currentRow = 1;
		int currentColumn = 0;
		LookupManager manager = LookupManager.getInstance();
		if(rs!=null){
			rsmd = rs.getMetaData();
		}
		while(rs.next()){   //循环结果集
			if(currentData%dataCountPerSheet==0){
				cus = wwb.createSheet(getDataSheetName(), currentSheet);
				createDataHead(wwb.getSheet(currentSheet));
				currentRow = 1;
				currentColumn = 0;
				currentSheet++;
			}
			Iterator<String> fIt = fieldTreeMap.keySet().iterator();
			while(fIt.hasNext()){//循环列
				String columnName = fIt.next();
                String columnSubName2 = columnName.length()>4?columnName.subSequence(columnName.length()-4, columnName.length()).toString():"";
                try {
					if (!columnSubName2.equals("_ORA")) {
					    if(rs.getObject(columnName) != null) {
					    	cus.addCell(new Label(currentColumn,currentRow,rs.getObject(columnName).toString(),getCurrRowStyle(currentRow)));
					    } else {
					    	cus.addCell(new Label(currentColumn,currentRow," ",getCurrRowStyle(currentRow)));
					    }
					} else {
						if (rs.getObject(columnName.substring(0, columnName.length()-4)) != null) {
							String lookupName = oracleMapping.get(columnName.substring(0, columnName.length()-4));
							String value = manager.getOracleValue(lookupName, rs.getString(columnName.substring(0, columnName.length()-4)));
							if (value == null) {
								cus.addCell(new Label(currentColumn,currentRow,rs.getString(columnName.substring(0, columnName.length()-4)),getCurrRowStyle(currentRow)));
							} else {
								cus.addCell(new Label(currentColumn,currentRow,value,getCurrRowStyle(currentRow)));
							}
						} else {
							cus.addCell(new Label(currentColumn,currentRow," ",getCurrRowStyle(currentRow)));
						}
					}
				} catch (Exception e) {
					log.info("【导出失败,导出字段在SQL查询列中不存在】："+columnName);
					throw e;
				}
                currentColumn++;
			}
			currentRow++;
			currentColumn=0;
			currentData++;
			thread.setExpRecNum(thread.getExpRecNum() + 1);
		}
	}
	
	/**
	 * 数据页表头
	 * @param ws
	 * @throws Exception
	 */
	private void createDataHead(WritableSheet ws) throws Exception{
		Iterator<String> fIt = fieldTreeMap.keySet().iterator();
		int currentColumn = 0;
		while(fIt.hasNext()){
			String key = fIt.next();
			int length = 0;
			int i = 1;
			while(i<rsmd.getColumnCount()){
				if(rsmd.getColumnName(i).equals(key)){
					length = rsmd.getColumnDisplaySize(i);
					break;
				}
				i++;
			}
			/**
			 * TODO 列宽定义，根据数据字段长度定义EXCEL文件中列的宽度
			 */
			if(length>=1000){
				ws.setColumnView(currentColumn, 50);
			}else if(length>=100){
				ws.setColumnView(currentColumn, 30);
			}
			ws.addCell(new Label(currentColumn,0,fieldTreeMap.get(key),fmtColumnNames));
			currentColumn ++;
		}
	}
	
	/**
	 * 创建首页
	 * @param wwb
	 * @throws Exception
	 */
	private void createIndex(WritableWorkbook wwb) throws Exception{
		WritableSheet ws = wwb.createSheet(ExportXLSCellFormat.EXPROT_INDEX_SHEET_NAME, currentSheet);
		currentSheet ++;
		ws.addCell(new Label(2, 2, "", new WritableCellFormat()));
		ws.mergeCells(2, 2, 9, 12);
		ws.addCell(new Label(2, 2, getIndexInfo()));
		ws.getWritableCell(2, 2).setCellFormat(fmtIndexInfo);
	}
	
	/**
	 * 创建文件
	 * @return
	 * @throws IOException
	 */
	private File createFile() throws Exception{
		StringBuilder builder = new StringBuilder();
        builder.append(FileTypeConstance.getExportPath());
        if (!builder.toString().endsWith(File.separator)) {
            builder.append(File.separator);
        }
        File file = new File(builder.toString());
        if (! file.exists()) {
            file.mkdirs();
        }
        builder.append(taskName + ".xls");
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
	private WritableCellFormat getCurrRowStyle(int currentRow){
		return (currentRow&1)==1?fmtRowDataOdd:fmtRowDataEve;
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
	
}
