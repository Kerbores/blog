package com.kerbores.blog.module.common;

import java.util.LinkedHashMap;

import javax.servlet.http.Cookie;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.view.JspView;
import org.nutz.mvc.view.UTF8JsonView;

import com.kerbores.utils.entries.OperationState;
import com.kerbores.utils.entries.Result;

/**
 * 
 * 提供controller的数据封装统标准操作
 * 
 * @author Kerbores <br>
 *         每个模块只需要继承此模块，配置@At和@Fail即可
 * 
 * @Fail 建议直接放到统一的处理view进行处理 @Fail("jsp:jsp.exception.exception")
 */
@Encoding(input = "UTF-8", output = "UTF-8")
@IocBean
@Fail("http:500")
@Ok("json")
public abstract class BaseModule {
	protected Result result = Result.me();
	protected LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
	protected OperationState state = OperationState.DEFAULT;
	protected String title = "";
	protected static final Log log = Logs.get();

	// @Inject
	// protected GlobalConst global;// 全局变量的加载，并且已经注入到ioc容器，单例的加载方式有助于减少大量的io操作

	public BaseModule() {
		result.clear();
		data.clear();
	}


	/**
	 * 清除结果
	 */
	public void _clear() {
		result.clear();
		data.clear();
	}

	/**
	 * 转发至jsp
	 * 
	 * @param path
	 *            路径
	 * @param objs
	 *            数据
	 * @return
	 */
	public View _renderJsp(String path, Object... objs) {
		Mvcs.getActionContext().getRequest().setAttribute("objs", objs);// 数据绑定
		return new JspView(path);
	}

	/**
	 * 指定命名空间为页面一级导航高亮提供支持
	 * 
	 * @return
	 */
	public abstract String _getNameSpace();

	/**
	 * json形式响应
	 * 
	 * @param objs
	 *            数据
	 * @return
	 */
	public View _renderJson(Object... objs) {
		UTF8JsonView view = (UTF8JsonView) UTF8JsonView.NICE;
		view.setData(objs);
		return view;
	}

	/**
	 * 获取指定的 cookie
	 * 
	 * @param name
	 *            cookie 名
	 * @return cookie 值,如果没有返回 null
	 */
	protected String _getCookie(String name) {
		Cookie[] cookies = Mvcs.getActionContext().getRequest().getCookies();
		for (Cookie cookie : cookies) {
			if (Strings.equals(cookie.getName(), name)) {
				return cookie.getValue();
			}
		}
		return null;
	}

}