package com.kerbores.utils.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;

import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 
 * @author Ixion
 *
 *         create at 2014年8月27日
 */
public class XmlUtil {
	private static final Log log = Logs.get();

	// 存放对类创建JAXBContext对象
	private static ConcurrentHashMap<String, JAXBContext> jaxbcontextmap = new ConcurrentHashMap<String, JAXBContext>();

	/**
	 * 创建单例JAXBContext对象，并将对象放到ConcurrentHashMap中
	 * 
	 * @param c
	 *            待处理的类
	 * @return
	 * @throws JAXBException
	 */
	public static JAXBContext getJAXBContext(Class<?> c) throws JAXBException {
		JAXBContext context = jaxbcontextmap.get(c.getName());
		if (context != null) {
			return context;
		} else {
			context = JAXBContext.newInstance(c);
			jaxbcontextmap.put(c.getName(), context);
			return context;
		}
	}

	/**
	 * 把xml配置转换成对象
	 * 
	 * @param <T>
	 *            类型泛型
	 * 
	 * @param xml
	 *            xml串信息
	 * @param classObj
	 *            类型
	 * @return T泛型对象实例
	 */
	public static <T> Object unmarshal(String xml, Class<? extends T> classObj) {
		Object obj;
		try {
			JAXBContext jaxbContext = getJAXBContext(classObj);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			obj = unmarshaller.unmarshal(new StringReader(xml));
			return obj;
		} catch (JAXBException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 把对象转换成xml配置
	 * 
	 * @param classObj
	 *            待处理的类类型
	 * @param obj
	 *            待处理的类实例
	 * @return xml串
	 */
	public static String marshal(Class<?> classObj, Object obj) {
		String xmlStr = "";
		try {
			JAXBContext jaxbContext = getJAXBContext(classObj);
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter out = new StringWriter();
			marshaller.marshal(obj, new StreamResult(out));
			xmlStr = out.toString();
		} catch (JAXBException e) {
			log.error(e.getMessage());
		}
		return xmlStr;
	}

	/**
	 * 把对象转换成xml配置
	 * 
	 * @param obj
	 *            待处理的类实例
	 * @return xml串
	 */
	public static String marshal(Object obj) {
		return marshal(obj.getClass(), obj);
	}

}
