package com.yuchengtech.bob.core;

import org.apache.log4j.Logger;

import com.yuchenglicense.LicenseVerify;
import com.yuchengtech.crm.exception.BizException;

/**
 * CRM lisence管理器
 * @author WILLJOE
 * @since 2013-07-03
 */
public class CrmLisenceManager {
	
	//默认采用开发模式
	//#lisence限制级别：DEV:开发模式（不受任何约束）；DEB:DEBUG模式（调试模式，过期只警告）；DEP:部署模式（正式运行模式，过期则启动失败）
	private String lisenceModel = "DEV";
	
	private String licenseFile="";
	
	boolean verified = false;
	
	private LicenseVerify licenseVerify;
	
	public static String LISENCE_DEV = "DEV";
	public static String LISENCE_DEB = "DEB";
	public static String LISENCE_DEP = "DEP";
	
	public static String LISENCE_ERROR = "License无效或已过期，请联系CRM产品中心获取最新License文件";
	
	public static String PRODUCT_DEV_INFO = "YC.PMS";
	public static String PRODUCT_UNREG_INFO = "YC.PMS";
	
	private static Logger log = Logger.getLogger(CrmLisenceManager.class);
	
	private static CrmLisenceManager instance;
	
	public static CrmLisenceManager getInstance(){
		if(null == instance){
			instance = new CrmLisenceManager();
		}
		return instance;
	}
	
	public void initialize(){
		if(LISENCE_DEV.equals(lisenceModel)){
			verified = true;
			return;
		}else {
			verified = null!=licenseFile && !licenseFile.equals("") && verifyLicense();
			if(LISENCE_DEB.equals(lisenceModel) && !verified){
				log.warn(LISENCE_ERROR);
				return;
			}else if(LISENCE_DEP.equals(lisenceModel) && !verified){
				log.warn(LISENCE_ERROR);
				throw new BizException(1, 2, "999", LISENCE_ERROR, new String());
			}
		}
	}
	
	/**
	 * 检测lisence合法状态
	 * @return
	 */
	private boolean verifyLicense(){
		try {
			// 需要将License文件放置到合适的路径下，License文件的命名为“10位序号-1位License类型编号-10位产品编号.lic”
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			licenseFile = path + licenseFile;
			licenseVerify = new LicenseVerify(licenseFile);
			// 通过比对License文件校验码和碓冰当前系统时间，判断License文件是否有效
			if (!licenseVerify.licenseValid()) {
				log.info(licenseVerify.getErrorMessage());
				return false;
			}

			// 其他可用方法
			// 判断License许可证是否过期，如果过期返回true
			else if(licenseVerify.licenseExpired()){
				log.info(licenseVerify.getErrorMessage());
				return false;
			}
				
			// 判断License许可证是否未到启用时间，如果未到启用时间返回true
			else if(licenseVerify.licenseEarlierThanStartDate()){
				log.info(licenseVerify.getErrorMessage());
				return false;
			}else{
				// 其他可用的License信息，用于显示版权声明，所有返回值均为String类型
				// 许可证创建时间
				log.info("许可证创建时间:"+licenseVerify.getBuildTime());
				// 许可证编号
				log.info("许可证编号:"+licenseVerify.getLicenseCode());
				// 产品编号
				log.info("产品编号:"+licenseVerify.getProductCode());
				// 产品中文名称
				log.info("产品中文名称:"+licenseVerify.getProductNameZh());
				// 产品英文缩写名称（大写字母）
				log.info("产品英文缩写名称:"+licenseVerify.getProductNameEn());
				// 产品主版本号
				log.info("产品主版本号:"+licenseVerify.getVersionMajor());
				// 产品子版本号
				log.info("产品子版本号:"+licenseVerify.getVersionSub());
				// 产品发布号
				log.info("产品发布号:"+licenseVerify.getVersionPublish());
				// 产品编译日期
				log.info("产品编译日期:"+licenseVerify.getComplieDate());
				// 产品许可证类型
				log.info("产品许可证类型:"+licenseVerify.getLicenseType());
				// 产品客户使用名称
				log.info("产品客户使用名称:"+licenseVerify.getCustomerName());
				// 产品销售合同号
				log.info("产品销售合同号:"+licenseVerify.getContractCode());
				// 产品License许可证有效起始日期
				log.info("起始日期:"+licenseVerify.getLicenseStartTime());
				// 产品License许可证有效终止日期
				log.info("终止日期:"+licenseVerify.getLicenseEndTime());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	/**
	 * 获取版权所有
	 * @return
	 */
	public String getCustomerName(){
		
		if(LISENCE_DEV.equals(lisenceModel)){
			return PRODUCT_DEV_INFO;
		}else if(LISENCE_DEB.equals(lisenceModel)){
			if(null != licenseVerify && verified)
				return licenseVerify.getCustomerName();
			else 
				return PRODUCT_UNREG_INFO;
		}else{
			return PRODUCT_UNREG_INFO;
		}
	}
	
	/**
	 * 获取产品版本号
	 * @return
	 */
	public String getVersion(){
		if(null != licenseVerify){
			return licenseVerify.getVersionMajor()+"."
			+licenseVerify.getVersionSub()+"."
			+ licenseVerify.getVersionPublish();
		}else return "";
	}
	
	private CrmLisenceManager(){
	}

	public String getLisenceModel() {
		return lisenceModel;
	}

	public void setLisenceModel(String lisenceModel) {
		this.lisenceModel = lisenceModel;
	}

	public String getLicenseFile() {
		return licenseFile;
	}

	public void setLicenseFile(String licenseFile) {
		this.licenseFile = licenseFile;
	}
	
	
	
}
