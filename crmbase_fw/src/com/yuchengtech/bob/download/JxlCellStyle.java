package com.yuchengtech.bob.download;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

/**
 * 样式未做修改,字体、背景颜色均可在此处修改，
 * 涉及行宽、列宽等操作，需要在导出方法中根据数据列的属性进行适配，暂时未做此操作。
 * 另：关于数据格式，比如身份证等数据格式不能用科学技术法展示等，也未做适配，需要涉及到具体列时再做控制。
 * @author CHANGZH
 */
public class JxlCellStyle {
	
	/**到处信息首页页签名字*/
	public static final String EXPROT_INDEX_SHEET_NAME = "导出信息";
	
	/**首页信息格说明样式*/
	public static WritableCellFormat getFmtIndexInfo(){
		WritableCellFormat fmtIndexInfo = new WritableCellFormat();
		try {
			/**12号字体，楷体，不加粗*/
			WritableFont Font12NoBold = new WritableFont(WritableFont.createFont("楷体"),12, WritableFont.NO_BOLD, false);
			fmtIndexInfo.setFont(Font12NoBold);
			fmtIndexInfo.setAlignment(Alignment.CENTRE);
			fmtIndexInfo.setVerticalAlignment(VerticalAlignment.CENTRE);
			fmtIndexInfo.setBorder(Border.ALL, BorderLineStyle.THICK);
			fmtIndexInfo.setBackground(Colour.SKY_BLUE);
		} catch (Exception e){}
		return fmtIndexInfo;
	}
	
	/**导出表头样式*/
	public static WritableCellFormat getFmtColumnNames(){
		WritableCellFormat fmtColumnNames = new WritableCellFormat();
		try {
			/**10号字体，楷体，加粗*/
			WritableFont Font10Bold = new WritableFont(WritableFont.createFont("楷体"),10, WritableFont.BOLD, false);
			fmtColumnNames.setFont(Font10Bold);
			fmtColumnNames.setAlignment(Alignment.CENTRE);
			fmtColumnNames.setVerticalAlignment(VerticalAlignment.CENTRE);
			fmtColumnNames.setBorder(Border.ALL, BorderLineStyle.THICK);
			fmtColumnNames.setBackground(Colour.GRAY_50);
		} catch (Exception e) {}
		return fmtColumnNames;
	}
	
	/**奇数行样式*/
	public static WritableCellFormat getFmtRowDataOdd(){
		WritableCellFormat fmtRowDataOdd= new WritableCellFormat();
		try{
			/**10号字体，宋体，不加粗*/
			WritableFont Font10 = new WritableFont(WritableFont.createFont("宋体"),10, WritableFont.NO_BOLD, false);
			fmtRowDataOdd.setFont(Font10);
			fmtRowDataOdd.setAlignment(Alignment.CENTRE);
			fmtRowDataOdd.setBorder(Border.ALL, BorderLineStyle.THIN);
			fmtRowDataOdd.setBackground(Colour.WHITE);
		}catch(Exception e){}
		return fmtRowDataOdd;
	}
	
	/**偶数行样式*/
	public static WritableCellFormat getFmtRowDataEve(){
		WritableCellFormat fmtRowDataEve = new WritableCellFormat();
		try{
			/**10号字体，宋体，不加粗*/
			WritableFont Font10 = new WritableFont(WritableFont.createFont("宋体"),10, WritableFont.NO_BOLD, false);
			fmtRowDataEve.setFont(Font10);
			fmtRowDataEve.setAlignment(Alignment.CENTRE);
			fmtRowDataEve.setBorder(Border.ALL, BorderLineStyle.THIN);
			fmtRowDataEve.setBackground(Colour.GRAY_25);
		}catch(Exception e){}
		return fmtRowDataEve;
	}
}
