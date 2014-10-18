package com.kerbores.ext.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * @author 王贵源 create at 2014年9月20日 下午12:25:23
 */
public class ContextPrinter extends TagSupport {
	private static final Log log = Logs.get();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		Object obj = pageContext.findAttribute(value);
		if (obj == null) {
			obj = pageContext.getRequest().getAttribute(value);
		}
		if (obj == null) {
			((PageContext) pageContext.getRequest()).getServletContext().getAttribute(value);
		}
		if (obj == null) {
			pageContext.getSession().getAttribute(value);
		}
		if (obj == null) {
			Logs.get().warn(
					"'" + value + "' not found ! then print itself as result");
			obj = value;
		}
		try {
			out.write(obj.toString());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return SKIP_BODY;
	}

}
