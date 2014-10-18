package com.kerbores.utils.db;

import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;

import com.kerbores.utils.entries.QueryResult;

/**
 * SQL运行器
 * 
 * @author Kerbores
 * 
 */
public class SqlActuator {

	/**
	 * 执行报表查询,建议使用runReport
	 * 
	 * @param query
	 *            查询语句，注意一定是报表查询类sql语句 不然可能导致异常
	 * @param dao
	 *            nutzdao对象
	 * @return 报表数据封装 直接toString可以查看数据格式
	 */
	@Deprecated
	public static QueryResult runReportSql(String query, Dao dao) {
		// 创建SQL
		Sql sql = Sqls.create(query);
		// 为sql添加一个回调函数用于取回resultset中的数据
		sql.setCallback(new ReportSqlCallback());
		// 执行sql
		dao.execute(sql);
		// 取回结果作为返回
		return (QueryResult) sql.getResult();
	}

	/**
	 * 执行update语句
	 * 
	 * @param query
	 *            sql语句
	 * @param dao
	 *            nutzdao对象
	 * @return 影响的记录条数
	 */
	public static int runUpdate(String query, Dao dao) {
		Sql sql = Sqls.create(query);
		sql.setCallback(new UpdateSqlCallback());
		dao.execute(sql);
		return (Integer) sql.getResult();
	}

	/**
	 * 执行更新
	 * 
	 * @param sql
	 *            sql接口对象
	 * @param dao
	 *            dao接口实例
	 * @return 影响的记录条数
	 */
	public static int runUpdate(Sql sql, Dao dao) {
		sql.setCallback(new UpdateSqlCallback());
		dao.execute(sql);
		return (Integer) sql.getResult();
	}

	/**
	 * 按照记录列表的形式返回数据
	 * 
	 * @param query
	 *            查询语句
	 * @param dao
	 *            dao对象
	 * @return 记录列表
	 */
	public static List<Record> runReport(String query, Dao dao) {
		Sql sql = Sqls.create(query);
		sql.setCallback(new ReportCallBack());
		dao.execute(sql);
		return (List<Record>) sql.getResult();
	}

	/**
	 * 执行报表或者查询类sql<br>
	 * 通过Sqls.creat(""),sql.vars().set(key,val)/sql.params().set(key,value)
	 * 可很方便的创建各种自定义的sql,详情见 <a
	 * href='http://api.kerbores.com/nutz/dao/customized_sql.html'>NUTZ
	 * 自定义SQL</a>
	 * 
	 * @param sql
	 *            sql接口对象
	 * @param dao
	 *            dao接口实例
	 * @return 记录列表
	 */
	public static List<Record> runReport(Sql sql, Dao dao) {
		sql.setCallback(new ReportCallBack());
		dao.execute(sql);
		return (List<Record>) sql.getResult();
	}

	/**
	 * 执行查询单个结构的失去了语句 比如 count 查询对象id等int类型字段
	 * 
	 * @param query
	 *            sql语句
	 * @param dao
	 *            dao接口对象
	 * @return 单结果的int形式
	 */
	public static int runUnq(String query, Dao dao) {
		Sql sql = Sqls.create(query);
		sql.setCallback(new UnqIntSqlCallback());
		dao.execute(sql);
		return (Integer) sql.getResult();
	}

	/**
	 * 执行单列查询
	 * 
	 * @param sql
	 *            sql对象
	 * @param dao
	 *            dao接口实例
	 * @return
	 */
	public static int runUnq(Sql sql, Dao dao) {
		sql.setCallback(new UnqIntSqlCallback());
		dao.execute(sql);
		return (Integer) sql.getResult();
	}
}
