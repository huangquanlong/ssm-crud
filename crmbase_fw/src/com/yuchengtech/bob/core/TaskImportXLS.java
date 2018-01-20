package com.yuchengtech.bob.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.yuchengtech.bob.common.ImportSQLShop;
import com.yuchengtech.bob.importimpl.ImportInterface;
import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.upload.ImportTradeBean;

/**
 * @describe The import task created by the ExportImportManager object.
 *           The in the task, it will take the data in the XLS file to the temporary table defined by the tradeCode.
 *           And than invoke the valiClass logic.
 * @author WillJoe
 * modify by CHANGZH 2013-01-09
 * add info of running
 */
public class TaskImportXLS extends BackgroundTask{
    
    private String fileName = "";
    
    private String creator = "";
    
    private String creatororg = "";
    
    private String PKHead = "";
    /**当前导入记录数*/
    private int curRecordNum = 0;
    /**当前sheet中记录数*/
    private int curSheetRecordNum = 0;
    /**导入记录总数*/
    private int totalNum = 0;
    /**当前sheet号*/
    private int sheetNum = 0;
    /**sheet总数*/
    private int totalSheetNum = 0;
    /**当前状态:0 记录转化SQL脚本;1 执行SQL脚本; 2 导入到临时表; 3 导入完成。*/
    private int importState = 0;
    /**已导入完成sheet数*/
    private int finishSheetNum = 0;
    /**已导入完成记录数*/
    private int finishRecordNum = 0;
    
    ImportTradeBean itb;
    
    public void setImportTradeBean(ImportTradeBean importTradeBean) {
        this.itb = importTradeBean;
    }

    @Override
    public void run() {
        running = true;
        ImportSQLShop iSQLShop = new ImportSQLShop(itb);
        iSQLShop.setCreator(creator);
        iSQLShop.setCreatororg(creatororg);
        StringBuilder builder = new StringBuilder("");
        builder.append(FileTypeConstance.getImportTmpPath());
        builder.append(File.separator);
        builder.append(fileName);
        File importFile = new File(builder.toString());
        Workbook workbook = null;
        Connection connection=null;
        try {
        	setCurRecordNum(0);
            setTotalNum(0);
            setFinishRecordNum(0);
            setFinishSheetNum(0);
            //fs = new POIFSFileSystem(is);
            //wb = new HSSFWorkbook(importFile);
            //workbook = Workbook.getWorkbook(importFile);
            InputStream fs = new FileInputStream(importFile);	
            workbook = WorkbookFactory.create(fs);            
            int sheetIndex = itb.getSheetStartIndex();
            //log.info("We have got the EXCEL data, begin to import into the database...");
            connection = this.getDatasource().getConnection();
            Statement stmt = connection.createStatement();
            connection.setAutoCommit(false);
            //log.info("workbook.getSheets().length =["+workbook.getSheets().length +"]");
            /***取得sheet个数*/
            setTotalSheetNum(workbook.getNumberOfSheets());
            while(sheetIndex<workbook.getNumberOfSheets()){
            	/***当前sheet号*/
            	setSheetNum(sheetIndex);            	
                Sheet currentSheet = workbook.getSheetAt(sheetIndex);
                /**当前sheet中记录数*/
                setCurSheetRecordNum(currentSheet.getLastRowNum()>0? currentSheet.getLastRowNum():currentSheet.getLastRowNum());
                int rowIndex = 1;
                //log.info("currentSheet.getRows() =["+currentSheet.getRows() +"]");
                while(rowIndex <= currentSheet.getLastRowNum()){   
                	setImportState(0);
                    String PKvalue = PKHead+"_"+sheetIndex+"_"+rowIndex;
                    String SQL = iSQLShop.getInsertSQL(currentSheet.getRow(rowIndex),PKvalue);
                    stmt.addBatch(SQL);
                    //log.info("SQL["+rowIndex +"]=[" +SQL+"]");
                    rowIndex++;
                    setCurRecordNum(++curRecordNum);
                    setTotalNum(++totalNum);
                    if(rowIndex%100000==0){
                      log.info("Your data line has been more than 100000...");
                      setImportState(1);
                        stmt.executeBatch();                        
                        connection.commit();
                    }
                }
                setImportState(1);
                stmt.executeBatch();
                connection.commit();
                
                //log.info("Sheet "+sheetIndex +" has bean commit completely!");
                sheetIndex++;
                setFinishRecordNum(this.getCurRecordNum());
                setFinishSheetNum(++finishSheetNum);
            }
            
            stmt.close();
            //log.info("All the EXCEL data has been commit completely!");
            setImportState(2);
            ImportInterface transObject = (ImportInterface) Class.forName(itb.getValiClass()).newInstance();
            transObject.excute(connection,PKHead,getaUser());
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            ExportImportManager.getInstance().removeImportTask(this);
            running = false;
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreatororg(String creatororg) {
        this.creatororg = creatororg;
    }

    public String getPKHead() {
        return PKHead;
    }

    public void setPKHead(String pKHead) {
        PKHead = pKHead;
    }

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setCurRecordNum(int curRecordNum) {
		this.curRecordNum = curRecordNum;
	}

	public int getCurRecordNum() {
		return curRecordNum;
	}

	public void setImportState(int importState) {
		this.importState = importState;
	}

	public int getImportState() {
		return importState;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public void setTotalSheetNum(int totalSheetNum) {
		this.totalSheetNum = totalSheetNum;
	}

	public int getTotalSheetNum() {
		return totalSheetNum;
	}

	public void setCurSheetRecordNum(int curSheetRecordNum) {
		this.curSheetRecordNum = curSheetRecordNum;
	}

	public int getCurSheetRecordNum() {
		return curSheetRecordNum;
	}

	public void setFinishSheetNum(int finishSheetNum) {
		this.finishSheetNum = finishSheetNum;
	}

	public int getFinishSheetNum() {
		return finishSheetNum;
	}

	public void setFinishRecordNum(int finishRecordNum) {
		this.finishRecordNum = finishRecordNum;
	}

	public int getFinishRecordNum() {
		return finishRecordNum;
	}
    
}
