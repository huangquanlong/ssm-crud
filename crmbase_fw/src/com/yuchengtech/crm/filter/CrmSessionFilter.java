package com.yuchengtech.crm.filter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

import com.yuchengtech.bob.common.LogService;
import com.yuchengtech.bob.core.LookupManager;
import com.yuchengtech.crm.constance.OperateTypeConstant;

/**
 * CRM SESSION超时过滤器，当SESSION超时之后，将由此过滤器返回一个异常编码：600.
 * @author WILLJOE
 */
public class CrmSessionFilter implements Filter {
	//获取需要记录授权和参数维护日志的功能清单（在数据字典维护）
	//字典类别码：AUTHORIZE_OPERATE_LOG
	//字典内容：key:/userManager!save.json    value:维护用户信息
	ConcurrentHashMap<String, String> fmap=LookupManager.getInstance().getOracleValues("AUTHORIZE_OPERATE_LOG");
	public void destroy() {}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		//HttpSession session = request.getSession();
		if(!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)){
			if(request.getMethod().equals("POST")){//仅过滤POST请求
				if(fmap!=null&&fmap.containsKey(request.getServletPath())){//判断该请求是否需要记录日志
					LogService.loginfo.setLoginIp(request.getRemoteAddr());
					LogService.loginfo.setLogTypeId(Long.valueOf(OperateTypeConstant.AUTHORIZE_LOG));
					LogService.loginfo.setAfterValue(request.getServletPath());
					LogService.loginfo.setContent(fmap.get(request.getServletPath()));
					LogService.addLog();
				}
			}
			arg2.doFilter(request, response);
		}
		else
			response.sendError(600,"session out");
	}
	public void init(FilterConfig arg0) throws ServletException {}

}
