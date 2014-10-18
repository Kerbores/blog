package com.kerbores.ext.tag;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * @author Ixion
 *
 *         create at 2014年9月9日
 */
public class IteratorTag extends TagSupport {
	private static final Log log = Logs.get();

	/**
	 * 
	 */
	private static final long serialVersionUID = 742425L;
	/**
	 * 迭代内容名称
	 */
	private String name;
	/**
	 * 迭代值在pageContext中的名称
	 */
	private Object value;
	/**
	 * 待迭代的集合
	 */
	private Collection collection;
	/**
	 * 待迭代集合的迭代器
	 */
	private Iterator it;

	/**
	 * @return the it
	 */
	public Iterator getIt() {
		return it;
	}

	/**
	 * @param it
	 *            the it to set
	 */
	public void setIt(Iterator it) {
		this.it = it;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the collection
	 */
	public Collection getCollection() {
		return collection;
	}

	/**
	 * @param collection
	 *            the collection to set
	 */
	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		// 首先初始化集合和迭代器
		try {
			if (value instanceof String) {
				collection = (Collection) pageContext.findAttribute(value.toString());
			}
			if (value instanceof Collection) {
				collection = (Collection) value;
			}
			it = collection.iterator();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		if (collection == null || collection.size() == 0) {
			return SKIP_BODY;
		}

		if (it.hasNext()) {
			pageContext.getRequest().setAttribute(name, it.next());
		}
		return EVAL_BODY_INCLUDE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doAfterBody()
	 */
	@Override
	public int doAfterBody() throws JspException {
		if (it.hasNext()) {
			pageContext.getRequest().setAttribute(name, it.next());
			return EVAL_BODY_AGAIN;
		} else {
			return EVAL_PAGE;
		}

	}

}
