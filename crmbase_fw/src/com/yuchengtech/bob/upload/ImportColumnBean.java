package com.yuchengtech.bob.upload;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.yuchengtech.bob.common.RegexContance;
import com.yuchengtech.bob.upload.poi.POIUtils;

/**
 * @describe This object defines a column of the trade. It will include the information of the data and the table column mapping.
 * @author WillJoe modify by CHANGZH 20130219
 */
public class ImportColumnBean {
    
    /**Column name, not a key value.*/
    String name;
    
    /**The index of the data in the sheet column.*/
    int columnIndex;
    
    /**The goal table column name.*/
    String fieldCode;
    
    /**If is PK of the goal table*/
    boolean isPK=false;
    
    /**The data type.*/
    String type;
    
    /**The data length*/
    int length;
    
    /**The precision of a number data.*/
    int precial;
    
    /**If the column allow a null data.*/
    boolean isNull=true;

    /**
     * @describe Return the insert SQL by a data line read in the EXCEL.
     * @param rowData
     * @param pkValue
     * @return
     */
    public String getValueInSQL(Row row,String pkValue){
    	List<Cell> rowData = new ArrayList();
    	for(int i = 0; i <= row.getPhysicalNumberOfCells(); i++) {
    		rowData.add(row.getCell(i));
    	}
        if(isPK){
            return "'"+pkValue+"'";
        }
        switch(FileTypeConstance.dataTypes.indexOf(type)){
        case 0:
            return formatNum(rowData);
        case 1:
            return formatVar(rowData);
        case 2:
            return formatDate(rowData);
        default:
            return formatVar(rowData);
        }
    }
    
    /**
     * @describe Get and format a number data.
     * @param rowData
     * @return
     */
    private String formatNum(List<Cell> rowData){
        String tmpData = null;
        if(columnIndex<rowData.size()){
        	Object data = POIUtils.getCellValue(rowData.get(columnIndex));
        	tmpData = data == null? "": data.toString();
        }
        if("".equals(tmpData)){
            tmpData = null;
        }
        if(null != tmpData){
        	tmpData = tmpData.trim();
        }
        
        if(null!= tmpData && !RegexContance.CheckNumric(tmpData)){
            tmpData = null;
        }
        
        if(length != 0){
            boolean isNega = false;
            String intPart = "0";
            String floatPart = "0";
            if(null!=tmpData){
                if(tmpData.startsWith("-")){
                    isNega = true;
                    tmpData = tmpData.substring(1);
                    if(tmpData==null){
                        tmpData = "0.0";
                    }
                }
                if(tmpData.indexOf(".")<0){
                    intPart=tmpData;
                }else if(tmpData.startsWith(".")){
                    floatPart = tmpData.substring(1);
                    if(floatPart==null){
                        floatPart="0";
                    }
                }else if(tmpData.endsWith(".")){
                    intPart = tmpData.substring(0,tmpData.length()-1);
                    if(intPart==null){
                        intPart="0";
                    }
                }else{
                    intPart = tmpData.substring(0,tmpData.indexOf("."));
                    floatPart = tmpData.substring(tmpData.indexOf(".")+1);
                    if(intPart==null){
                        intPart="0";
                    }
                    if(floatPart==null){
                        floatPart="0";
                    }
                }
                if(precial!=0&&precial<floatPart.length()){
                    floatPart = floatPart.substring(0,precial-1);
                }
                if(length!=0&&(length-precial)<intPart.length()){
                    intPart = intPart.substring(intPart.length()-(length-precial));
                }
                if(isNega)
                    tmpData = "-"+intPart;
                else
                    tmpData = intPart;
                if(precial != 0){
                    tmpData =tmpData +"."+floatPart;
                }
            }
        }
        if(isNull)
            return tmpData;
        else 
            return null == tmpData?"0":tmpData;
        
    }
    
   /**
    * @describe Get and format a string data.
    * @param rowData
    * @return
    */
    private String formatVar(List<Cell> rowData){
        String tmpData = null;
        if(columnIndex < rowData.size()){
        	Object data = POIUtils.getCellValue(rowData.get(columnIndex));
        	tmpData = data == null? "": data.toString();
        }
        if("".equals(tmpData)){
            tmpData=null;
        }
        if(tmpData != null && tmpData.length() > length){
            if(length != 0)
                tmpData = tmpData.substring(0, length-1);
        }
        if(isNull) 
            return "'"+tmpData+"'";
        else 
            return null==tmpData?"' '":"'"+tmpData+"'";
    }
    
    /**
     * @describe Get and format a data of date.
     * @param rowData
     * @return
     */
    private String formatDate(List<Cell> rowData){
        String tmpData = null;
        if(columnIndex < rowData.size()){
        	SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        	tmpData = sf.format((Date) POIUtils.getCellValue(rowData.get(columnIndex)));
        }
        
        if("".equals(tmpData)){
            tmpData = null;
        }
        
        if(null != tmpData && !RegexContance.CheckDate(tmpData)){
            tmpData = null;
        }
        
        if(tmpData != null){
            tmpData.replaceAll("-", "/");
        }
        
        if(tmpData != null){
            tmpData = "to_date('"+tmpData+"','dd/mm/yyyy')";
        }
        
        if(isNull)
            return tmpData;
        else 
            return null==tmpData?"SYSDATE":tmpData;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public boolean isPK() {
        return isPK;
    }

    public void setPK(boolean isPK) {
        this.isPK = isPK;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPrecial() {
        return precial;
    }

    public void setPrecial(int precial) {
        this.precial = precial;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }
    
}
