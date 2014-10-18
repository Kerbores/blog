package com.kerbores.utils.web.pager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * @author Kerbores<br>
 *         PageItems.java Create at 2014年1月6日<br>
 *         分页组件<br>
 *         此组件可以生成基于bootstrap的分页条<br>
 *         字段可用于分页条的自定义<br>
 *         pageSize 定义分页大小<br>
 *         barLength 定义分页条的长度 <br>
 * <br>
 *         构建一个分页条的基本参数为 数据(data) 需要分页的数据的总条目(total) 和 当前数据所属的页码(page)
 */
public class PageItems<T> {
	private static final Log log = Logs.get();
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PageItems [total=" + total + ", page=" + page + ", pageSize=" + pageSize + ", barLength=" + barLength + ", data=" + data + "]";
	}

	/**
	 * 条目总数
	 */
	private int total;
	/**
	 * 当前页码
	 */
	private int page;
	/**
	 * 页面大小，默认显示15条数据
	 */
	private int pageSize = 15;
	/**
	 * 分页条长度 分页条长度默认为10个页面
	 */
	private int barLength = 10;
	/**
	 * 数据内容
	 */
	private List<T> data;

	/**
	 * 基本参数构造器，传入总条目数，当前页面和当前页面数据创建一个分页组件
	 * 
	 * @param total
	 * @param page
	 * @param data
	 */
	public PageItems(int total, int page, List<T> data) {
		super();
		this.total = total;
		this.page = page;
		this.data = data;
	}

	/**
	 * 自定义pageSize的组件
	 * 
	 * @param total
	 * @param page
	 * @param pageSize
	 * @param data
	 */
	public PageItems(int total, int page, int pageSize, List<T> data) {
		super();
		this.total = total;
		this.page = page;
		this.pageSize = pageSize;
		this.data = data;
	}

	/**
	 * 
	 */
	public PageItems() {
		super();
	}

	/**
	 * 自定义页面大小和分页条长度的组件
	 * 
	 * @param total
	 * @param page
	 * @param pageSize
	 * @param barLength
	 * @param data
	 */
	public PageItems(int total, int page, int pageSize, int barLength, List<T> data) {
		super();
		this.total = total;
		this.page = page;
		this.pageSize = pageSize;
		this.barLength = barLength;
		this.data = data;
	}

	/**
	 * 自定义分页条长度的组件
	 * 
	 * @param total
	 * @param page
	 * @param barLength
	 * @param data
	 */
	public PageItems(int total, int page, List<T> data, int barLength) {
		super();
		this.total = total;
		this.page = page;
		this.barLength = barLength;
		this.data = data;
	}

	public int getPages() {
		return total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
	}

	/**
	 * 生成分页条
	 * 
	 * @param url
	 *            分页URL 拼接页码需要带= 如：userList.jsp?user.name=xxx&user.sex=1&page=
	 * @return
	 */
	public String genPagerBar(String url) {
		return genPagerBar(url, "UTF-8");
	}

	public String genPagerBar(String url, String charset) {
		try {
			String temp = url.split("\\?")[1];
			url = url.split("\\?")[0] + "?";
			String[] ps = temp.split("&");
			for (String p : ps) {
				if (!p.trim().endsWith("=")) {
					url += p.split("=")[0] + "=" + URLEncoder.encode(p.split("=")[1], charset) + "&";
				} else {
					url += p;
				}
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		if (total == 0) {
			return "";
		}
		String bar = "";
		// 总页数
		int pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
		if (page <= 0) {
			this.page = 1;// 参数错误显示第一页，页面小于0的时候很可怕不是？！
		}
		if (page > pages) {
			this.page = pages;// 页码大于总页数的时候显示最后一页
		}

		if (pages < barLength) {
			barLength = pages; // 如果总页数小于分页条长度，只显示页数长度的分页条
		}

		bar += "<ul class='pagination pagination-sm fill-inline'>";// bootstrap
																	// pager
																	// start
		bar += "<li " + (page <= 1 ? "class='disabled'" : "") + "><a href='" + url + (page - 1) + "'>&laquo;</a></li>";// 上一页
		// 页码小于分页条的一半的时候，从第一页开始显示到barLength页//page 1 barLength 2
		if (page <= barLength / 2) {
			bar += genPagerBarNode(url, 1, barLength, page);
		}

		// 页码大于页数减去分页长度的一半的时候 显示 pages - barLength到pages页
		else if (page >= pages - barLength / 2) {
			bar += genPagerBarNode(url, pages - barLength == 0 ? 1 : pages - barLength, pages, page);
		}
		// 中间情况 显示 page - barLength/2到page+barLength/2页
		else {
			bar += genPagerBarNode(url, page - barLength / 2, page + barLength / 2, page);
		}
		bar += "<li " + (page == pages ? "class='disabled'" : "") + "><a href='" + url + (page + 1) + "'>&raquo;</a></li>";
		bar += "</ul>";
		return bar;
	}

	/**
	 * 
	 * 生成分页节点
	 * 
	 * @param url
	 *            分页URL
	 * @param start
	 *            开始节点index
	 * @param end
	 *            结束节点index
	 * @param page
	 *            当前页
	 * @return
	 */
	private String genPagerBarNode(String url, int start, int end, int page) {
		String target = "";
		for (int i = start; i <= end; i++) {
			target += "<li " + (page == i ? "class='active'" : "") + "><a href='" + url + i + "'>" + i
					+ (page == i ? "<span class='sr-only'>(current)</span>" : "") + "</a></li>";
		}
		return target;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		if (page <= 0) {
			return 1;
		}
		if (page > getPages()) {
			return getPages();
		}
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the barLength
	 */
	public int getBarLength() {
		return barLength;
	}

	/**
	 * @param barLength
	 *            the barLength to set
	 */
	public void setBarLength(int barLength) {
		this.barLength = barLength;
	}

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

}
