
package com.migu.util.tools;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author luchenxi
 *
 *         2018年1月29日
 */
public class SpringContextHolder implements ApplicationContextAware
{
	private static ApplicationContext applicationContext;

	@ Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		SpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@ SuppressWarnings ("unchecked")
	public static <T>T getBean(String name)
	{
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@ SuppressWarnings ("unchecked")
	public static <T>T getBean(Class<T> clazz)
	{
		checkApplicationContext();
		return (T) applicationContext.getBeansOfType(clazz);
	}

	/**
	 * 清除applicationContext静态变量.
	 */
	public static void cleanApplicationContext()
	{
		applicationContext = null;
	}

	private static void checkApplicationContext()
	{
		if(applicationContext == null)
		{
			throw new IllegalStateException(
				"applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}
}
