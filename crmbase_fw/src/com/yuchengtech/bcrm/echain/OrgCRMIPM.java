package com.yuchengtech.bcrm.echain;


import java.security.Key;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;

import com.ecc.echain.db.DbControl;
import com.ecc.echain.log.WfLog;
import com.ecc.echain.org.OrgIF;
import com.ecc.echain.org.model.DepModel;
import com.ecc.echain.org.model.GroupModel;
import com.ecc.echain.org.model.OrgModel;
import com.ecc.echain.org.model.RoleModel;
import com.ecc.echain.org.model.UserModel;
import com.ecc.echain.workflow.cache.OUCache;
import com.ecc.echain.workflow.engine.Base;
/**
 * Title:EMP自带组织机构系统缺省实现
 * Description:处理所有与用户组织相机构关的方法
 * Copyright:yucheng Copyright (c) 2011
 * Company: yuchengtech
 * @author liujy
 * @version 3.0
 */
public class OrgCRMIPM implements OrgIF{

  /**
   * 返回根机构
   * @param Connection con：数据库连接，允许为null
   * @return OrgModel
   */
	public OrgModel getRootOrg(Connection con){
		return OUCache.getInstance().rootOrgModel;
	}
		
  /**
   * 返回所有的一级机构
   * @param Connection con：数据库连接，允许为null
   * @return List里面放OrgModel
   */
	public List getAllBaseOrgs(Connection con){
		return OUCache.getInstance().rootOrgModel.getOrgList();
	}

  /**
   * 返回机构下所有的直属子机构（不含多级子机构）
   * @param orgid：父机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放OrgModel
   */
	public List getDirectSubOrgs(String orgid,Connection con){
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om==null)
			return new ArrayList();
		return om.getOrgList();
	}

  /**
   * 返回机构下所有的子机构（包含多级子机构）
   * @param orgid：父机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放OrgModel
   */
	public List getAllSubOrgs(String orgid,Connection con){
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om==null)
			return new ArrayList();
		return om.getAllorgList();
	}
		
  /**
   * 返回机构所属的上级机构
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return 上级机构OrgModel
   */
	public OrgModel getParentOrg(String orgid,Connection con){
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om==null)
			return null;
		return (OrgModel)OUCache.getInstance().hmOMCache.get(om.getSuporgid());
	}
	
  /**
   * 返回机构对象
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return 上级机构OrgModel
   */
	public OrgModel getOrgModel(String orgid,Connection con){
		return (OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
	}
		
  /**
   * 返回所有的机构
   * @param Connection con：数据库连接，允许为null
   * @return List里面放OrgModel
   */
	public List getAllOrgs(Connection con){
		List list=new ArrayList();
		list.addAll(OUCache.getInstance().hmOMCache.values());
		return list;
	}

  /**
   * 返回机构下所有的直属部门（一级部门）
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放DepModel
   */
	public List getDirectDepsByOrg(String orgid,Connection con){
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om==null)
			return new ArrayList();
		return om.getDepList();
	}
		
  /**
   * 返回机构下所有的部门（含多级子部门）
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放DepModel
   */
	public List getAllDepsByOrg(String orgid,Connection con){
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om==null)
			return new ArrayList();
		return om.getAlldepList();
	}

  /**
   * 返回部门下所有的直属子部门（不含多级子部门）
   * @param orgid：机构id
   * @param depid：父部门id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放DepModel
   */
	public List getDirectSubDepsByDep(String orgid, String depid,Connection con){
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om==null)
			return new ArrayList();
		return om.getDepModel(depid).getDepList();
	}

  /**
   * 返回部门底下所有的子部门（含多级子部门）
   * @param orgid：机构id
   * @param depid：父部门id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放DepModel
   */
	public List getAllSubDepsByDep(String orgid, String depid,Connection con){
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om==null)
			return new ArrayList();
		return om.getDepModel(depid).getAlldepList();
	}

  /**
   * 返回部门所属的上级部门
   * @param orgid：机构id
   * @param depid：部门id
   * @param Connection con：数据库连接，允许为null
   * @return DepModel，如果是一级部门，则返回null
   */
	public DepModel getParentDep(String orgid, String depid,Connection con){
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om==null)
			return null;
		return om.getDepModel(om.getDepModel(depid).getSupdepid());
	}
		
  /**
   * 返回部门对象
   * @param orgid：机构id
   * @param depid：部门id
   * @param Connection con：数据库连接，允许为null
   * @return DepModel
   */
	public DepModel getDepModel(String orgid, String depid,Connection con){
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om==null)
			return null;
		return om.getDepModel(depid);
	}
		
  /**
   * 返回机构下所有直属用户（直接属于机构而不属于部门的）
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getDirectUsersByOrg(String orgid,Connection con){
		List<UserModel> list=new ArrayList<UserModel>();
		UserModel um;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
        	vecData=db.performQuery("select t1.account_name,t1.user_name,t1.user_state,t1.email,t1.mobilephone from admin_auth_account t1 " +
        			"where t1.org_id='"+orgid+"' ", con);
        	for(int i=0;i<vecData.size();i++){
        		vecRow=(Vector)vecData.elementAt(i);
        		um=new UserModel();
        		um.setUserid((String)vecRow.elementAt(0));
        		um.setUsername((String)vecRow.elementAt(1));
        		um.setUserstatus(Integer.parseInt((String)vecRow.elementAt(2)==null?"-1":(String)vecRow.elementAt(2)));
        		um.setEmail((String)vecRow.elementAt(3));
        		um.setMobile((String)vecRow.elementAt(4));
        		um.setOrgid(orgid);
        		um.setDepid(null);
        		list.add(um);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return list;
	}
		
  /**
   * 返回机构下所有的用户
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getAllUsersByOrg(String orgid,Connection con){
		List<UserModel> list=new ArrayList<UserModel>();
		UserModel um;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
        	vecData=db.performQuery("select t1.account_name,t1.user_name,t1.user_state,t1.email,t1.mobilephone,t1.org_id from admin_auth_account t1 " +
        			" where t1.org_id='"+orgid+"' ", con);
        	for(int i=0;i<vecData.size();i++){
        		vecRow=(Vector)vecData.elementAt(i);
        		um=new UserModel();
        		um.setUserid((String)vecRow.elementAt(0));
        		um.setUsername((String)vecRow.elementAt(1));
        		um.setUserstatus(Integer.parseInt((String)vecRow.elementAt(2)==null?"0":(String)vecRow.elementAt(2)));
        		um.setEmail((String)vecRow.elementAt(3));
        		um.setMobile((String)vecRow.elementAt(4));
        		um.setOrgid(orgid);
        		um.setDepid((String)vecRow.elementAt(5));
        		list.add(um);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return list;
	}

  /**
   * 返回部门下所有直属用户（不含子部门）
   * @param orgid：机构id
   * @param depid：部门id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getDirectUsersByDep(String orgid, String depid,Connection con){
		List<UserModel> list=new ArrayList<UserModel>();
		UserModel um;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
        	vecData=db.performQuery("select t1.account_name,t1.user_name,t1.user_state,t1.email,t1.mobilephone from admin_auth_account t1  " +
        			" where t1.org_id='"+depid+"' ", con);
        	for(int i=0;i<vecData.size();i++){
        		vecRow=(Vector)vecData.elementAt(i);
        		um=new UserModel();
        		um.setUserid((String)vecRow.elementAt(0));
        		um.setUsername((String)vecRow.elementAt(1));
        		um.setUserstatus(Integer.parseInt((String)vecRow.elementAt(2)==null?"0":(String)vecRow.elementAt(2)));
        		um.setEmail((String)vecRow.elementAt(3));
        		um.setMobile((String)vecRow.elementAt(4));
        		um.setOrgid(orgid);
        		um.setDepid(depid);
        		list.add(um);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return list;
	}

  /**
   * 返回部门下所有的用户（含多级子部门）
   * @param orgid：机构id
   * @param depid：部门id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getAllUsersByDep(String orgid, String depid,Connection con){
		List<UserModel> list=new ArrayList<UserModel>();
		list.addAll(getDirectUsersByDep(orgid,depid,con));
		DepModel dm=getDepModel(orgid,depid,con);
		for(int i=0;i<dm.getAlldepList().size();i++){
			list.addAll(getDirectUsersByDep(orgid,((DepModel)dm.getAlldepList().get(i)).getDepid(),con));
		}
		return list;
	}

  /**
   * 取得用户对象
   * @param orgid：机构id
   * @param userid：用户id
   * @param Connection con：数据库连接，允许为null
   * @return UserModel
   */
	public UserModel getUserModel(String orgid, String userid,Connection con){
		UserModel um=null;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
        	vecData=db.performQuery("select t1.account_name,t1.user_name,t1.user_state,t1.email,t1.mobilephone,t1.org_id from admin_auth_account t1  " +
        			" where t1.org_id='"+orgid+"' and t1.account_name='"+userid+"'", con);
        	if(vecData!=null&&vecData.size()>0){
        		vecRow=(Vector)vecData.elementAt(0);
        		um=new UserModel();
        		um.setUserid((String)vecRow.elementAt(0));
        		um.setUsername((String)vecRow.elementAt(1));
        		um.setUserstatus(Integer.parseInt((String)vecRow.elementAt(2)==null?"0":(String)vecRow.elementAt(2)));
        		um.setEmail((String)vecRow.elementAt(3));
        		um.setMobile((String)vecRow.elementAt(4));
        		um.setOrgid(orgid);
        		um.setDepid((String)vecRow.elementAt(5));
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return um;
	}
	public UserModel getUserModel(String userid,Connection con){
		UserModel um=null;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
        	vecData=db.performQuery("select t1.account_name,t1.user_name,t1.user_state,t1.email,t1.mobilephone,t1.org_id,t1.org_id from admin_auth_account t1  " +
        			" where t1.account_name='"+userid+"'", con);
        	if(vecData!=null&&vecData.size()>0){
        		vecRow=(Vector)vecData.elementAt(0);
        		um=new UserModel();
        		um.setUserid((String)vecRow.elementAt(0));
        		um.setUsername((String)vecRow.elementAt(1));
        		um.setUserstatus(Integer.parseInt((String)vecRow.elementAt(2)==null?"0":(String)vecRow.elementAt(2)));
        		um.setEmail((String)vecRow.elementAt(3));
        		um.setMobile((String)vecRow.elementAt(4));
        		um.setOrgid((String)vecRow.elementAt(5));
        		um.setDepid((String)vecRow.elementAt(6));
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return um;
	}
	
  /**
   * 取得用户所在的机构
   * @param userid：用户id
   * @param Connection con：数据库连接，允许为null
   * @return String orgid，如果找不到则返回null
   */
	public String getOrgIdByUser(String userid,Connection con){
		String orgid=null;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
        	String[] useridString = userid.split(";");
        	vecData=db.performQuery("select t1.org_id from  admin_auth_account t1  where t1.account_name='"+useridString[0]+"'", con);
        	if(vecData!=null&&vecData.size()>0){
        		vecRow=(Vector)vecData.elementAt(0);
        		orgid=(String)vecRow.elementAt(0);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return orgid;
	}
		
  /**
   * 登录验证
   * @param orgid：机构id
   * @param userid：用户id
   * @param password：用户密码
   * @param Connection con：数据库连接，允许为null
   * @return UserModel，如果是无效用户（不合法），返回null
   */
	public UserModel isValidUser(String orgid, String userid, String password,Connection con){
		//密码加密算法
		try{
			//BASE64Decoder base64decoder = new BASE64Decoder();
			//BASE64Encoder base64encoder = new BASE64Encoder();
			//Base64.decodeBase64(str).toString();
			//byte[] keyBytes = base64decoder.decodeBuffer("lianame");
			byte[] keyBytes = Base64.decodeBase64("lianame");//base64decoder.decodeBuffer("lianame");
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			generator.init(new SecureRandom(keyBytes));
			Key key = generator.generateKey();
			Cipher cipher =Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptBytes =cipher.doFinal(password.getBytes("UTF-8"));
			//Base64.encodeBase64(str.getBytes("UTF-8")).toString();
			//password = base64encoder.encode(encryptBytes);
			password = Base64.encodeBase64(encryptBytes).toString();
		}catch(Exception e){
			WfLog.log(WfLog.ERROR,"isValidUser密码加密算法执行异常");
			e.printStackTrace();
		}		
		UserModel um=null;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
        	vecData=db.performQuery("select t1.account_name,t1.user_name,t1.user_state,t1.email,t1.mobilephone,t1.org_id,t1.org_id from admin_auth_account t1 " +
        			" where t1.account_name='"+userid+"' and t1.password='"+password+"' and t1.org_id='"+orgid+"'", con);
        	if(vecData!=null&&vecData.size()>0){
        		vecRow=(Vector)vecData.elementAt(0);
        		um=new UserModel();
        		um.setUserid((String)vecRow.elementAt(0));
        		um.setUsername((String)vecRow.elementAt(1));
        		um.setUserstatus(Integer.parseInt((String)vecRow.elementAt(2)==null?"0":(String)vecRow.elementAt(2)));
        		um.setEmail((String)vecRow.elementAt(3));
        		um.setMobile((String)vecRow.elementAt(4));
        		um.setOrgid((String)vecRow.elementAt(5));
        		um.setDepid((String)vecRow.elementAt(6));
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return um;
	}
		
  /**
   * 返回机构领导（正职、副职）
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getOrgLeaders(String orgid,Connection con){
		return null;
	}
	
  /**
   * 返回机构主管（正职）
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getOrgDirectors(String orgid,Connection con){
		return null;
	}

  /**
   * 返回部门领导（正职、副职）
   * @param orgid：机构id
   * @param depid：部门id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getDepLeaders(String orgid, String depid,Connection con){
		return null;
	}

  /**
   * 返回部门主管（正职）
   * @param orgid：机构id
   * @param depid：部门id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getDepDirectors(String orgid, String depid,Connection con){
		return null;
	}
	
  /**
   * 取得用户所在的部门
   * @param userid：用户id
   * @param Connection con：数据库连接，允许为null
   * @return String depid，如果找不到则返回null
   */
	public String getDepIdByUser(String userid,Connection con){
		String depid=null;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
        	vecData=db.performQuery("select t1.org_id from  admin_auth_account t1  where t1.account_name='"+userid+"'", con);
        	if(vecData!=null&&vecData.size()>0){
        		vecRow=(Vector)vecData.elementAt(0);
        		depid=(String)vecRow.elementAt(0);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return depid;
	}

  /**
   * 返回系统中所有全局角色（不属于某一个机构的角色）
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放RoleModel
   */
	public List getAllBaseRoles(Connection con){
		List list=new ArrayList();
		list.addAll(OUCache.getInstance().hmRMCache.values());
		return list;
	}
	
  /**
   * 返回系统中所有角色
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放RoleModel
   */
	public List getAllRoles(Connection con){
		List list=new ArrayList();
		list.addAll(OUCache.getInstance().hmRMCache.values());
		Iterator it=OUCache.getInstance().hmOMCache.keySet().iterator();
		OrgModel om;
		String orgid;
		while(it.hasNext()){
			orgid=(String)it.next();
			om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
			list.addAll(om.getRoleList());
		}
		return list;
	}
		
  /**
   * 返回机构下所有角色（也包含全局角色）
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放RoleModel
   */
	public List getAllRoles(String orgid,Connection con){
		List list=new ArrayList();
		list.addAll(OUCache.getInstance().hmRMCache.values());
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om!=null)
			list.addAll(om.getRoleList());
		return list;
	}
	
  /**
   * 返回部门下所有角色（也包含全局角色）
   * @param orgid：机构id
   * @param depid：部门id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放RoleModel
   */
	public List getAllRoles(String orgid,String depid,Connection con){
		List list=new ArrayList();
		list.addAll(OUCache.getInstance().hmRMCache.values());
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om!=null)
			list.addAll(om.getDepModel(depid).getRoleList());
		return list;
	}
	
  /**
   * 取得角色对象
   * @param orgid：机构id
   * @param roleid：角色id
   * @param Connection con：数据库连接，允许为null
   * @return RoleModel
   */
	public RoleModel getRoleModel(String orgid, String roleid,Connection con){
		if(orgid==null||orgid.equals(""))
			return (RoleModel)OUCache.getInstance().hmRMCache.get(roleid);
		RoleModel rm=((OrgModel)OUCache.getInstance().hmOMCache.get(orgid)).getRoleModel(roleid);
		if(rm!=null)
			return rm;
		else
			return (RoleModel)OUCache.getInstance().hmRMCache.get(roleid);
	}

  /**
   * 返回角色下用户
   * @param orgid：机构id
   * @param roleid：角色id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getUsersByRole(String orgid, String roleid,Connection con){
		List<UserModel> list=new ArrayList<UserModel>();
		UserModel um;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
        	vecData=db.performQuery("select a.account_name,a.user_name,a.user_state,a.email,a.mobilephone,a.org_id,a.org_id " +
        			"from admin_auth_account a left join admin_auth_account_role b on a.id = b.account_id  " +
        			" where   b.role_id="+roleid+"", con);
        	for(int i=0;i<vecData.size();i++){
        		vecRow=(Vector)vecData.elementAt(i);
        		um=new UserModel();
        		um.setUserid((String)vecRow.elementAt(0));
        		um.setUsername((String)vecRow.elementAt(1));
        		um.setUserstatus(Integer.parseInt(((String)vecRow.elementAt(2)==null||(boolean)vecRow.elementAt(2).equals(""))?"0":(String)vecRow.elementAt(2)));
        		um.setEmail((String)vecRow.elementAt(3));
        		um.setMobile((String)vecRow.elementAt(4));
        		um.setOrgid((String)vecRow.elementAt(5));
        		um.setDepid((String)vecRow.elementAt(6));
        		list.add(um);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return list;
	}

  /**
   * 返回用户拥有的角色
   * @param orgid：机构id
   * @param userid：用户id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放RoleModel
   */
	public List getRolesByUser(String orgid, String userid,Connection con){
		List<RoleModel> list=new ArrayList<RoleModel>();
		RoleModel rolemodel;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//km modify
    		vecData=db.performQuery("select d.id,d.role_type,d.role_name,d.role_code,a.org_id " +
    				"from admin_auth_account a left join admin_auth_account_role b on a.id = b.account_id " +
        			" left join admin_auth_role d on b.role_id = d.id " +
        			" where a.id='"+userid+"'",con);
        	for(int j=0;j<vecData.size();j++){
        		vecRow=(Vector)vecData.elementAt(j);
        		rolemodel=new RoleModel();
        		rolemodel.setRoleid((String)vecRow.elementAt(0));
        		rolemodel.setRoletype((String)vecRow.elementAt(1));
        		rolemodel.setRolename((String)vecRow.elementAt(2));
        		rolemodel.setRoleright((String)vecRow.elementAt(3));
        		rolemodel.setOrgid((String)vecRow.elementAt(4));
        		rolemodel.setDepid((String)vecRow.elementAt(5));
        		list.add(rolemodel);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return list;
	}

  /**
   * 返回系统中所有的全局群组/岗位（不属于某一个机构的群组/岗位）
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放GroupModel
   */
	public List getAllBaseGroups(Connection con){
		List list=new ArrayList();
		list.addAll(OUCache.getInstance().hmGMCache.values());
		return list;
	}
	
  /**
   * 返回系统中所有的群组（岗位）
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放GroupModel
   */
	public List getAllGroups(Connection con){
		List list=new ArrayList();
		list.addAll(OUCache.getInstance().hmGMCache.values());
		Iterator it=OUCache.getInstance().hmOMCache.keySet().iterator();
		OrgModel om;
		String orgid;
		while(it.hasNext()){
			orgid=(String)it.next();
			om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
			list.addAll(om.getGroupList());
		}
		return list;
	}
		
  /**
   * 返回机构下的群组（岗位）(也包括全局岗位)
   * @param orgid：机构id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放GroupModel
   */
	public List getAllGroups(String orgid,Connection con){
		List list=new ArrayList();
		list.addAll(OUCache.getInstance().hmGMCache.values());
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om!=null)
			list.addAll(om.getGroupList());
		return list;
	}
	
  /**
   * 返回部门下的群组（岗位）(也包括全局岗位)
   * @param orgid：机构id
   * @param depid：部门id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放GroupModel
   */
	public List getAllGroups(String orgid,String depid,Connection con){
		List list=new ArrayList();
		list.addAll(OUCache.getInstance().hmGMCache.values());
		OrgModel om=(OrgModel)OUCache.getInstance().hmOMCache.get(orgid);
		if(om!=null)
			list.addAll(om.getDepModel(depid).getGroupList());
		return list;
	}
		
  /**
   * 取得群组（岗位）对象
   * @param orgid：机构id
   * @param groupid：工作组id
   * @param Connection con：数据库连接，允许为null
   * @return GroupModel
   */
	public GroupModel getGroupModel(String orgid, String groupid,Connection con){
		if(orgid==null||orgid.equals(""))
			return (GroupModel)OUCache.getInstance().hmGMCache.get(groupid);
		GroupModel gm=((OrgModel)OUCache.getInstance().hmOMCache.get(orgid)).getGroupModel(groupid);
		if(gm!=null)
			return gm;
		else
			return (GroupModel)OUCache.getInstance().hmRMCache.get(groupid);
	}
		
  /**
   * 返回群组（岗位）下用户
   * @param orgid：机构id
   * @param groupid：工作组id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放UserModel
   */
	public List getUsersByGroup(String orgid, String groupid,Connection con){
		List<UserModel> list=new ArrayList<UserModel>();
		UserModel um;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//no modify
        	vecData=db.performQuery("select a.actorno,a.actorname,a.state,a.usermail,a.mobile,a.organno,a.depno from s_user a,s_dutyuser b where a.actorno=b.actorno and a.organno='"+orgid+"' and b.dutyno='"+groupid+"'", con);
        	for(int i=0;i<vecData.size();i++){
        		vecRow=(Vector)vecData.elementAt(i);
        		um=new UserModel();
        		um.setUserid((String)vecRow.elementAt(0));
        		um.setUsername((String)vecRow.elementAt(1));
        		um.setUserstatus(Integer.parseInt((String)vecRow.elementAt(2)==null?"0":(String)vecRow.elementAt(2)));
        		um.setEmail((String)vecRow.elementAt(3));
        		um.setMobile((String)vecRow.elementAt(4));
        		um.setOrgid((String)vecRow.elementAt(5));
        		um.setDepid((String)vecRow.elementAt(6));
        		list.add(um);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return list;
	}

  /**
   * 返回用户拥有的群组（岗位）
   * @param orgid：机构id
   * @param userid：用户id
   * @param Connection con：数据库连接，允许为null
   * @return List里面放GroupModel
   */
	public List getGroupByUser(String orgid, String userid,Connection con){
		List<GroupModel> list=new ArrayList<GroupModel>();
		GroupModel groupmodel;
		DbControl db=DbControl.getInstance();
        Vector vecData,vecRow;
        boolean bClose=false;
        try{
        	if(con==null){
        		con = db.getConnection();
        		bClose=true;
        	}//no modify
    		vecData=db.performQuery("select a.dutyno,a.dutyname,a.dutyright,a.organno,a.depno from s_duty a,s_dutyuser b where a.dutyno=b.dutyno and b.actorno='"+userid+"'",con);
        	for(int j=0;j<vecData.size();j++){
        		vecRow=(Vector)vecData.elementAt(j);
        		groupmodel=new GroupModel();
        		groupmodel.setGroupid((String)vecRow.elementAt(0));
        		groupmodel.setGroupname((String)vecRow.elementAt(1));
        		groupmodel.setGroupright((String)vecRow.elementAt(2));
        		groupmodel.setOrgid((String)vecRow.elementAt(3));
        		groupmodel.setDepid((String)vecRow.elementAt(4));
        		list.add(groupmodel);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(bClose&&con!=null){
        		try{
        			db.freeConnection(con);
        		}catch(Exception e){
        	        WfLog.log(WfLog.ERROR,Base.CanNotReleaseDatabaseConnect);
        	        e.printStackTrace();
        	    }
        	}
        }
		return list;
	}

  /**
   * 获取用户授权信息
   * @param orgid：机构id
   * @param userid：用户id
   * @param appid：应用模块ID
   * @param Connection con：数据库连接，允许为null
   * @return 授权的用户id，如果为空""或者null则说明没有授权
   */
	public String getGrantor(String orgid, String userid, String appid,Connection con){
		return null;
	}
	
  /**
   * 取得上一级用户
   * @param orgid：机构id
   * @param userid：用户id
   * @param Connection con：数据库连接，允许为null
   * @return List，里面放UserModel
   */
	public List getSuperiorUsers(String orgid, String userid,Connection con){
		//这里实现为取到用户所在机构的上一级机构直属人员
		OrgModel om=getParentOrg(getOrgIdByUser(userid,con),con);
		return getDirectUsersByOrg(om.getOrgid(),con);
	}

  /**
   * 取得下级办理人
   * @param orgid：机构id
   * @param userid：用户id
   * @param Connection con：数据库连接，允许为null
   * @return List，里面放UserModel
   */
	public List getJuniorUsers(String orgid, String userid,Connection con){
		//这里实现为取到用户所在机构的直属子机构人员		
		List al=getDirectSubOrgs(getOrgIdByUser(userid,con),con);
		if(al==null||al.size()==0)
			return new ArrayList();
		List list=new ArrayList();
		OrgModel om;
		for(int i=0;i<al.size();i++){
			om=(OrgModel)al.get(i);
			list.addAll(getDirectUsersByOrg(om.getOrgid(),con));
		}
		return list;
	}

  /**
   * 取得同部门人员
   * @param orgid：机构id
   * @param userid：用户id
   * @param Connection con：数据库连接，允许为null
   * @return List，里面放UserModel
   */
	public List getSameDepUsers(String orgid, String userid,Connection con){
		return getDirectUsersByDep(orgid,getDepIdByUser(userid,con),con);
	}
	
  /**
   * 取得同机构人员
   * @param orgid：机构id
   * @param userid：用户id
   * @param Connection con：数据库连接，允许为null
   * @return List，里面放UserModel
   */
	public List getSameOrgUsers(String orgid, String userid,Connection con){
		String organno=getOrgIdByUser(userid,con);
		return getDirectUsersByOrg(organno,con);
	}
		
  /**
   * 获取用户邮件地址
   * @param orgid：机构id
   * @param userid：用户id,允许多值（用";"隔开）
   * @param Connection con：数据库连接，允许为null
   * @return String，用户的邮件地址
   */
	public String getUserEmail(String orgid,String userid,Connection con){
		if(userid==null||userid.equals(""))
			return null;
		else if(userid.indexOf(";")==-1)
			return getUserModel(orgid,userid,con).getEmail();
		else{//允许多值（用";"隔开）
			String strResult="";
			StringTokenizer st = new StringTokenizer(userid,";");
		    while (st.hasMoreElements()) {
		  		strResult+=";"+getUserModel(orgid,(String)st.nextElement(),con).getEmail();
		    }
		    if(strResult.length()>1)
	        	strResult=strResult.substring(1);
			return strResult;
		}
	}
	
  /**
   * 加载组织用户到缓存中,根据项目需要决定是否实现
   * @param OUCache oucache：组织用户缓存对象
   * @param Connection con：数据库连接，允许为null
   * @return void
   */
	public void loadOUCache(OUCache oucache,Connection con){
		oucache.hmOMCache.clear();//首先清空hmOMCache的内容
    	oucache.hmRMCache.clear();
    	oucache.hmGMCache.clear();
    	OrgModel orgmodel;
        DepModel depmodel;
        RoleModel rolemodel;
        GroupModel groupmodel;
        String sqlstr,orgid,suporgid,depid,supdepid,roleid,groupid;
        DbControl db=DbControl.getInstance();
        Vector vecData,vecRow,vecData2,vecRow2;
        try{
        	//首先读取机构信息//km modify
        	sqlstr="select org_id,up_org_id,org_id,org_name,org_level from admin_auth_org  order by org_level,id";
        	vecData=db.performQuery(sqlstr,con);
        	for(int i=0;i<vecData.size();i++){
        		vecRow=(Vector)vecData.elementAt(i);
        		orgid=(String)vecRow.elementAt(0);
        		suporgid=(String)vecRow.elementAt(1);
        		orgmodel=new OrgModel();
        		orgmodel.setOrgid(orgid);
        		orgmodel.setOrgcode((String)vecRow.elementAt(2));
        		orgmodel.setOrgname((String)vecRow.elementAt(3));
        		orgmodel.setOrgstatus(0);
        		oucache.hmOMCache.put(orgid, orgmodel);
        		if("1".equals(vecRow.elementAt(4))){//根机构
        			orgmodel.setSuporgid(null);
        			orgmodel.setOrglevel(1);
        			oucache.rootOrgModel=orgmodel;
        		}else{
        			orgmodel.setSuporgid(suporgid);
        			orgmodel.setOrglevel(Integer.parseInt((String)vecRow.elementAt(4)==null?"-1":(String)vecRow.elementAt(4)));
        			if(oucache.hmOMCache.containsKey(suporgid)){
        				((OrgModel)oucache.hmOMCache.get(suporgid)).getOrgList().add(orgmodel);
        				OrgModel omtmp=orgmodel;
        				while(omtmp.getSuporgid()!=null){
        					if(oucache.hmOMCache.containsKey(omtmp.getSuporgid())){
        						omtmp=(OrgModel)oucache.hmOMCache.get(omtmp.getSuporgid());
            					omtmp.getAllorgList().add(orgmodel);
        					}else{
        						break;
        					}            					
        				}            				
        			}
        		}
        		//读取机构下部门//km modify
        		sqlstr="select org_id,up_org_id,org_name from admin_auth_org where org_level = '4' and org_id='"+orgid+"' order by id";
        		vecData2=db.performQuery(sqlstr,con);
            	for(int j=0;j<vecData2.size();j++){
            		vecRow2=(Vector)vecData2.elementAt(j);
            		depid=(String)vecRow2.elementAt(0);
            		supdepid=(String)vecRow2.elementAt(1);
            		depmodel=new DepModel();
            		depmodel.setDepid(depid);
            		depmodel.setDepname((String)vecRow2.elementAt(2));
            		depmodel.setOrgid(orgid);
            		depmodel.setDepstatus(0);
            		if(supdepid==null||supdepid.equals("")||supdepid.equals("1")){//一级部门
            			depmodel.setSupdepid(null);
            			orgmodel.getDepList().add(depmodel);
            			orgmodel.getAlldepList().add(depmodel);
            		}else{
            			depmodel.setSupdepid(supdepid);
            			orgmodel.getAlldepList().add(depmodel);
            			DepModel supdm=orgmodel.getDepModel(supdepid);
            			if(supdm!=null){//把当前部门加入到上级部门中
            				supdm.getDepList().add(depmodel);
            				supdm.getAlldepList().add(depmodel);
            				while(supdm.getSupdepid()!=null){
            					supdm=orgmodel.getDepModel(supdm.getSupdepid());
            					if(supdm!=null){
            						supdm.getAlldepList().add(depmodel);
            					}else{
            						break;
            					}
            				} 
            			}
            		}
            	}
            	/*//读取机构下角色 //km modify
            	sqlstr="select t1.id,t1.role_type,t1.role_name,t1.role_code,t3.org_id from admin_auth_role t1 inner join " +
            			"admin_auth_account_role t2 on t1.id = t2.role_id inner join admin_auth_account_org t3" +
            			" on t2.account_id = t3.account_id where  t3.org_id='"+orgid+"' order by t1.id";
        		vecData2=db.performQuery(sqlstr,con);
            	for(int j=0;j<vecData2.size();j++){
            		vecRow2=(Vector)vecData2.elementAt(j);
            		roleid=(String)vecRow2.elementAt(0);
            		rolemodel=new RoleModel();
            		rolemodel.setRoleid(roleid);
            		rolemodel.setRoletype((String)vecRow2.elementAt(1));
            		rolemodel.setRolename((String)vecRow2.elementAt(2));  
            		rolemodel.setRoleright((String)vecRow2.elementAt(3)); 
            		rolemodel.setOrgid(orgid);
            		rolemodel.setDepid(null);
            		rolemodel.isBaseFlag=false;
            		orgmodel.getRoleList().add(rolemodel);
            		//oucache.hmRMCache.put(roleid, rolemodel);//非全局角色
            		if(vecRow2.elementAt(4)!=null&&vecRow2.elementAt(4).toString().trim().length()>0)
            			orgmodel.getDepModel((String)vecRow2.elementAt(4)).getRoleList().add(rolemodel);
            	}*/
            	/*//读取机构下群组（岗位）//km modify
            	sqlstr="select t1.account_name,t1.account_name,t4.role_code,t3.org_id from admin_auth_account t1 left join " +
            			"admin_auth_account_role t2 on t1.id = t2.account_id left join " +
            			"admin_auth_role t4 on t2.role_id = t4.id left join admin_auth_account_org t3 on" +
            			" t1.id = t3.account_id where t3.org_id ='"+orgid+"' order by t3.org_id,t1.id";
        		vecData2=db.performQuery(sqlstr,con);
            	for(int j=0;j<vecData2.size();j++){
            		vecRow2=(Vector)vecData2.elementAt(j);
            		groupid=(String)vecRow2.elementAt(0);
            		groupmodel=new GroupModel();
            		groupmodel.setGroupid(groupid);
            		groupmodel.setGroupname((String)vecRow2.elementAt(1));
            		groupmodel.setGroupright((String)vecRow2.elementAt(2));
            		groupmodel.setOrgid(orgid);
            		groupmodel.setDepid(null);
            		groupmodel.isBaseFlag=false;
            		orgmodel.getGroupList().add(groupmodel);
            		//oucache.hmGMCache.put(groupid, groupmodel);//非全局岗位
            		if(vecRow2.elementAt(3)!=null&&vecRow2.elementAt(3).toString().trim().length()>0)
            			orgmodel.getDepModel((String)vecRow2.elementAt(3)).getGroupList().add(groupmodel);
            	}*/
        	}
        	//读取全局角色//km modify
        	sqlstr="select id,role_type,role_name from admin_auth_role  order by id";
    		vecData2=db.performQuery(sqlstr,con);
        	for(int j=0;j<vecData2.size();j++){
        		vecRow2=(Vector)vecData2.elementAt(j);
        		roleid=(String)vecRow2.elementAt(0);
        		rolemodel=new RoleModel();
        		rolemodel.setRoleid(roleid);
        		rolemodel.setRoletype((String)vecRow2.elementAt(1));
        		rolemodel.setRolename((String)vecRow2.elementAt(2));  
        		rolemodel.setRoleright("0"); 
        		rolemodel.setOrgid(null);
        		rolemodel.setDepid(null);
        		rolemodel.isBaseFlag=true;
        		oucache.hmRMCache.put(roleid, rolemodel);//全局角色
        	}
        	/*//读取全局群组（岗位）
        	sqlstr="select dutyno,dutyname from s_duty where dutyright='0' order by orderno";
    		vecData2=db.performQuery(sqlstr,con);
        	for(int j=0;j<vecData2.size();j++){
        		vecRow2=(Vector)vecData2.elementAt(j);
        		groupid=(String)vecRow2.elementAt(0);
        		groupmodel=new GroupModel();
        		groupmodel.setGroupid(groupid);
        		groupmodel.setGroupname((String)vecRow2.elementAt(1));
        		groupmodel.setGroupright("0");
        		groupmodel.setOrgid(null);
        		groupmodel.setDepid(null);
        		groupmodel.isBaseFlag=true;
        		oucache.hmGMCache.put(groupid, groupmodel);//全局岗位
        	}*/
        }
        catch(Exception e){
        	WfLog.log(WfLog.ERROR,"【Exception】加载组织机构缓存信息异常，出错信息如下：");
        	e.printStackTrace();
        }
	}

@Override
public List getAllWFClient(String arg0, Connection arg1) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public List getRolesByName(String arg0, String arg1, Connection arg2) {
	// TODO Auto-generated method stub
	return null;
}
}

