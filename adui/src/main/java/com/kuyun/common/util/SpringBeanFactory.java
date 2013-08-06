/**
 * tenfen.com Inc.
 * Copyright (c) 2012-2015 All Rights Reserved.
 */
package com.kuyun.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 * Spring Bean Factory
 * 
 * <pre>
 * 
 * </pre>
 * 
 * @author zzj
 * @version $Id: BeanFactory.java, v 0.1 2012-10-30 下午05:13:01 zhangzhijian Exp
 *          $
 */
public class SpringBeanFactory {
	
	
	@SuppressWarnings("unused")
	private static final ApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(
			new String[] { "classpath:spring/applicationContext*.xml" });

	/**
	 * 
	 * 功能：获取Bean对象
	 * 
	 * @author: zhangzhijian
	 * @Date: 2012-6-11
	 * 
	 * @param beanId
	 * @return
	 */
	public static Object getBean(String beanId) {
		return SpringContextUtil.getBean(beanId);
	}
	
	public static void main(String[] args) {
    }

}
