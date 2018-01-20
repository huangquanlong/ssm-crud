package com.yuchengtech.bob.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @describe It will be created when the application startup. And it will always exist in the memory.And of course it's singleton.
 * @author WillJoe
 */
public class ImportTradeManage {
    
    private static Logger log = Logger.getLogger(ImportTradeManage.class);
    
    private static ImportTradeManage instance;
    
    List<ImportTradeBean> itbl = new ArrayList<ImportTradeBean>();
    
    private ImportTradeManage(){}
    
    /**
     * @describe Get the instance object of this class.
     * @return
     */
    public static ImportTradeManage getInstance(){
        if(instance == null){
            instance = new ImportTradeManage();
        }
        return instance;
    }
    
    /**
     * @describe You can get a tradeBean defined in the .XML file by it's tradeCode.
     * @param tradeCode
     * @return
     */
    public ImportTradeBean findTradeBean(String tradeCode){
        for(ImportTradeBean itb:itbl){
            if(itb.getTradCode().equals(tradeCode))
                return itb;
        }
        return null;
    }
    
    /**
     * @describe The main initialize method. It will be invoked only one time ,when the application start up.
     * @param is
     */
    @SuppressWarnings("unchecked")
    public void initialize() {
    	 String s_xmlpath="ImportTradeDefine.xml"; 
         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
         InputStream is=classLoader.getResourceAsStream(s_xmlpath); 
        log.info("Loading import trade definition...");
        Document doc = null;
        SAXReader reader = new SAXReader();    
        try {
            doc = reader.read(is);
            List<Element> tradeNodes = doc.selectNodes("root/trade");
            log.info("Finding: "+tradeNodes.size()+" sheets have bean defined!" );
            log.info("Loading ......");
            for(Element e:tradeNodes){
                ImportTradeBean tradeBean = new ImportTradeBean();
                String tradeCode = parseStrAttr(e.attributeValue("tradeCode"));
                if("".equals(tradeCode)){
                    log.info("No TradeCode!");
                    continue;
                }
                String tableName = parseStrAttr(e.attributeValue("tableName"));
                if("".equals(tableName)){
                    log.info("No tableName");
                    continue;
                }
                int sheetStartIndex = parseIntAttr(e.attributeValue("sheetStartIndex"));
                String creatorColumn = parseStrAttr(e.attributeValue("creatorColumn"));
                String creatOrgColumn = parseStrAttr(e.attributeValue("creatOrgColumn"));
                String creatDateColumn = parseStrAttr(e.attributeValue("creatDateColumn"));
                String valiClass = parseStrAttr(e.attributeValue("valiClass"));
                log.info("Find tradeCode:"+tradeCode+"; tableName:"+tableName);
                tradeBean.setTradCode(tradeCode);
                tradeBean.setTableName(tableName);
                tradeBean.setCreatorColumn(creatorColumn);
                tradeBean.setCreatOrgColumn(creatOrgColumn);
                tradeBean.setCreatDateColumn(creatDateColumn);
                tradeBean.setSheetStartIndex(sheetStartIndex);
                if(!valiClass.equals("")){
                    tradeBean.setValiClass(valiClass);
                }
                List<Element> columns = e.selectNodes("column");
                for(Element column:columns){
                    String columnName = parseStrAttr(column.attributeValue("name"));
                    int cIndex = parseIntAttr(column.attributeValue("columnIndex"));
                    String columnCode = parseStrAttr(column.attributeValue("fieldCode"));
                    boolean isPK = parseBoolAttr(column.attributeValue("isPK"));
                    String columnType = parseStrAttr(column.attributeValue("type"));
                    int columnLength = parseIntAttr(column.attributeValue("length"));
                    int columnPrecial = parseIntAttr(column.attributeValue("precial"));
                    boolean isNull = parseBoolAttr(column.attributeValue("isNull"));
                    log.info("    Find column:"+columnName+" with code "+columnCode);
                    ImportColumnBean icb = new ImportColumnBean();
                    icb.setColumnIndex(cIndex);
                    icb.setFieldCode(columnCode);
                    icb.setLength(columnLength);
                    icb.setName(columnName);
                    icb.setNull(isNull);
                    icb.setPK(isPK);
                    icb.setPrecial(columnPrecial);
                    icb.setType(columnType);
                    tradeBean.addColumn(icb);
                }
                itbl.add(tradeBean);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        log.info("Import trade load completely! Totally "+itbl.size()+" trades are initialize.");
        try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private int parseIntAttr(String att){
        return Integer.valueOf((null==att||"".equals(att))?"0":att);
    }
    
    private boolean parseBoolAttr(String att){
        return Boolean.valueOf((null==att||"".equals(att))?"false":att);
    }
    
    private String parseStrAttr(String att){
        return (null==att)?"":att;
    }
}
