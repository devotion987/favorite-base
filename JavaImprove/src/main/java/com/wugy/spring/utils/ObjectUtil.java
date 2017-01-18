package com.wugy.spring.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.MapUtils;

/**
 * 对象操作工具类
 *
 * @author devotion
 */
public class ObjectUtil {

	/**
	 * 设置成员变量
	 */
	public static void setField(Object obj, String fieldName, Object fieldValue) {
		try {
			if (PropertyUtils.isWriteable(obj, fieldName)) {
				PropertyUtils.setProperty(obj, fieldName, fieldValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取成员变量
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object propertyValue = null;
		try {
			if (PropertyUtils.isReadable(obj, fieldName)) {
				propertyValue = PropertyUtils.getProperty(obj, fieldName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return propertyValue;
	}

	/**
	 * 复制所有成员变量
	 */
	public static void copyFields(Object source, Object target) {
		try {
			for (Field field : source.getClass().getDeclaredFields()) {
				// 若不为 static 成员变量，则进行复制操作
				if (!Modifier.isStatic(field.getModifiers())) {
					field.setAccessible(true); // 可操作私有成员变量
					field.set(target, field.get(source));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取对象的字段映射（字段名 => 字段值），忽略 static 字段
	 */
	public static Map<String, Object> getFieldMap(Object obj) {
		return getFieldMap(obj, true);
	}

	/**
	 * 获取对象的字段映射（字段名 => 字段值）
	 */
	public static Map<String, Object> getFieldMap(Object obj, boolean isStaticIgnored) {
		Map<String, Object> fieldMap = new LinkedHashMap<String, Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (isStaticIgnored && Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			String fieldName = field.getName();
			Object fieldValue = ObjectUtil.getFieldValue(obj, fieldName);
			fieldMap.put(fieldName, fieldValue);
		}
		return fieldMap;
	}

	/**
	 * 转置 Map
	 */
	public static <K, V> Map<V, K> invert(Map<K, V> source) {
		Map<V, K> target = null;
		if (MapUtils.isNotEmpty(source)) {
			target = new LinkedHashMap<V, K>(source.size());
			for (Map.Entry<K, V> entry : source.entrySet()) {
				target.put(entry.getValue(), entry.getKey());
			}
		}
		return target;
	}
}
