package com.yuchengtech.crm.sec.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.util.Assert;

import com.yuchengtech.bob.core.UserOnlineManager;

public class SecSessionRegistryImpl extends SessionRegistryImpl{

	private static Logger log = Logger.getLogger(SecSessionRegistryImpl.class);
	
    public SecSessionRegistryImpl()
    {
    }

    public List getAllPrincipals()
    {
        return Arrays.asList(principals.keySet().toArray());
    }

    public List getAllSessions(Object principal, boolean includeExpiredSessions)
    {
        Set sessionsUsedByPrincipal = (Set)principals.get(principal);
        if(sessionsUsedByPrincipal == null)
            return Collections.emptyList();
        List list = new ArrayList(sessionsUsedByPrincipal.size());
        synchronized(sessionsUsedByPrincipal)
        {
            Iterator itr = sessionsUsedByPrincipal.iterator();
            do
            {
                if(!itr.hasNext())
                    break;
                String sessionId = (String)itr.next();
                SessionInformation sessionInformation = getSessionInformation(sessionId);
                if(sessionInformation != null && (includeExpiredSessions || !sessionInformation.isExpired()))
                    list.add(sessionInformation);
            } while(true);
        }
        return list;
    }

    public SessionInformation getSessionInformation(String sessionId)
    {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        return (SessionInformation)sessionIds.get(sessionId);
    }

    public void onApplicationEvent(SessionDestroyedEvent event)
    {
        String sessionId = event.getId();
        removeSessionInformation(sessionId);
    }

    public void refreshLastRequest(String sessionId)
    {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        SessionInformation info = getSessionInformation(sessionId);
        if(info != null)
            info.refreshLastRequest();
    }

    public synchronized void registerNewSession(String sessionId, Object principal)
    {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        Assert.notNull(principal, "Principal required as per interface contract");
        if(logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("Registering session ").append(sessionId).append(", for principal ").append(principal).toString());
        if(getSessionInformation(sessionId) != null)
            removeSessionInformation(sessionId);
        sessionIds.put(sessionId, new SessionInformation(principal, sessionId, new Date()));
        Set sessionsUsedByPrincipal = (Set)principals.get(principal);
        if(sessionsUsedByPrincipal == null)
        {
            sessionsUsedByPrincipal = Collections.synchronizedSet(new HashSet(4));
            principals.put(principal, sessionsUsedByPrincipal);
        }
        sessionsUsedByPrincipal.add(sessionId);
        if(logger.isTraceEnabled())
            logger.trace((new StringBuilder()).append("Sessions used by '").append(principal).append("' : ").append(sessionsUsedByPrincipal).toString());
        refreshCache (true, principal);
    }

    public void removeSessionInformation(String sessionId)
    {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        SessionInformation info = getSessionInformation(sessionId);
        if(info == null)
            return;
        if(logger.isTraceEnabled())
            logger.debug((new StringBuilder()).append("Removing session ").append(sessionId).append(" from set of registered sessions").toString());
        sessionIds.remove(sessionId);
        Set sessionsUsedByPrincipal = (Set)principals.get(info.getPrincipal());
        if(sessionsUsedByPrincipal == null)
            return;
        if(logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("Removing session ").append(sessionId).append(" from principal's set of registered sessions").toString());
        synchronized(sessionsUsedByPrincipal)
        {
            sessionsUsedByPrincipal.remove(sessionId);
            if(sessionsUsedByPrincipal.size() == 0)
            {
                if(logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("Removing principal ").append(info.getPrincipal()).append(" from registry").toString());
                principals.remove(info.getPrincipal());
                refreshCache (false, info.getPrincipal());
            }
        }
        if(logger.isTraceEnabled())
            logger.trace((new StringBuilder()).append("Sessions used by '").append(info.getPrincipal()).append("' : ").append(sessionsUsedByPrincipal).toString());
    }

    

    protected final Log logger = LogFactory.getLog(SecSessionRegistryImpl.class);
    private final Map principals = Collections.synchronizedMap(new HashMap());
    private final Map sessionIds = Collections.synchronizedMap(new HashMap());
    /***
     * 在线用户信息刷新
     * @param flag 增加 or 移除
     * @param authUser 用户信息
     */
    private void refreshCache (boolean flag, Object authUser) {
    	UserOnlineManager.getInstance().refreshCache(flag, authUser);
    }

}
