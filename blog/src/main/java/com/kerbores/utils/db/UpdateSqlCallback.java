package com.kerbores.utils.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;

/**
 * @author Kerbores<br>
 *         UpdateSqlCallback.java Create at 2014年1月11日<br>
 *         更新语句回调实现
 * 
 */
public class UpdateSqlCallback implements SqlCallback {

	/**
	 * 返回更新语句影响的记录条数
	 */
	@Override
	public Object invoke(Connection connection, ResultSet rs, Sql sql)
			throws SQLException {
		return sql.getUpdateCount();
	}

}
