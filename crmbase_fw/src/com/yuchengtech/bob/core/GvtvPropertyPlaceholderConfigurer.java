package com.yuchengtech.bob.core;

import java.util.Enumeration;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
/**
 * 
 * @description  spring_jdbc数据库配置项中用户名及密码密文处理类
 * @author  chenlin
 *
 * @date 2014-10-11
 */
public class GvtvPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {
	@Override  
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {  
        Enumeration<?> keys = props.propertyNames();  
        while (keys.hasMoreElements()) {  
            String key = (String)keys.nextElement();  
            String value = props.getProperty(key);  
            if (key.endsWith(".encryption") && null != value) {  
                props.remove(key);
                key = key.substring(0, key.length() - 11);
                try {
					value = AESHelper.decodeStr(value);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
                props.setProperty(key, value);  
            }  
            System.setProperty(key, value);  
        }  
        super.processProperties(beanFactoryToProcess, props);  
    }  
}
