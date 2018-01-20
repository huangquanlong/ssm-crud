package com.yuchengtech.bob.upload;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe The trade info bean.It will be initialize when the application is started.
 * @author WillJoe
 */
public class ImportTradeBean {
    
    /**TradeCode*/
    private String tradCode;
    
    /**The temporary table name*/
    private String tableName;
    
    /**The index of the data start sheet.*/
    private int sheetStartIndex=0;
    
    /**The importing user column.It will be blanked by the current login user.*/
    private String creatorColumn;
    
    /**The importing user's organization code column. And also it will be blank by the login user's organization code*/
    private String creatOrgColumn;
    
    /**The date column. And it will be blanked by the system time.*/
    private String creatDateColumn;
    
    /**Aha! this is your own business logic. The class will take the data from the temporary table to the final table.*/
    private String valiClass = "com.yuchengtech.bob.importimpl.DefaultImpl";
    
    List<ImportColumnBean> importColumnBean = new ArrayList<ImportColumnBean>();
    
    /**
     * @describe Add a column bean to a trade bean.
     * @param icb
     */
    public void addColumn(ImportColumnBean icb){
        importColumnBean.add(icb);
    }
    
    public String getTradCode() {
        return tradCode;
    }
    public void setTradCode(String tradCode) {
        this.tradCode = tradCode;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public List<ImportColumnBean> getImportColumnBean() {
        return importColumnBean;
    }
    public void setImportColumnBean(List<ImportColumnBean> importColumnBean) {
        this.importColumnBean = importColumnBean;
    }

    public int getSheetStartIndex() {
        return sheetStartIndex;
    }

    public void setSheetStartIndex(int sheetStartIndex) {
        this.sheetStartIndex = sheetStartIndex;
    }

    public String getCreatorColumn() {
        return creatorColumn;
    }

    public void setCreatorColumn(String creatorColumn) {
        this.creatorColumn = creatorColumn;
    }

    public String getCreatOrgColumn() {
        return creatOrgColumn;
    }

    public void setCreatOrgColumn(String creatOrgColumn) {
        this.creatOrgColumn = creatOrgColumn;
    }

    public String getCreatDateColumn() {
        return creatDateColumn;
    }

    public void setCreatDateColumn(String creatDateColumn) {
        this.creatDateColumn = creatDateColumn;
    }

    public String getValiClass() {
        return valiClass;
    }

    public void setValiClass(String valiClass) {
        this.valiClass = valiClass;
    }
    
}
