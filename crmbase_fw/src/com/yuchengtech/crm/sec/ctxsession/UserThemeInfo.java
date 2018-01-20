package com.yuchengtech.crm.sec.ctxsession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.system.ui.search.model.OcrmFSysUserCfg;
import com.yuchengtech.crm.system.ui.search.service.UserSysCfgService;

/**
 * 前端主题信息
 * @author WILLJOE
 *
 */
public class UserThemeInfo implements ICtxSessionManager {
	/**日志信息*/
	private static Logger log = Logger.getLogger(CtxSessionManager.class);
	@Autowired
	UserSysCfgService uscs;
	@Override
	public void addCtxSessionParam(AuthUser authUser) {
		OcrmFSysUserCfg userCfg = uscs.findByUser();
		if(null != userCfg){
			log.info("read theme info in database.");
			authUser.putAttribute("crm.front.BG", userCfg.getBgIcon());
			authUser.putAttribute("crm.front.TH", userCfg.getThemeCss());
			authUser.putAttribute("crm.front.WS", userCfg.getWordSize());
			authUser.putAttribute("defaultProject", userCfg.getSpareOne());
		}else{
			log.info("read default theme info in properties.");
			authUser.putAttribute("crm.front.BG", FileTypeConstance.getBipProperty("crm.front.defaultBG"));
			authUser.putAttribute("crm.front.TH", FileTypeConstance.getBipProperty("crm.front.defaultTH"));
			authUser.putAttribute("crm.front.WS", FileTypeConstance.getBipProperty("crm.front.defaultWS"));
			authUser.putAttribute("defaultProject", "");
		}
	}

}
