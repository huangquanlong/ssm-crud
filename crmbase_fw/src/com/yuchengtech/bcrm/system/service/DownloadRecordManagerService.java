package com.yuchengtech.bcrm.system.service;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.OcrmFSysDownloadRecord;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.download.DownloadThreadManager;
import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.sec.common.SystemUserConstance;
/**
 * 报表下载管理
 * @author CHANGZH@YUCHENGTECH.COM
 * @since 2013-06-17
 */
@Service
public class DownloadRecordManagerService extends CommonService{
	
	public DownloadRecordManagerService(){
		JPABaseDAO<OcrmFSysDownloadRecord, Long>  baseDAO = new JPABaseDAO<OcrmFSysDownloadRecord, Long>(OcrmFSysDownloadRecord.class);  
		super.setBaseDAO(baseDAO);
	}
	/**
	 * 删除记录
	 * 删除文件
	 * 杀掉进程
	 **/
	public void removeDownloadFile(String key) {
		long id = Long.parseLong(key);
		OcrmFSysDownloadRecord ord = em.find(OcrmFSysDownloadRecord.class, id);
		int threadId  = Integer.parseInt(ord.getThreadId());
		DownloadThreadManager.getInstance().removeDownloadThreadById(threadId);
		StringBuilder filePath = new StringBuilder();
		filePath.append(FileTypeConstance.getExportPath());
        if (!filePath.toString().endsWith(File.separator)) {
        	filePath.append(File.separator);
        }
		File file = new File(filePath.append(ord.getFileName()).toString());
		if (file.exists()) {
			file.delete();
		}
		this.batchRemove(key);
	}
	
	@SuppressWarnings("unchecked")
	public boolean isLogicSysManager() {
		AuthUser userDetails = this.getUserSession();
		boolean isLogicSysManagerFlag = false;
		for (int i = 0; i < userDetails.getRolesInfo().size(); i ++ ) {
			Map tempMap = (HashMap) userDetails.getRolesInfo().get(i);
			String roleCode = (String) tempMap.get("ROLE_CODE");
			if ( SystemUserConstance.LOGIC_SYSTEM_USER_ID.equals(roleCode) ||
				 SystemUserConstance.SUPER_SYSTEM_USER_ID.equals(roleCode) ||
				 SystemUserConstance.SYSTEM_ADMIN_ID.equals(roleCode)) {
				isLogicSysManagerFlag = true;
				break;
			}
		}
		
		return isLogicSysManagerFlag;
	}
}
