package com.kerbores.utils.entries;

import java.util.List;

import org.nutz.lang.util.NutMap;

/**
 * 
 * nutz执行sql之后的返回结果 只处理报表类查询
 * 
 * @author Kerbores
 * 
 */
public class QueryResult {
	/**
	 * 数据
	 * 
	 * 表格是一个row的列表 row 是单元格组成的map 单元格使用 key-value形式表示 why map
	 * map进行json转换的时候很自然的形成key-value形式 便于在view上进行数据的获取和一个dom操作
	 */
	private List<NutMap> data;

	@Override
	public String toString() {
		return "QueryResult [data=" + data + "]";
	}

	public List<NutMap> getData() {
		return data;
	}

	public void setData(List<NutMap> data) {
		this.data = data;
	}

}
