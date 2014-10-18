package com.kerbores.utils.db;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.lang.util.NutMap;

import com.kerbores.utils.entries.QueryResult;

/**
 * @author Kerbores<br>
 *         ReportSqlCallback.java Create at 2014年1月11日<br>
 *         报表查询sql回调实现
 * 
 */
public class ReportSqlCallback implements SqlCallback {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nutz.dao.sql.SqlCallback#invoke(java.sql.Connection,
	 * java.sql.ResultSet, org.nutz.dao.sql.Sql)
	 */
	@Override
	public Object invoke(Connection connection, ResultSet rs, Sql sql) throws SQLException {
		QueryResult result = new QueryResult();
		List<NutMap> data = new ArrayList<NutMap>();
		ResultSetMetaData mate = rs.getMetaData();
		int columns = mate.getColumnCount();
		// 取行数据
		while (rs.next()) {
			// 单行处理
			NutMap row = new NutMap();
			for (int i = 1; i <= columns; i++) {
				Object obj = rs.getObject(i);
				if (obj instanceof Clob) {
					Clob clob = (Clob) obj;
					obj = clob.getSubString(0, (int) clob.length());// TODO
																	// Clob强制处理成String
																	// 这个明显是不合理的,但是也是无可厚非的
				}
				row.put(mate.getColumnLabel(i), obj);
			}
			// 添加行
			data.add(row);
		}
		// 封装到result
		result.setData(data);
		return result;
	}
}
