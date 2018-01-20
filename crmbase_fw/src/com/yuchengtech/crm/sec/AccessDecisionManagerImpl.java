package com.yuchengtech.crm.sec;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AccessDecisionManagerImpl implements AccessDecisionManager {

	
	public void decide(Authentication authentication, Object arg1,
			Collection<ConfigAttribute> cfa) throws AccessDeniedException,
			InsufficientAuthenticationException {
		if(null == authentication.getAuthorities() && authentication.getAuthorities().size() == 0){
			throw new AccessDeniedException("");
		}
		Collection<GrantedAuthority> gas = authentication.getAuthorities();
		for(GrantedAuthority ga : gas){
			for(ConfigAttribute configAttribute : cfa){
				if(ga.getAuthority().equals(configAttribute.getAttribute()))
					return;
			}
		}
		return;
		
		//throw new AccessDeniedException("");

	}

	public boolean supports(ConfigAttribute arg0) {
		return true;
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}

}
