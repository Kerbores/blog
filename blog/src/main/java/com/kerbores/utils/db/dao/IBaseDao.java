package com.kerbores.utils.db.dao;

import java.util.List;

import org.nutz.dao.Condition;

/**
 * 
 * @author Ixion
 *
 *         nutz dao 通用接口
 * @param <T>
 *            create at 2014年8月22日
 */
public interface IBaseDao<T> {
	/**
	 * 持久化对象
	 * 
	 * @param t
	 *            待持久化对象
	 * @return 持久化成功状态标识
	 */
	public T save(T t);

	/**
	 * 更新对象
	 * 
	 * @param t
	 *            待更新对象
	 * @return 成功状态
	 */
	public boolean update(T t);

	/**
	 * 根据Id删除对象
	 * 
	 * @param id
	 *            待删除对象的id
	 * @param c
	 *            待删除对象的类型
	 * @return 删除成功与否的状态标识
	 */
	public boolean delById(int id);

	/**
	 * 根据Id查找对象
	 * 
	 * @param id
	 *            待查找对象的id
	 * @param c
	 *            带查找对象的类型
	 * @return 查询结果对象
	 */
	public T findById(int id);

	/**
	 * 查询全部
	 * 
	 * @param c
	 *            要查询的对象类型
	 * @param orderby
	 *            逆序字段
	 * @return 对象列表
	 */
	public List<T> findAll(String orderby);

	/**
	 * 无排序列表
	 * 
	 * @param c
	 * @return
	 */
	public List<T> findAll();

	/**
	 * 根据条件查询
	 * 
	 * @param c
	 *            查询对象的类型
	 * @param condition
	 *            查询条件
	 * @return 满足条件的对象列表
	 */
	public List<T> search(Condition condition);

	/**
	 * 获取记录数
	 * 
	 * @param c
	 *            对象
	 * @return 对象对应表的记录数，主要用于分页
	 */
	public int countAll();

	/**
	 * 按条件统计数量
	 * 
	 * @param c
	 *            对象
	 * @param condition
	 *            统计条件
	 * @return 满足条件的记录数
	 */
	public int countByCnd(Condition condition);

	/**
	 * 根据条件查询单个对象
	 * 
	 * @param condition
	 *            查询条件
	 * @return 查询结果
	 */
	public T findByCondition(Condition condition);

	/**
	 * 按照条件进行分页查询
	 * 
	 * @param condition
	 *            查询条件
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            页面大小
	 * @return 查询结果列表
	 */
	public List<T> searchByPage(Condition condition, int currentPage, int pageSize);

}
