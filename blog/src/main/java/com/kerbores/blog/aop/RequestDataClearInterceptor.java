package com.kerbores.blog.aop;

import java.lang.reflect.Method;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

/**
 * @author 王贵源 create at 2014年9月20日 下午5:10:12<br>
 *         清空单例模块上次请求中的数据，保证每次请求拿到的都是本次的数据
 * 
 */
@IocBean(name = "dataclear")
public class RequestDataClearInterceptor implements MethodInterceptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nutz.aop.MethodInterceptor#filter(org.nutz.aop.InterceptorChain)
	 */
	@Override
	public void filter(InterceptorChain chain) throws Throwable {
		for (Method method : chain.getCallingObj().getClass().getMethods()) {
			if (Strings.equals("_getNameSpace", method.getName())) {
				Mvcs.getActionContext().getRequest().setAttribute("nameSpace", method.invoke(chain.getCallingObj(), new Object[] {}));
			}
			if (Strings.equals("_clear", method.getName())) {
				method.invoke(chain.getCallingObj(), new Object[] {});
			}
		}
		chain.doChain();
	}
}