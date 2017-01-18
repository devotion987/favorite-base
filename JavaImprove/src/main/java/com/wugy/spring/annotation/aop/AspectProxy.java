package com.wugy.spring.annotation.aop;

import java.lang.reflect.Method;

import com.wugy.spring.annotation.aop.proxy.Proxy;
import com.wugy.spring.annotation.aop.proxy.ProxyChain;

/**
 * 切面代理
 *
 * @author devotion
 */
public abstract class AspectProxy implements Proxy {

	@Override
	public final Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result = null;

		Class<?> cls = proxyChain.getTargetClass();
		Method method = proxyChain.getTargetMethod();
		Object[] params = proxyChain.getMethodParams();

		begin();
		try {
			if (intercept(cls, method, params)) {
				before(cls, method, params);
				result = proxyChain.doProxyChain();
				after(cls, method, params, result);
			} else {
				result = proxyChain.doProxyChain();
			}
		} catch (Exception e) {
			e.printStackTrace();
			error(cls, method, params, e);
			throw e;
		} finally {
			end();
		}

		return result;
	}

	public void begin() {
	}

	public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
		return true;
	}

	public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
	}

	public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
	}

	public void error(Class<?> cls, Method method, Object[] params, Throwable e) {
	}

	public void end() {
	}
}
