package com.kerbores.utils.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;

/**
 * @author Ixion
 *
 *         create at 2014年9月4日
 */
public class ReportCallBack implements SqlCallback {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nutz.dao.sql.SqlCallback#invoke(java.sql.Connection,
	 * java.sql.ResultSet, org.nutz.dao.sql.Sql)
	 */
	@Override
	public Object invoke(Connection paramConnection, ResultSet rs, Sql paramSql) throws SQLException {
		List<Record> records = new ArrayList<Record>();
		while (rs.next()) {
			records.add(Record.create(rs));
		}
		return records;
	}

}
