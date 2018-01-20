/**
 * 
 */
package com.yuchengtech.bob.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author km
 * 
 */
public class DataAuthType {

	public static Map<String,String> paraMap = new HashMap<String,String>();
	static{
		paraMap.put("USER", "@USER_ID");	//当前用户ID
		paraMap.put("CUSER", "@CUSER_ID");	//当前用户辖内所属条线用户ID集合
		paraMap.put("'ORG'", "'@ORG_ID'");	//兼容以前版本的数据权限
		paraMap.put("ORG", "@ORG_ID");		//当前机构ID
		paraMap.put("CORG", "@CORG_ID");	//当前机构辖内机构集合(SQL查询)
		paraMap.put("JCORG_ID", "@JCORG_ID");	//当前机构辖内机构集合(JQL查询)
		paraMap.put("UORG", "@UORG_ID");		//当前机构的直接上级
		paraMap.put("SORG", "@SORG_ID");	//当前机构的直接子机构
		paraMap.put("PORG", "@PORG_ID");	//当前机构的路径（即父祖机构+当前机构）
	}
	
	
	public static String getParamString(AuthUser aUser,String key){
		if (key.equals("USER")) {
			return "'"+aUser.getUserId()+"'";
		}else if (key.equals("CUSER")) {
			return aUser.getCuserSql();
		}else if (key.equals("'ORG'")) {
			return "'"+aUser.getUnitId()+"'";
		}else if (key.equals("ORG")) {
			return "'"+aUser.getUnitId()+"'";
		}else if (key.equals("CORG")) {
			if(null != aUser.getUnitInfo()){
				return "SELECT SYSUNIT.UNITID FROM SYS_UNITS SYSUNIT WHERE SYSUNIT.UNITSEQ LIKE '"+aUser.getUnitInfo().get("UNITSEQ")+"%'";
			}else{
				return "";
			}
		}else if(key.equals("JCORG_ID")){
			if(null != aUser.getUnitInfo()){
				return "SELECT C.unitid FROM SysUnits C WHERE C.unitseq LIKE '"+aUser.getUnitInfo().get("UNITSEQ")+"%'";
			}else{
				return "";
			}
		}else if (key.equals("UORG")) {
			StringBuffer authOrgStr = new StringBuffer("");
			if(aUser.getUpOrgList().size()>0){
	        	for(Object org: aUser.getUpOrgList()){
	        		Map tmpOrg = (Map)org;
	        		if(authOrgStr.length()==0)
	        			authOrgStr.append("'"+tmpOrg.get("UNITID")+"'");
	        		else
	        			authOrgStr.append(",'"+tmpOrg.get("UNITID")+"'");
	        	}
	        	
	        }else{
	        	authOrgStr.append("''");
	        }
			return authOrgStr.toString();
		}else if (key.equals("SORG")) {
			StringBuffer authOrgStr = new StringBuffer("");
			if(aUser.getSubOrgList().size()>0){
	        	for(Object org: aUser.getSubOrgList()){
	        		Map tmpOrg = (Map)org;
	        		if(authOrgStr.length()==0)
	        			authOrgStr.append("'"+tmpOrg.get("UNITID")+"'");
	        		else
	        			authOrgStr.append(",'"+tmpOrg.get("UNITID")+"'");
	        	}
	        	
	        }else{
	        	authOrgStr.append("''");
	        }
			return authOrgStr.toString();
		}else if (key.equals("PORG")) {
			StringBuffer authOrgStr = new StringBuffer("");
			if(aUser.getPathOrgList().size()>0){
	        	for(Object org: aUser.getPathOrgList()){
	        		Map tmpOrg = (Map)org;
	        		String[] pathOrg = tmpOrg.get("UNITSEQ").toString().split(",");
	        		for(int i=0;i< pathOrg.length;i++){
	        			if(authOrgStr.length()==0){
	        				authOrgStr.append("'"+pathOrg[i]+"'");
	        			}
	        			else{
	        				authOrgStr.append(",'"+pathOrg[i]+"'");
	        			}
	        		}
	        	}
	        	
	        }else{
	        	authOrgStr.append("''");
	        }
			return authOrgStr.toString();
		}
		
		
		return "";
	}
	
	/**
	 * 
	 * @param QL
	 * @return	将以下引号里的内容进行替换：
	 * 			“@USER_ID”	当前用户ID			替换为 'A'		;
	 * 			“@CUSER_ID” 当前用户辖内所属条线用户ID集合	替换为'A','B'...
	 * 			“@ORG_ID”	当前机构ID			替换为 'A'		;
	 * 			“@CORG_ID”	当前机构辖内机构集合	替换为 'A','B'...		;
	 * 			“@UORG_ID”	直接上级机构		替换为 'A'		;
	 * 			“@SORG_ID”	直接子机构			替换为 'A'		;
	 * 			“@PORG_ID”	机构树路径集合		替换为 'A','B'...		
	 * @throws Exception
	 */
	public static String processParameter(AuthUser auth,String QL) throws Exception {
		for(String key : paraMap.keySet()){
			QL = QL.replaceAll(paraMap.get(key).toString(), getParamString(auth,key));
		}
		return QL;
	}
}
