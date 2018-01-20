package com.yuchengtech.bob.core;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupPathListener implements ServletContextListener {
   
    public void contextDestroyed(ServletContextEvent event) {
    	// nothing to do
    }

    public void contextInitialized(ServletContextEvent event) {
    	// This listener must startup before log4j
        StringBuilder builder = new StringBuilder();
        ServletContext context = event.getServletContext();
        builder.append(context.getRealPath("/"));
        System.setProperty("CRM_WEB_ROOT", builder.toString());
        if (!builder.toString().endsWith(File.separator)) {
            builder.append(File.separator);
        }
        builder.append("WEB-INF");
        builder.append(File.separator);
        builder.append("logs");
        builder.append(File.separator);
        System.setProperty("CRM_LOG_PATH", builder.toString());
    }
    
}
