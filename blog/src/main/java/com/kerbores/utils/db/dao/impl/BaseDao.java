package com.kerbores.utils.db.dao.impl;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.kerbores.utils.db.dao.IBaseDao;

/**
 * 
 * @author Ixion
 *
 *         nutz dao 实现模板
 * @param <T>
 *            create at 2014年8月22日
 */
@IocBean
public abstract class BaseDao<T> implements IBaseDao<T> {

	@Inject
	protected Dao dao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#save(java.lang.Object)
	 */
	@Override
	public T save(T t) {
		return dao.insert(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#update(java.lang.Object)
	 */
	@Override
	public boolean update(T t) {
		return dao.update(t) == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#delById(int, java.lang.Class)
	 */
	@Override
	public boolean delById(int id) {
		return dao.delete(initClass(), id) == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#findById(int, java.lang.Class)
	 */
	@Override
	public T findById(int id) {
		return dao.fetch(initClass(), id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#findAll(java.lang.Class, java.lang.String)
	 */
	@Override
	public List<T> findAll(String orderby) {
		return dao.query(initClass(), Cnd.orderBy().desc(orderby), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#search(java.lang.Class,
	 * org.nutz.dao.Condition)
	 */
	@Override
	public List<T> search(Condition condition) {
		return dao.query(initClass(), condition, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#countAll(java.lang.Class)
	 */
	@Override
	public int countAll() {
		return dao.count(initClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#countByCnd(java.lang.Class,
	 * org.nutz.dao.Condition)
	 */
	@Override
	public int countByCnd(Condition condition) {
		return dao.count(initClass(), condition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#findByCondition(java.lang.Class,
	 * org.nutz.dao.Condition)
	 */
	@Override
	public T findByCondition(Condition condition) {
		return dao.fetch(initClass(), condition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#findAll(java.lang.Class)
	 */
	@Override
	public List<T> findAll() {

		return dao.query(initClass(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iofd.ldb.dao.IBaseDao#searchByPage(java.lang.Class,
	 * org.nutz.dao.Condition, int, int)
	 */
	@Override
	public List<T> searchByPage(Condition condition, int currentPage, int pageSize) {
		Pager pager = dao.createPager(currentPage, pageSize);
		return dao.query(initClass(), condition, pager);
	}

	/**
	 * 获取当前接口操作的泛型字节码描述
	 * 
	 * @return 要操作的类型的字节码,使Dao接口能获取字节码与数据库之间的映射关系
	 */
	public abstract Class<T> initClass();

}
