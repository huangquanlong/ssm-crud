package com.yuchengtech.crm.sec;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class SecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource{
	
	public SecurityMetadataSourceImpl(){}
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
		return configAttributes;
	}

	public Collection<ConfigAttribute> getAttributes(Object url)
			throws IllegalArgumentException {
		Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();

		return configAttributes;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

}
