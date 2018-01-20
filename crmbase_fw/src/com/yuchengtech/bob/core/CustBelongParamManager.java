package com.yuchengtech.bob.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.yuchengtech.bcrm.system.model.FwSysProp;
import com.yuchengtech.crm.constance.SystemConstance;
/***
 * 客户归属参数管理
 * @author CHANGZH
 * @since 2012-12-27
 */ 
public class CustBelongParamManager {
    
	/**单例对象*/
    private static CustBelongParamManager instance; 
    
    /**客户类型*/
    public static final String CUST_TYPE                    = "CustOnwerPara1";
    /**客户归属管理模式*/
    public static final String CUST_MANAGER_TYPE 			= "CustOnwerPara2";
    /**归属机构的分配模式*/
    public static final String CUST_DISTRIBUTION_TYPE 		= "CustOnwerPara3";
    /**自动分配主办机构业务规则*/
    public static final String CUST_AUTO_DISTRIBUTION_RULE  = "CustOnwerPara4";    
    /**分配客户经理参数*/
    public static final String CUST_MANAGER_PARAM 			= "CustOnwerPara5";
    
    /**数据源*/
    private DataSource dsOracle;    
    /**日志*/
    private static Logger log = Logger.getLogger(CustBelongParamManager.class);
    /**客户归属参数list*/
    private List<FwSysProp> belongParamsList = new ArrayList<FwSysProp>();
    /**spring上下文*/
    private ApplicationContext applicationContext;
    /**构造方法*/
    private CustBelongParamManager() {
    	belongParamsList = new ArrayList<FwSysProp>();
    }
    /**获取单例对象*/
    public static synchronized CustBelongParamManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new CustBelongParamManager();
        }
        return instance;
    }
    /**
	 * 从数据库初始化当客户归属参数信息
	 * @param 
	 */
    private int loadCustBelongParams() {
        String SQL = "SELECT * from FW_SYS_PROP f " +
        		"where f.PROP_NAME LIKE 'CustOnwerPara%'" +
        		" and f.APP_ID ='" + SystemConstance.LOGIC_SYSTEM_APP_ID + "'";
        int rowsCached = 0;
        try {
        	Connection con=dsOracle.getConnection();
            ResultSet rs;
            PreparedStatement pstmt = con.prepareStatement(SQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	FwSysProp fwSysProp = new FwSysProp();
            	fwSysProp.setAppId(rs.getString("APP_ID"));
            	fwSysProp.setVersion(rs.getInt("VERSION"));
            	fwSysProp.setId(rs.getLong("ID"));            	
            	fwSysProp.setPropName(rs.getString("PROP_NAME"));
            	fwSysProp.setPropValue(rs.getString("PROP_VALUE"));
            	fwSysProp.setPropDesc(rs.getString("PROP_DESC"));
            	fwSysProp.setRemark(rs.getString("REMARK"));
            	belongParamsList.add(fwSysProp);
                rowsCached++;
            }
            rs.close();
            rs = null;
            con.close();
        } catch (SQLException e) {
            log.error("加载CustBelongParam时发生异常:", e);
            e.printStackTrace();
        }
        return rowsCached;
    }
    /**
	 * 初始化方法
	 * @param ApplicationContext
	 */ 
    public void initialize(ApplicationContext applicationContext) {
    	this.applicationContext = applicationContext;
        dsOracle = (DataSource) applicationContext.getBean("dsOracle");
        log.info("开始加载CustBelongParams……");
        int count = loadCustBelongParams();
        log.info("完成加载CustBelongParams，共计：" + count);
    }
    /**
	 * spring上下文信息
	 *
	 */ 
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    /**
	 * 取得客户归属参数信息
	 * @param paramName
	 * @return list
	 */
	public List<FwSysProp> getBelongParamsList(){
		return this.belongParamsList;
	}
	/**
	 * 设置客户归属参数信息
	 * @param list
	 */
	public void setBelongParamsList(List<FwSysProp> belongParamsList){
		this.belongParamsList = belongParamsList;
	}
	/**
	* 通过客户归属参数名取得参数信息
	* @param paramName
	* @return FwSysProp
	*/	
	public FwSysProp findParamItemByName(String paramName){
		for(FwSysProp fwSysProp : belongParamsList){
			if(fwSysProp.getPropName().equals(paramName)){
				return fwSysProp;
			}
		}
		return null;
	}
	/**
	* 通过客户归属参数名取得参数值
	* @param paramName  如CustBelongParamManager.CUST_TYPE
	* @return paramValue
	*/	
	public String findParamValueByName(String paramName){
		for(FwSysProp fwSysProp : belongParamsList){
			if(fwSysProp.getPropName().equals(paramName)){
				return fwSysProp.getPropValue();
			}
		}
		return null;
	}

}
