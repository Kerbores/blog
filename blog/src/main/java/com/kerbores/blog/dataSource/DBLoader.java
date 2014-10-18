package com.kerbores.blog.dataSource;

import java.io.File;

import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;

/**
 * sqlite数据源加载器
 * 
 * @author 贵源 <br>
 *         create at 2014年10月18日
 */
@IocBean(name = "dbLoader")
public class DBLoader {

	@Inject
	private PropertiesProxy config;

	public String getJdbcUrl() {
		String path = config.get("path");
		File dbFile = Files.findFile(path);
		if (dbFile == null) {
			throw new RuntimeException(" '" + path + "' 文件不存在!");
		}
		return "jdbc:sqlite://" + Strings.lowerFirst(dbFile.getPath());
	}

}
