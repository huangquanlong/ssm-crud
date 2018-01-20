package com.yuchengtech.bob.upload.poi;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 样式未做修改,字体、背景颜色均可在此处修改，
 * 涉及行宽、列宽等操作，需要在导出方法中根据数据列的属性进行适配，暂时未做此操作。
 * 另：关于数据格式，比如身份证等数据格式不能用科学技术法展示等，也未做适配，需要涉及到具体列时再做控制。
 * @author CHANGZH
 */
public class ExportCellStyle {
	
	/**到处信息首页页签名字*/
	public static final String EXPROT_INDEX_SHEET_NAME = "导出信息";
	
	/**首页信息格说明样式*/
	public static CellStyle getFmtIndexInfo(Workbook wb, String type){
		CellStyle fmtIndexInfo = wb.createCellStyle();
		try {
			/**12号字体，楷体，不加粗*/
			Font font = wb.createFont();
			font.setFontHeightInPoints((short)12);
		    font.setFontName("楷体");
		    fmtIndexInfo.setFont(font);
		    fmtIndexInfo.setAlignment(CellStyle.ALIGN_CENTER);
		    fmtIndexInfo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		    fmtIndexInfo.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		    fmtIndexInfo.setFillPattern(CellStyle.SOLID_FOREGROUND);
		} catch (Exception e){}
		return fmtIndexInfo;
	}
	
	/**导出表头样式*/
	public static CellStyle getFmtColumnNames(Workbook wb, String type){
		CellStyle fmtColumnNames = wb.createCellStyle();
		try {
			/**14号字体，楷体，加粗*/
			Font font = wb.createFont();
			font.setFontHeightInPoints((short)14);
		    font.setFontName("楷体");
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		    //font.setItalic(true);
		    //font.setStrikeout(true);
			fmtColumnNames.setFont(font);
			fmtColumnNames.setAlignment(CellStyle.ALIGN_CENTER);
			fmtColumnNames.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			fmtColumnNames.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
			fmtColumnNames.setFillPattern(CellStyle.SOLID_FOREGROUND);
		} catch (Exception e) {}
		return fmtColumnNames;
	}
	
	/**奇数行样式*/
	public CellStyle getFmtRowDataOdd(CellStyle currRowStyle, Font font){
		try{
			/**10号字体，宋体，不加粗*/
			font.setFontHeightInPoints((short)10);
		    font.setFontName("楷体");
		    currRowStyle.setFont(font);
		    currRowStyle.setAlignment(CellStyle.ALIGN_CENTER);
		    currRowStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		}catch(Exception e){}
		return currRowStyle;
	}
	
	/**偶数行样式*/
	public CellStyle getFmtRowDataEve(CellStyle currRowStyle, Font font){
		try{
			/**10号字体，宋体，不加粗*/
			font.setFontHeightInPoints((short)10);
		    font.setFontName("楷体");
		    currRowStyle.setFont(font);
		    currRowStyle.setAlignment(CellStyle.ALIGN_CENTER);
		    currRowStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		    currRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		    currRowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}catch(Exception e){}
		return currRowStyle;
	}

}
