package com.kerbores.ext.tag;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * @author 王贵源 create at 2014年9月20日 下午12:24:16
 */
public class BootstrapPagination extends TagSupport {
	private static final Log log = Logs.get();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 请求的命名空间
	 */
	private String nameSpace;
	/**
	 * 请求的方法（路径）
	 */
	private String method;
	/**
	 * 当前页
	 */
	private int page;
	/**
	 * 分页条最大长度
	 */
	private int maxLength = 10;
	/**
	 * 总页数
	 */
	private int pages;
	/**
	 * 其他请求参数
	 */
	private Map otherParam;
	/**
	 * 大小
	 */
	private String size;

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * 分页参数名
	 */
	private String pageParam = "page";
	/**
	 * 参数的encoding charset
	 */
	private String paramEncoding = "UTF-8";
	/**
	 * 输出流
	 */
	private JspWriter out;

	/**
	 * @return the nameSpace
	 */
	public String getNameSpace() {
		return nameSpace;
	}

	/**
	 * @param nameSpace
	 *            the nameSpace to set
	 */
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
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
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength
	 *            the maxLength to set
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the pages
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}

	/**
	 * @return the otherParam
	 */
	public Map getOtherParam() {
		return otherParam;
	}

	/**
	 * @param otherParam
	 *            the otherParam to set
	 */
	public void setOtherParam(Map otherParam) {
		this.otherParam = otherParam;
	}

	/**
	 * @return the pageParam
	 */
	public String getPageParam() {
		return pageParam;
	}

	/**
	 * @param pageParam
	 *            the pageParam to set
	 */
	public void setPageParam(String pageParam) {
		this.pageParam = pageParam;
	}

	/**
	 * @return the paramEncoding
	 */
	public String getParamEncoding() {
		return paramEncoding;
	}

	/**
	 * @param paramEncoding
	 *            the paramEncoding to set
	 */
	public void setParamEncoding(String paramEncoding) {
		this.paramEncoding = paramEncoding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		return super.doStartTag();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			out.write(genPager());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return SKIP_BODY;
	}

	/**
	 * 生产分页条
	 * 
	 * @return
	 */
	private String genPager() {
		if (pages == 0) {
			return "";
		}

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		String url = basePath + nameSpace + method + "?";
		// 处理路径
		try {
			if (otherParam != null) {
				Iterator it = otherParam.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next().toString();
					String val = otherParam.get(key).toString();
					url += key + "=" + URLEncoder.encode(val, paramEncoding) + "&";
				}
			}
		} catch (UnsupportedEncodingException e) {
			log.warn(e);
			log.error(e.getMessage());
		}

		url += pageParam + "=";

		String bar = "<ul class='pagination " + (size == null ? "" : "pagination-" + size) + "'>";
		bar += "<li " + (page <= 1 ? "class='disabled'" : "") + "><a href='" + url + (page - 1) + "'>&laquo;</a></li>";

		if (pages < maxLength) {
			maxLength = pages;
		}

		// 页码小于分页条的一半的时候，从第一页开始显示到barLength页//page 1 barLength 2
		if (page <= maxLength / 2) {
			bar += genPagerBarNode(url, 1, maxLength, page);
		}
		// 页码大于页数减去分页长度的一半的时候 显示 pages - maxLength到pages页
		else if (page >= pages - maxLength / 2) {
			bar += genPagerBarNode(url, pages - maxLength == 0 ? 1 : pages - maxLength, pages, page);
		}
		// 中间情况 显示 page - maxLength/2到page+maxLength/2页
		else {
			bar += genPagerBarNode(url, page - maxLength / 2, page + maxLength / 2, page);
		}
		// 处理结尾
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
}
