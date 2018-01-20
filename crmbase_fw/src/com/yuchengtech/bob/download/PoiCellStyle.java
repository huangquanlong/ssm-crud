package com.yuchengtech.bob.download;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * 样式如字体、背景颜色均可在此处修改，
 * @author CHANGZH
 */
public class PoiCellStyle {
	
	/**到处信息首页页签名字*/
	public static final String EXPROT_INDEX_SHEET_NAME = "导出信息";
	
	/**首页信息格说明样式*/
	public static CellStyle getFmtIndexInfo(CellStyle fmtIndexInfo, Font font){
		try {
			/**12号字体，楷体，不加粗*/
			font.setFontHeightInPoints((short)12);
		    font.setFontName("楷体");
		    fmtIndexInfo.setFont(font);
		    fmtIndexInfo.setAlignment(CellStyle.ALIGN_CENTER);
		    fmtIndexInfo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		    fmtIndexInfo.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		    fmtIndexInfo.setFillPattern(CellStyle.SOLID_FOREGROUND);
		} catch (Exception e){
			e.printStackTrace();
		}
		return fmtIndexInfo;
	}
	
	/**导出表头样式*/
	public static CellStyle getFmtColumnNames(CellStyle fmtColumnNames, Font font){
		
		try {
			/**14号字体，楷体，加粗*/
			font.setFontHeightInPoints((short)14);
		    font.setFontName("楷体");
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		    fmtColumnNames.setFont(font);
			fmtColumnNames.setAlignment(CellStyle.ALIGN_CENTER);
			fmtColumnNames.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			fmtColumnNames.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
			fmtColumnNames.setFillPattern(CellStyle.SOLID_FOREGROUND);
		} catch (Exception e){
			e.printStackTrace();
		}
		return fmtColumnNames;
	}
	
	/**奇数行样式*/
	public static CellStyle getFmtRowDataOdd(CellStyle currRowStyle, Font font){
		try{
			/**10号字体，宋体，不加粗*/
			font.setFontHeightInPoints((short)10);
			font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		    font.setFontName("楷体");
		    currRowStyle.setFont(font);
		    currRowStyle.setAlignment(CellStyle.ALIGN_CENTER);
		    currRowStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		} catch (Exception e){
			e.printStackTrace();
		}
		return currRowStyle;
	}
	
	/**偶数行样式*/
	public static CellStyle getFmtRowDataEve(CellStyle currRowStyle, Font font){
		try{
			/**10号字体，宋体，不加粗*/
			font.setFontHeightInPoints((short)10);
			font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		    font.setFontName("楷体");
		    currRowStyle.setFont(font);
		    currRowStyle.setAlignment(CellStyle.ALIGN_CENTER);
		    currRowStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		    currRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		    currRowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		} catch (Exception e){
			e.printStackTrace();
		}
		return currRowStyle;
	}

}
