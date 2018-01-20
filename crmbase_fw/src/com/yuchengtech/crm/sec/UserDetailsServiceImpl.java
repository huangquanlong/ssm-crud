package com.yuchengtech.crm.sec;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.bob.vo.IAuser;
import com.yuchengtech.crm.constance.OperateTypeConstant;
/**
 * UserDetailsServiceImpl 实现类
 * @author changzh@yuchengtech.com
 * @date 2012-11-15
 * 
 **/
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private static Logger log = Logger.getLogger(UserDetailsServiceImpl.class);
	
    @Autowired
    @Qualifier("dsOracle")
    private DataSource ds;
    
	/**获取用户信息*/
	public UserDetails loadUserByUsername(String name)
			throws UsernameNotFoundException, DataAccessException {
		Connection conn = null;
		
		try {
			conn = ds.getConnection();
			String authString = "SELECT COUNT(1) AS TOTLE FROM ADMIN_AUTH_ACCOUNT WHERE USER_CODE = '"+name+"' OR ACCOUNT_NAME = '"+name+"'";
			Statement stsm = conn.createStatement();
			ResultSet rs = stsm.executeQuery(authString);
			int count = 0;
			if(rs.next()) {
				count = rs.getInt("TOTLE");
			}
			if(count == 0) {
				rs.close();
				stsm.close();
				//conn.close();
				UsernameNotFoundException unfe = new UsernameNotFoundException("登录失败,用户不存在或用户名错误");
				//unfe.printStackTrace();
				printLogInfo(name, unfe);
				throw unfe;
			} else if (count > 1) {
				rs.close();
				stsm.close();
				//conn.close();
				UsernameNotFoundException unfe = new UsernameNotFoundException("登录失败,用户信息配置错误");
				//unfe.printStackTrace();
				printLogInfo(name, unfe);
				throw unfe;
			}
			authString =  "SELECT * FROM ADMIN_AUTH_ACCOUNT WHERE USER_CODE = '"+name+"' OR ACCOUNT_NAME = '"+name+"'";
			rs = stsm.executeQuery(authString);
			AuthUser iAuser = null;
			Long id = new Long(0);
			if(rs.next()){
				if ("0".equals(rs.getString("USER_STATE"))) {
					UsernameNotFoundException unfe = new UsernameNotFoundException("登录失败,用户已冻结");
					//unfe.printStackTrace();
					printLogInfo(name, unfe);
					throw unfe;
				}
				
				if (!"".equals(rs.getString("DEADLINE")) && null != rs.getString("DEADLINE")) {	
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Date cutDate = sf.parse(sf.format(new Date()));
					if (rs.getDate("DEADLINE").before(cutDate)) {
						UsernameNotFoundException unfe = new UsernameNotFoundException("登录失败,用户已超过有效期");
						//unfe.printStackTrace();
						printLogInfo(name, unfe);
						throw unfe;
					}					
				}
				
				iAuser = new IAuser();
				id = rs.getLong("ID");
				iAuser.setUsername(rs.getString("ACCOUNT_NAME"));
				iAuser.setPassword(rs.getString("PASSWORD"));
				iAuser.setUnitId(rs.getString("ORG_ID"));
				iAuser.setDptId(rs.getString("DIR_ID"));
				iAuser.setOffenIP(rs.getString("OFFENIP"));
				iAuser.setLastLoginTime(rs.getString("LASTLOGINTIME"));
				iAuser.setTemp1(rs.getString("TEMP1"));
				iAuser.setTemp2(rs.getString("TEMP2"));
				iAuser.setUserCode(rs.getString("USER_CODE"));
				//iAuser.setBelongBusiLine(rs.getString("BELONG_BUSI_LINE"));
				//iAuser.setBelongTeamHead(rs.getString("BELONG_TEAM_HEAD"));
			}
			
			authString = "SELECT COUNT(1) AS ROLES FROM ADMIN_AUTH_ACCOUNT_ROLE WHERE ACCOUNT_ID = "+id;
			
			rs = stsm.executeQuery(authString);
			
			if(rs.next())
				count = rs.getInt("ROLES");
			
			if(count == 0 ){
				rs.close();
				stsm.close();
				//conn.close();
				UsernameNotFoundException unfe = new UsernameNotFoundException("登录失败,角色异常");
				//unfe.printStackTrace();
				printLogInfo(name, unfe);
				throw unfe;
			}
			
			authString = "SELECT DISTINCT(R.ROLE_CODE),ROLE_ID FROM ADMIN_AUTH_ACCOUNT_ROLE P LEFT JOIN ADMIN_AUTH_ROLE R ON P.ROLE_ID = R.ID WHERE P.ACCOUNT_ID = "+id;
			rs = stsm.executeQuery(authString);
			
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			while(rs.next()){
				authorities.add(new GrantedAuthorityImpl(rs.getString("ROLE_ID")));
			}
			iAuser.setAuthorities(authorities);
			iAuser.setLoginType(FileTypeConstance.getSystemProperty("LOGIN_TYPE"));
			rs.close();
			stsm.close();
			
			return iAuser;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} finally {
			if(null!=conn){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**输出异常信息*/
	private void printLogInfo(String userName, AuthenticationException authException) {
		log.info(OperateTypeConstant.getOperateText(OperateTypeConstant.LOGIN_SYS) + ":[" + userName + "]" + authException.getMessage());
	}
	
}
