package com.yuchengtech.bob.core;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.yuchengtech.bob.upload.ExportMetaBean;
import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.upload.poi.DataMetaBean;

public class TaskExportCSV extends BackgroundTask {
    
    private final String sperator = ",";
    
    private final String newLine = "\n";
    	
	private String sql;
	
	private int expRecNum = 0;
	
	private String filename;
	
	private String exportType = FileTypeConstance.getSystemProperty("EXP_FILE_TYPE")==null?"CSV":FileTypeConstance.getSystemProperty("EXP_FILE_TYPE");
	
	/** 表头标题映射 */
    private Map<String, String> fieldLabel = new HashMap<String, String>();
    /** Oracle字典映射 */
    private Map<String, String> oracleMapping = new HashMap<String, String>();

	public void run() {
		running = true;
		sql = getMessage();
		try {
			if(exportType.equals("CSV")){
				StringBuilder builder = new StringBuilder();
				builder.append(FileTypeConstance.getExportPath());
				if (!builder.toString().endsWith(File.separator)) {
					builder.append(File.separator);
				}
				builder.append(File.separator);
				File file = new File(builder.toString());
				if (! file.exists()) {
					file.mkdir();
				}
				builder.append(getTaskID() + ".csv");
				filename = builder.toString();
				file = new File(filename);
				if (! file.exists()) {
					file.createNewFile();
				}
				export();
			}else if(exportType.toUpperCase().startsWith("XLS") || exportType.toUpperCase().startsWith("XLSX")){
				//exportXLS();
				exportXLSByPoi(exportType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("导出数据时发生异常:", e);
		} finally {
			ExportImportManager.getInstance().removeExportTask(this);
			running = false;
		}
	}
	/***
	 *  导出Excel by poi
	 *  @author CHANGZH
	 *  @date 20130130
	 * 
	 **/
	public void exportXLSByPoi(String exportType) throws Exception {
		Connection conn = null;
		try {
			conn = getDatasource().getConnection();
			Statement stmt = conn.createStatement();
			setMessage("准备导出数据...");
            StringBuilder builder = new StringBuilder(sql);
            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
            builder.append(") SUB_QUERY");
            ResultSet rs = stmt.executeQuery(builder.toString());
            if(rs.next()) {
                total = rs.getInt("TOTAL");
            }
            setMessage("共计:" + total);
			rs = stmt.executeQuery(sql);
			
			DataMetaBean dmb = new DataMetaBean();
			dmb.setTotal(total);
			dmb.setTaskName(getTaskID()+"");
			dmb.setFieldTreeMap(fieldLabel);
			dmb.setSql(sql);
			dmb.setOracleMapping(oracleMapping);
			dmb.writeXLS(rs ,this, exportType);
		} finally {
			conn.close();
		}
	}
	
	public void exportXLS() throws Exception {
		Connection conn = null;
		try {
			conn = getDatasource().getConnection();
			Statement stmt = conn.createStatement();
			setMessage("准备导出数据...");
            StringBuilder builder = new StringBuilder(sql);
            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
            builder.append(") SUB_QUERY");
            ResultSet rs = stmt.executeQuery(builder.toString());
            if(rs.next()) {
                total = rs.getInt("TOTAL");
            }
            setMessage("共计:" + total);
			rs = stmt.executeQuery(sql);
			
			ExportMetaBean emb = new ExportMetaBean();
			emb.setTotal(total);
			emb.setTaskName(getTaskID()+"");
			emb.setFieldTreeMap(fieldLabel);
			emb.setSql(sql);
			emb.setOracleMapping(oracleMapping);
			emb.writeXLS(rs ,this);
		} finally {
			conn.close();
		}
	}

	public void export() throws Exception {
		Connection conn = null;
		FileWriter writer = null;
		try {
			conn = getDatasource().getConnection();
			Statement stmt = conn.createStatement();
			writer = new FileWriter(filename);
			setMessage("准备导出数据...");
            StringBuilder builder = new StringBuilder(sql);
            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
            builder.append(") SUB_QUERY");
            ResultSet rs = stmt.executeQuery(builder.toString());
            if(rs.next()) {
                total = rs.getInt("TOTAL");
            }
            setMessage("共计:" + total);
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();
			setMessage("正在导出表头...");
			for (int i = 0; i < columnCount; i++) {
                String columnName = meta.getColumnName(i + 1);
                if (fieldLabel.containsKey(columnName)) {
                    writer.append(fieldLabel.get(columnName));
                    writer.append(sperator);
                } else {
                    String fakeColumnName = columnName + "_GP";
                    if (fieldLabel.containsKey(fakeColumnName)) {
                        writer.append(fieldLabel.get(fakeColumnName));
                        writer.append(sperator);
                    }
                    fakeColumnName = columnName + "_ORA";
                    if (fieldLabel.containsKey(fakeColumnName)) {
                        writer.append(fieldLabel.get(fakeColumnName));
                        writer.append(sperator);
                    }
                }
            }
			writer.append(newLine);
            setMessage("正在导出数据...");
            LookupManager manager = LookupManager.getInstance();
            setExpRecNum(0);
			while (rs.next() && running) {
	            for (int i = 0; i < columnCount; i++) {
	                String columnName = meta.getColumnName(i + 1);
	                if (fieldLabel.containsKey(columnName)) {
	                    if(rs.getObject(columnName) != null) {
	                        writer.append(rs.getObject(columnName).toString());
	                    } 
	                    writer.append(sperator);
	                } else {
                        if(rs.getObject(columnName) != null) {
                            if (fieldLabel.containsKey(columnName + "_ORA")) {
                                String lookupName = oracleMapping.get(columnName);
                                String value = manager.getOracleValue(lookupName, rs.getString(columnName));
                                if (value == null) {
                                    writer.append(rs.getString(columnName));
                                } else {
                                    writer.append(value);                                    
                                }
                                writer.append(sperator);
                            }
                        }else if(fieldLabel.containsKey(columnName+"_GP"))
                            writer.append(sperator);
                        else if(fieldLabel.containsKey(columnName+"_ORA"))
                            writer.append(sperator);
	                }
	            }
	            writer.append(newLine);
                current++;
                setExpRecNum(++expRecNum);
			}
			setMessage("数据导出完成。");
		} finally {
			writer.close();
			conn.close();
		}
	}

    public void setFieldLabel(Map<String, String> fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public void setOracleMapping(Map<String, String> oracleMapping) {
        this.oracleMapping = oracleMapping;
    }

	public void setExpRecNum(int expRecNum) {
		this.expRecNum = expRecNum;
	}

	public int getExpRecNum() {
		return expRecNum;
	}

}
