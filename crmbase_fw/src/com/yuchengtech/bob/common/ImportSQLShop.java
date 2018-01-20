package com.yuchengtech.bob.common;

import org.apache.poi.ss.usermodel.Row;

import com.yuchengtech.bob.upload.ImportTradeBean;

/**
 * @describe The shop sails SQL, money is Cell[].
 *           You can create it by a tradeBean.
 * @author WillJoe
 */
public class ImportSQLShop {
    
    private String creator;
    
    private String creatororg;
    
    private ImportTradeBean itb;
    
    public ImportSQLShop(ImportTradeBean itb){
        this.itb = itb;
    }
    
    /**
     * @describe Get the final insert sql. Buy it, even you have no money!
     * @return
     */
    public String getInsertSQL(Row rowData,String pkValue){
        StringBuilder sbData = new StringBuilder();
        StringBuilder sbField = new StringBuilder();
        
        StringBuilder sb = new StringBuilder("INSERT INTO "+itb.getTableName()+" ");
        for(int i=0;i<itb.getImportColumnBean().size();i++){
            if(i!=0){
                sbData.append(",");
                sbField.append(",");
            }
            sbField.append(itb.getImportColumnBean().get(i).getFieldCode());
            sbData.append(itb.getImportColumnBean().get(i).getValueInSQL(rowData,pkValue));
        }
        if(itb.getCreatOrgColumn()!=null&&!"".equals(itb.getCreatOrgColumn())&&creatororg!=null){
            if(sbField.length()!=0){
                sbField.append(","+itb.getCreatOrgColumn());
                sbData.append(",'"+creatororg+"'");
            }else {
                sbField.append(itb.getCreatOrgColumn());
                sbData.append("'"+creatororg+"'");
            }
        }
        if(itb.getCreatorColumn()!=null&&!"".equals(itb.getCreatorColumn())&&creator!=null){
            if(sbField.length()!=0){
                sbField.append(","+itb.getCreatorColumn());
                sbData.append(",'"+creator+"'");
            }else {
                sbField.append(itb.getCreatorColumn());
                sbData.append("'"+creator+"'");
            }
        }
        if(itb.getCreatDateColumn()!=null&&!"".equals(itb.getCreatDateColumn())){
            if(sbField.length()!=0){
                sbField.append(","+itb.getCreatDateColumn());
                sbData.append(",sysdate");
            }else {
                sbField.append(itb.getCreatDateColumn());
                sbData.append("sysdate");
            }
        }
        
        sb.append("(");
        sb.append(sbField);
        sb.append(") ");
        sb.append("VALUES (");
        sb.append(sbData);
        sb.append(")");
        return sb.toString();
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreatororg(String creatororg) {
        this.creatororg = creatororg;
    }
}
