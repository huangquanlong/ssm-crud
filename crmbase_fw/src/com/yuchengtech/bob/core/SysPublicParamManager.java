package com.yuchengtech.bob.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.yuchengtech.bcrm.system.model.FwSysProp;
import com.yuchengtech.crm.constance.SystemConstance;
/***
 * 系统公共参数管理
 * @author CHANGZH
 * @since 2012-07-04
 */ 
public class SysPublicParamManager {
    
	/**单例对象*/
    private static SysPublicParamManager instance; 
    
    /**客户归属参数部分-------------------------------------------------------*/
    
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
    
    /**营销管理部分-----------------------------------------------------------*/
    
    /**营销活动审批方式*/
    public static final String MKT_APP_TYPE                 = "mktAppType";
    /**客户来源渠道*/
    public static final String AIM_CUST_SOURCE              = "aimCustSource";
    
    /**数据源*/
    private DataSource dsOracle;    
    /**日志*/
    private static Logger log = Logger.getLogger(SysPublicParamManager.class);
    /**系统公共参数参数list*/
    private List<FwSysProp> sysParamsList = new ArrayList<FwSysProp>();
    
    public void setDsOracle(DataSource dsOracle) {
		this.dsOracle = dsOracle;
	}
	/**构造方法*/
    private SysPublicParamManager() {
    	sysParamsList = new ArrayList<FwSysProp>();
    }
    /**获取单例对象*/
    public static synchronized SysPublicParamManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new SysPublicParamManager();
        }
        return instance;
    }
    /**
	 * 从数据库初始化系统公共参数信息
	 * @param 
	 */
    public int loadSysPublicParams() {
        String SQL = "SELECT * FROM FW_SYS_PROP F " +
        		"WHERE F.APP_ID ='" + SystemConstance.LOGIC_SYSTEM_APP_ID + "'";
        int rowsCached = 0;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
        	conn = dsOracle.getConnection();
            pstmt = conn.prepareStatement(SQL);
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
            	sysParamsList.add(fwSysProp);
                rowsCached++;
            }
            rs.close();
            rs = null;
        } catch (SQLException e) {
            log.error("加载SysPublicParams时发生异常:", e);
            e.printStackTrace();
        } finally {
			try {
				if(rs!=null){
					rs.close();
				}if(pstmt!=null){
					pstmt.close();
				}if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				log.error("公共参数关闭数据连接异常:", e);
				e.printStackTrace();
			}
			
		}
        return rowsCached;
    }
    /**
	 * 初始化方法
	 * @param ApplicationContext
	 */ 
    public void initialize() {
        log.info("开始加载SysPublicParams……");
        int count = loadSysPublicParams();
        log.info("完成加载SysPublicParams，共计：" + count);
    }
    
    /**
	 * 取得系统公共参数信息
	 * @param paramName
	 * @return list
	 */
	public List<FwSysProp> getSysParamsList(){
		return this.sysParamsList;
	}
	/**
	 * 设置系统公共参数信息
	 * @param list
	 */
	public void setSysParamsList(List<FwSysProp> sysParamsList){
		this.sysParamsList = sysParamsList;
	}
	/**
	* 通过系统公共参数名取得参数信息
	* @param paramName
	* @return FwSysProp
	*/	
	public FwSysProp findParamItemByName(String paramName){
		for(FwSysProp fwSysProp : sysParamsList){
			if(fwSysProp.getPropName().equals(paramName)){
				return fwSysProp;
			}
		}
		return null;
	}
	/**
	* 通过系统公共参数名取得参数值
	* @param paramName  如SysPublicParamManager.CUST_TYPE
	* @return paramValue
	*/	
	public String findParamValueByName(String paramName){
		for(FwSysProp fwSysProp : sysParamsList){
			if(fwSysProp.getPropName().equals(paramName)){
				return fwSysProp.getPropValue();
			}
		}
		return null;
	}

}
