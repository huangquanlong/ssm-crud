package com.yuchengtech.bob.download;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yuchengtech.bcrm.system.model.OcrmFSysDownloadRecord;
import com.yuchengtech.bcrm.system.service.DownloadRecordManagerService;
import com.yuchengtech.bob.core.LookupManager;
import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;

/***
 * 
 * @author CHANGZH
 * @date 2013-07-09
 * @description 下载进程类
 * 
 */
public class DownloadThread extends BackgroundThread {

	private Object lock = new Object();
	private final String sperator = ",";
	private final String newLine = "\n";
	private int expRecNum = 0;
	public String status = "1"; // 0:完成； 1 ：进行中；2：等待；3:killed
	public boolean isBackgroundExport = true; //
	public static final String status_completed = "0";
	public static final String status_downloading = "1";
	public static final String status_wating = "2";
	public static final String status_killed = "3";

	private String exportType = FileTypeConstance.getSystemProperty("EXP_FILE_TYPE") == null ? "CSV"
			: FileTypeConstance.getSystemProperty("EXP_FILE_TYPE");
	/** 表头标题映射 */
	private Map<String, String> fieldLabel = new HashMap<String, String>();
	/***下载信息*/
	private Map<String, String> downloadInfo = new HashMap<String, String>();

	/** Oracle字典映射 */
	private Map<String, String> oracleMapping = new HashMap<String, String>();
	/*** 下载文件名 */
	private String filename;
	/*** 下载SQL */
	public String SQL;
	/***下载记录信息管理service*/
	private DownloadRecordManagerService service;

	class DownloadClass extends Thread {

		public void run() {
			try {
				OcrmFSysDownloadRecord odr = (OcrmFSysDownloadRecord) preReportDownload();
				doExportFile();
				odr.setThreadStatus(DownloadThread.status_completed);
				odr.setFinishTime(new Date());
				service.save(odr);
			} catch (Exception e) {
				e.printStackTrace();
				status = DownloadThread.status_completed;
				throw new BizException(1, 0, "2002", "下载失败！");
			}
			status = DownloadThread.status_completed;
			synchronized (lock) {
				lock.notify();
			}
		}
	}

	public void run() {
		DownloadClass dc = new DownloadClass();
		synchronized (lock) {
			try {
				dc.start();
				while (!status_completed.equals(getStatus())) {
					try {
						lock.wait();
					} catch (InterruptedException ie) {
						this.setStatus(status_completed);
						dc.interrupt();
						try {
	
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}  catch (Exception e) {
				e.printStackTrace();
			} finally {
				DownloadThreadManager.getInstance().removeDownloadThread(this);
			}
		}
	}

	/**
	 * 新增下载信息记录
	 * @return 下载信息记录
	 */
	public Object preReportDownload() {
		OcrmFSysDownloadRecord odr = new OcrmFSysDownloadRecord();
		odr.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
		odr.setFileName(getThreadID() + "." +exportType.toLowerCase());
		if (null != downloadInfo.get("menuId") && downloadInfo.get("menuId") != "") {
			odr.setMenuId(downloadInfo.get("menuId"));
		}
		if (null != downloadInfo.get("queryCon") && downloadInfo.get("queryCon") != "") {
			odr.setQueryCondtion(downloadInfo.get("queryCon"));
		}
		odr.setStartTime(new Date());
		odr.setThreadId(getThreadID()+"");
		odr.setThreadStatus("1");
		odr.setUserId(getaUser().getUserId());
		odr.setOrgId(getaUser().getUnitId());
		odr = (OcrmFSysDownloadRecord) service.save(odr);
		return odr;
	}

	/**
	 * 导出数据文件
	 * @throws Exception
	 */
	public void doExportFile() throws Exception {
		if (exportType.equals("CSV")) {
			StringBuilder builder = new StringBuilder();
			builder.append(FileTypeConstance.getExportPath());
			if (!builder.toString().endsWith(File.separator)) {
				builder.append(File.separator);
			}
			builder.append(File.separator);
			File file = new File(builder.toString());
			if (!file.exists()) {
				file.mkdir();
			}
			builder.append(getThreadID() + ".csv");
			filename = builder.toString();
			file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 导出CSV文件
			exportCSV();
		} else if (exportType.toUpperCase().endsWith("XLS")
				|| exportType.toUpperCase().endsWith("XLSX")) {
			// 导出EXCEL文件
			exportXLS();
		}

	}

	/***
	 * 导出Excel
	 **/
	public void exportXLS() throws Exception {
		Connection conn = null;
		try {
			conn = getDatasource().getConnection();
			Statement stmt = conn.createStatement();
			setMessage("准备导出数据...");
			StringBuilder builder = new StringBuilder(SQL);
			builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
			builder.append(") SUB_QUERY");
			ResultSet rs = stmt.executeQuery(builder.toString());
			if (rs.next()) {
				total = rs.getInt("TOTAL");
			}
			setMessage("共计:" + total);
			rs = stmt.executeQuery(SQL);
			if (exportType.toUpperCase().endsWith("XLSX")) {
				poiExportXLS(rs);
			} else {
				jxlExportXLS(rs);
			}

		} finally {
			conn.close();
		}
	}

	/***
	 * 导出Excel
	 * JXL方式
	 **/
	private void jxlExportXLS(ResultSet rs) throws Exception {

		// TODO JXL导出03版本以下的excel文件

		JxlMetaBean jxlMetaBean = new JxlMetaBean();
		jxlMetaBean.setTotal(total);
		// jxlMetaBean.setExportType(exportType);
		jxlMetaBean.setTaskName(Integer.toString(getThreadID()));
		jxlMetaBean.setFieldTreeMap(fieldLabel);
		jxlMetaBean.setOracleMapping(oracleMapping);
		jxlMetaBean.writeXLS(rs, this);

	}

	/***
	 * 导出Excel
	 * POI方式
	 **/
	private void poiExportXLS(ResultSet rs) throws Exception {

		PoiMetaBean poiMetaBean = new PoiMetaBean();
		poiMetaBean.setTotal(total);
		poiMetaBean.setExportType(exportType);
		poiMetaBean.setTaskName(Integer.toString(getThreadID()));
		poiMetaBean.setFieldTreeMap(fieldLabel);
		poiMetaBean.setOracleMapping(oracleMapping);
		poiMetaBean.writeFile(rs, this);
	}
	/***
	 * 导出CSV
	 **/
	public void exportCSV() throws Exception {
		Connection conn = null;
		FileWriter writer = null;
		try {
			conn = getDatasource().getConnection();
			Statement stmt = conn.createStatement();
			writer = new FileWriter(filename);
			setMessage("准备导出数据...");
			StringBuilder builder = new StringBuilder(SQL);
			builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
			builder.append(") SUB_QUERY");
			ResultSet rs = stmt.executeQuery(builder.toString());
			if (rs.next()) {
				total = rs.getInt("TOTAL");
			}
			setMessage("共计:" + total);
			rs = stmt.executeQuery(SQL);
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
			while (rs.next() && status_downloading.equals(status)) {
				for (int i = 0; i < columnCount; i++) {
					String columnName = meta.getColumnName(i + 1);
					if (fieldLabel.containsKey(columnName)) {
						if (rs.getObject(columnName) != null) {
							writer.append(rs.getObject(columnName).toString());
						}
						writer.append(sperator);
					} else {
						if (rs.getObject(columnName) != null) {
							if (fieldLabel.containsKey(columnName + "_ORA")) {
								String lookupName = oracleMapping
										.get(columnName);
								String value = manager.getOracleValue(
										lookupName, rs.getString(columnName));
								if (value == null) {
									writer.append(rs.getString(columnName));
								} else {
									writer.append(value);
								}
								writer.append(sperator);
							}
						} else if (fieldLabel.containsKey(columnName + "_GP"))
							writer.append(sperator);
						else if (fieldLabel.containsKey(columnName + "_ORA"))
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

	public void setExpRecNum(int expRecNum) {
		this.expRecNum = expRecNum;
	}

	public int getExpRecNum() {
		return expRecNum;
	}

	public Map<String, String> getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(Map<String, String> fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public Map<String, String> getOracleMapping() {
		return oracleMapping;
	}

	public void setOracleMapping(Map<String, String> oracleMapping) {
		this.oracleMapping = oracleMapping;
	}

	public String getSQL() {
		return SQL;
	}

	public void setSQL(String sQL) {
		SQL = sQL;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setService(DownloadRecordManagerService downloadRecordManagerService) {
		this.service = downloadRecordManagerService;
	}
	public Map<String, String> getDownloadInfo() {
		return downloadInfo;
	}
	
	public void setDownloadInfo(Map<String, String> downloadInfo) {
		this.downloadInfo = downloadInfo;
	}
}
