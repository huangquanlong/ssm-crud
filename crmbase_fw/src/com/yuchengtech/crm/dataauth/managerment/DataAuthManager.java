package com.yuchengtech.crm.dataauth.managerment;

import java.util.ArrayList;
import java.util.List;

import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.dataauth.model.AuthSysFilter;
import com.yuchengtech.crm.dataauth.model.AuthSysFilterAuth;
import com.yuchengtech.crm.dataauth.service.FilterLoader;

public class DataAuthManager {
	
	private static DataAuthManager instance;
	
	private FilterLoader filterLoader;
	
	private List<AuthSysFilter> filterList = new ArrayList<AuthSysFilter>();
	
	
	
	public FilterLoader getFilterLoader() {
		return filterLoader;
	}

	public void setFilterLoader(FilterLoader filterLoader) {
		this.filterLoader = filterLoader;
	}

	public static synchronized DataAuthManager getInstance(){
		if (instance != null) {
            return instance;
        } else {
            instance = new DataAuthManager();
        }
        return instance;
	}
	
	public void initialize(){
		filterList = filterLoader.LoadFilters();
	}
	
	/**
	 * 通过用户信息获取该用的所有的数据权限信息
	 * @param ia
	 * @return
	 */
	/**
	 * 通过用户信息获取该用的所有的数据权限信息
	 * @param ia
	 * @return
	 */
	public List<AuthSysFilter> getDataAuthInfo(AuthUser ia){
		//List<AuthSysFilterAuth> filterAuthList = dataAuthInfo.LoadAuthInfo(ia);
		List<AuthSysFilter> tmp = new ArrayList<AuthSysFilter>();
		for(AuthSysFilterAuth filterAuth: ia.getAuthInfos()){
			AuthSysFilter tmpFilter = findFilterById(filterAuth.getFilterId());
			if(null!=tmpFilter){
				tmp.add(tmpFilter);
			}
		}
		return tmp;
	}
	
	private AuthSysFilter findFilterById(String id){
		for(AuthSysFilter tmpFilter : filterList){
			if(tmpFilter.getId().toString().equals(id)){
				return tmpFilter;
			}
		}
		return null;
	}
	
}
