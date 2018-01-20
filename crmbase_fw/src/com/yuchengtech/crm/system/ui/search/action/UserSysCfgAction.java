package com.yuchengtech.crm.system.ui.search.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.system.ui.search.model.OcrmFSysUserCfg;
import com.yuchengtech.crm.system.ui.search.service.UserSysCfgService;

/**
 * @describtion: 用户首页个性化设置
 * 
 * @author : lhqheli (email: lhqheli@gmail.com)
 * @date : 2014-08-13 11:24:20
 */
@Action("/userSysCfg")
public class UserSysCfgAction extends CommonAction{

    private static final long serialVersionUID = 1L;
    
    @Autowired
    @Qualifier("dsOracle")
    private DataSource ds;
    
    @Autowired
    private UserSysCfgService userSysCfgService;
    
    @Autowired
    public void init(){
        model = new OcrmFSysUserCfg();
        setCommonService(userSysCfgService);
    }
    
    public void prepare(){
        AuthUser auth = (AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        StringBuffer sb = new StringBuffer("SELECT t.* FROM OCRM_F_SYS_USER_CFG t WHERE t.user_id = '");
        sb.append(auth.getUserId());
        sb.append("'");
        SQL = sb.toString();
        datasource =ds;
    }
}
