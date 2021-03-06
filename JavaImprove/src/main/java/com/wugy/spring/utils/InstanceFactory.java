package com.wugy.spring.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.wugy.java.utils.ClassUtil;
import com.wugy.spring.annotation.mvc.impl.DefaultClassScanner;
import com.wugy.spring.annotation.mvc.impl.DefaultHandlerExceptionResolver;
import com.wugy.spring.annotation.mvc.impl.DefaultHandlerInvoker;
import com.wugy.spring.annotation.mvc.impl.DefaultHandlerMapping;
import com.wugy.spring.annotation.mvc.impl.DefaultViewResolver;
import com.wugy.spring.annotation.mvc.service.ClassScanner;
import com.wugy.spring.annotation.mvc.service.HandlerExceptionResolver;
import com.wugy.spring.annotation.mvc.service.HandlerInvoker;
import com.wugy.spring.annotation.mvc.service.HandlerMapping;
import com.wugy.spring.annotation.mvc.service.ViewResolver;
import com.wugy.spring.jdbc.DataAccessor;
import com.wugy.spring.jdbc.DataSourceFactory;
import com.wugy.spring.jdbc.DefaultDataAccessor;
import com.wugy.spring.jdbc.DefaultDataSourceFactory;

/**
 * 实例工厂
 *
 * @author devotion 2016年9月2日14:21:14
 */
public class InstanceFactory {

	/**
	 * 用于缓存对应的实例
	 */
	private static final Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

	/**
	 * ClassScanner
	 */
	private static final String CLASS_SCANNER = "smart.framework.custom.class_scanner";

	/**
	 * DataSourceFactory
	 */
	private static final String DS_FACTORY = "smart.framework.custom.ds_factory";

	/**
	 * DataAccessor
	 */
	private static final String DATA_ACCESSOR = "smart.framework.custom.data_accessor";

	/**
	 * HandlerMapping
	 */
	private static final String HANDLER_MAPPING = "smart.framework.custom.handler_mapping";

	/**
	 * HandlerInvoker
	 */
	private static final String HANDLER_INVOKER = "smart.framework.custom.handler_invoker";

	/**
	 * HandlerExceptionResolver
	 */
	private static final String HANDLER_EXCEPTION_RESOLVER = "smart.framework.custom.handler_exception_resolver";

	/**
	 * ViewResolver
	 */
	private static final String VIEW_RESOLVER = "smart.framework.custom.view_resolver";

	/**
	 * 获取 ClassScanner
	 */
	public static ClassScanner getClassScanner() {
		return getInstance(CLASS_SCANNER, DefaultClassScanner.class);
	}

	/**
	 * 获取 DataSourceFactory
	 */
	public static DataSourceFactory getDataSourceFactory() {
		return getInstance(DS_FACTORY, DefaultDataSourceFactory.class);
	}

	/**
	 * 获取 DataAccessor
	 */
	public static DataAccessor getDataAccessor() {
		return getInstance(DATA_ACCESSOR, DefaultDataAccessor.class);
	}

	/**
	 * 获取 HandlerMapping
	 */
	public static HandlerMapping getHandlerMapping() {
		return getInstance(HANDLER_MAPPING, DefaultHandlerMapping.class);
	}

	/**
	 * 获取 HandlerInvoker
	 */
	public static HandlerInvoker getHandlerInvoker() {
		return getInstance(HANDLER_INVOKER, DefaultHandlerInvoker.class);
	}

	/**
	 * 获取 HandlerExceptionResolver
	 */
	public static HandlerExceptionResolver getHandlerExceptionResolver() {
		return getInstance(HANDLER_EXCEPTION_RESOLVER, DefaultHandlerExceptionResolver.class);
	}

	/**
	 * 获取 ViewResolver
	 */
	public static ViewResolver getViewResolver() {
		return getInstance(VIEW_RESOLVER, DefaultViewResolver.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String cacheKey, Class<T> defaultImplClass) {
		// 若缓存中存在对应的实例，则返回该实例
		if (cache.containsKey(cacheKey)) {
			return (T) cache.get(cacheKey);
		}
		// 从配置文件中获取相应的接口实现类配置
		String implClassName = ConfigHelper.getString(cacheKey);
		// 若实现类配置不存在，则使用默认实现类
		if (StringUtils.isEmpty(implClassName)) {
			implClassName = defaultImplClass.getName();
		}
		// 通过反射创建该实现类对应的实例
		T instance = ClassUtil.newInstance(implClassName);
		// 若该实例不为空，则将其放入缓存
		if (instance != null) {
			cache.put(cacheKey, instance);
		}
		// 返回该实例
		return instance;
	}
}
