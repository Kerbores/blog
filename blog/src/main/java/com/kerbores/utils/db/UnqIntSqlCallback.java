package com.kerbores.utils.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;

/**
 * @author Kerbores<br>
 *         UnqIntSqlCallback.java Create at 2014年1月18日
 * 
 */
public class UnqIntSqlCallback implements SqlCallback {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nutz.dao.sql.SqlCallback#invoke(java.sql.Connection,
	 * java.sql.ResultSet, org.nutz.dao.sql.Sql)
	 */
	@Override
	public Object invoke(Connection connection, ResultSet rs, Sql sql)
			throws SQLException {
		int i = 0;
		if (rs.next()) {
			i = rs.getInt(1);
		}
		return i;
	}

}
