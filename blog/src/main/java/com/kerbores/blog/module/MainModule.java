package com.kerbores.blog.module;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.kerbores.blog.module.common.BaseModule;
import com.kerbores.utils.entries.Result;

/**
 * @author 贵源 <br>
 *         create at 2014年10月18日
 */
@Modules(scanPackage = true)
@IocBy(type = ComboIocProvider.class, args = { "*org.nutz.ioc.loader.json.JsonLoader", "ioc.json", "*org.nutz.ioc.loader.annotation.AnnotationIocLoader",
		"com.kerbores" })
@Ok("json")
public class MainModule extends BaseModule {

	@Inject
	private Dao dao;
	

	@At("/main")
	public Result main() {
		System.err.println(dao.meta());
		return Result.success();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kerbores.blog.module.common.BaseModule#_getNameSpace()
	 */
	@Override
	public String _getNameSpace() {
		// TODO Auto-generated method stub
		return "main";
	}

}
