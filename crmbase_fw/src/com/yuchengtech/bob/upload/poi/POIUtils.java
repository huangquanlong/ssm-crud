package com.yuchengtech.bob.upload.poi;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * @作者: changzh
 * @日期: 2013-02-01
 * @描述: POI用于导入/导出的工具类。
 */
public class POIUtils {
	private static final long serialVersionUID = -5493681454502123207L;

	private static final Logger log = Logger.getLogger(POIUtils.class);
	
	public static final String DATA_TYPE_STRING = "STRING";

	public static final String DATA_TYPE_NUMBER = "NUMBER";

	public static final String DATA_TYPE_DATE = "DATE";

	public static final String DATA_TYPE_BOOLEAN = "BOOLEAN";

	public static final String DATA_TYPE_FORMULA = "FORMULA";

	/**
	 * 读取单元格。<br/>
	 * 
	 * @param cell 单元格
	 */
	public static Object getCellValue(Cell cell) {
		return getCellValue(cell, null);
	}

	/**
	 * 读取单元格。<br/>
	 * 
	 * @param cell 单元格
	 * @param dataType 数据类型
	 */
	public static Object getCellValue(Cell cell, String dataType) {
		Object value = null;
		if (cell == null) {
			return value;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			if (DATA_TYPE_STRING.equalsIgnoreCase(dataType)) {
				value = cell.getRichStringCellValue();
			} else if (DATA_TYPE_NUMBER.equalsIgnoreCase(dataType)) {
				double v = cell.getNumericCellValue();
				if (!Double.isNaN(v)) {
					value = new Double(v);
				}
			} else if (DATA_TYPE_DATE.equalsIgnoreCase(dataType)) {
				value = cell.getDateCellValue();
			} else if (DATA_TYPE_BOOLEAN.equalsIgnoreCase(dataType)) {
				value = new Boolean(cell.getBooleanCellValue());
			} else if (DATA_TYPE_FORMULA.equalsIgnoreCase(dataType)) {
				value = cell.getCellFormula();
			}
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				value = cell.getDateCellValue();
			} else {
				double v = cell.getNumericCellValue();
				if (!Double.isNaN(v)) {
					value = new Double(v);
				}
			}
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = new Boolean(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			value = new Byte(cell.getErrorCellValue());
			break;
		default:
			break;
		}

		return value;
	}

	/**
	 * 设置单元格。<br/>
	 * 
	 * @param cell 单元格
	 * @param value 值
	 */
	public static void setCellValue(Cell cell, Object value) {
		if (value == null) {
			if (cell instanceof XSSFCell) {
				cell.setCellValue(new XSSFRichTextString(""));
			} else {
				cell.setCellValue(new HSSFRichTextString(""));
			}			
			return;
		}

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
		case Cell.CELL_TYPE_FORMULA:
			String strValue = value.toString().trim();
			cell.setCellValue(new HSSFRichTextString(strValue));
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (value instanceof Short) {
				cell.setCellValue(((Short) value).shortValue());
			}
			if (value instanceof Integer) {
				cell.setCellValue(((Integer) value).intValue());
			}
			if (value instanceof Long) {
				cell.setCellValue(((Long) value).longValue());
			}
			if (value instanceof Float) {
				cell.setCellValue(((Float) value).floatValue());
			}
			if (value instanceof Double) {
				cell.setCellValue(((Double) value).doubleValue());
			}
			break;
		case Cell.CELL_TYPE_BLANK:
		case Cell.CELL_TYPE_BOOLEAN:
		case Cell.CELL_TYPE_ERROR:
		default:
			if (cell instanceof XSSFCell) {
				cell.setCellValue(new XSSFRichTextString(value.toString()));
			} else {
				cell.setCellValue(new HSSFRichTextString(value.toString()));
			}
			
			break;
		}
	}

	/**
	 * 复制对象列表至工作簿。<br/>
	 * 
	 * @param list 对象列表
	 * @param workbook 工作簿
	 * @param rowMapper 数据映射，[{key=对象属性名, value=对象属性值}, ...]
	 * @param sheetName 表格名称
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void copyListToWorkbook(List list, HSSFWorkbook workbook, Map rowMapper, String sheetName) throws Exception {
		// 创建sheet
		HSSFSheet sheet = workbook.createSheet(sheetName);

		// 创建rows
		int i = 0;
		int j = 0;
		// 创建header
		HSSFRow header = sheet.createRow(i++);
		for (Iterator iterator = rowMapper.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			HSSFCell cell = header.createCell((short) j++);
			setCellValue(cell, entry.getValue());
			if (log.isDebugEnabled()) {
				log.debug("RowMapper value, key=[" + entry.getKey() + "] value=[" + entry.getValue() + "]");
			}
		}
		for (Iterator it = list.iterator(); it.hasNext();) {
			// 创建内容
			Object obj = (Object) it.next();
			HSSFRow row = sheet.createRow(i++);
			j = 0;
			for (Iterator iterator = rowMapper.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				HSSFCell cell = row.createCell((short) j++);
				Object value = BeanUtils.getProperty(obj, entry.getKey().toString());
				setCellValue(cell, value);
				if (log.isDebugEnabled()) {
					log.debug("Model value, property=[" + entry.getKey() + "] value=[" + value + "]");
				}
			}
		}
	}
}
