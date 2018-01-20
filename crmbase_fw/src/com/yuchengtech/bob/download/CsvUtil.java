package com.yuchengtech.bob.download;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * 
 * CSV 工具类
 * @author CHANGZH
 * @date 2013-06-24
 * 
 */
public class CsvUtil {
	
	private String filename = null;
	
	private BufferedReader bufferedreader = null;
	
	private List<String> list = new ArrayList<String>();
	
	public StringBuffer[] strBufArr;
	
	//分隔符文件中分隔符 （可根据实际调整）
	private static String separator = ",";
	
	private int ColNum;

	public CsvUtil(String filename) throws IOException {
		this.filename = filename;
		bufferedreader = new BufferedReader(new FileReader(filename));
		String stemp;
		while ((stemp = bufferedreader.readLine()) != null) {
			list.add(stemp);
		}
		
		strBufArr = new StringBuffer[list.size()];
		for(int i = 0; i < strBufArr.length ; i++) {
			strBufArr[i] = new StringBuffer(list.get(i));
		}
		setColNum();
	}

	public List<String> getList() throws IOException {
		return list;
	}

	public int getRowNum() {
		return list.size();
	}

	public int getColNum() {
		if (!list.toString().equals("[]")) {
			if (list.get(0).toString().contains(separator)) {
				return list.get(0).toString().split(separator).length;
			} else if (list.get(0).toString().trim().length() != 0) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	
	public void setColNum() {
		if (!list.toString().equals("[]")) {
			if (list.get(0).toString().contains(separator)) {
				ColNum = list.get(0).toString().split(separator).length;
			} else if (list.get(0).toString().trim().length() != 0) {
				ColNum = 1;
			} else {
				ColNum = 0;
			}
		} else {
			ColNum = 0;
		}
	}


	public String getRow(int index) {
		if (this.list.size() != 0)
			return (String) list.get(index);
		else
			return null;
	}

	public String getCol(int index) {
		if (this.getColNum() == 0) {
			return null;
		}
		StringBuffer scol = new StringBuffer();
		String temp = null;
		int colnum = this.getColNum();
		if (colnum > 1) {
			for (Iterator it = list.iterator(); it.hasNext();) {
				temp = it.next().toString();
				scol = scol.append(temp.split(separator)[index] + separator);
			}
		} else {
			for (Iterator it = list.iterator(); it.hasNext();) {
				temp = it.next().toString();
				scol = scol.append(temp + separator);
			}
		}
		String str = new String(scol.toString());
		str = str.substring(0, str.length() - 1);
		return str;
	}

	public String getString(int row, int col) {
		String temp = null;
		int colnum = this.getColNum();
		if (colnum > 1) {
			temp = list.get(row).toString().split(separator)[col];
		} else if (colnum == 1) {
			temp = list.get(row).toString();
		} else {
			temp = null;
		}
		return temp;
	}
	
	public String getValueString(int row, int col) {
		String temp = null;
		
		if (ColNum > 1) {
			temp = strBufArr[row].toString().split(separator)[col];
		} else if (ColNum == 1) {
			temp = strBufArr[row].toString();
		} else {
			temp = null;
		}
		return temp;
	}

	public void CsvClose() throws IOException {
		this.bufferedreader.close();
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
