package com.kerbores.utils.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 此类提供将JAVABEAN实例化对象转换成XML字符串的方法parse(object);
 * 注意：本工具只能获取对象中显式定义的属性,从父类继承的属性暂时无法获得.
 * 所有的属性(包括属性类型的子类的属性)都必须有get方法或者is方法(boolean,Boolean).
 * 如果属性是引用类型变量且未初始化,则获取到的内容为空但是会创建该属性元素. 如果某属性类型无任何可访问属性则创建该属性的自结束标签
 * 
 * @author kipy(http://zkipy.blog.163.com)
 */
public class Bean2DomParser {

	private Log log = Logs.get();
	/**
	 * 日期格式 如果对象中有DATE对象将其转换成字符串输出
	 */
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	/**
	 * 文档对象
	 */
	private Document document;
	/**
	 * 根节点
	 */
	private Element root;
	/***
	 * XML字符集
	 */
	private String encoding = "UTF-8";
	/**
	 * 数字标签前导字符串
	 */
	private String header = "_";

	/**
	 * 获取文档对象
	 * 
	 * @return XML文档
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * 获取XML文件字符集定义
	 * 
	 * @return XML文件字符集定义
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 获取数字标签前导字符串
	 * 
	 * @return
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * 获取根节点对象
	 * 
	 * @return 根节点对象
	 */
	public Element getRoot() {
		return root;
	}

	/**
	 * 获取Date对象返回字符串显示形式，保留此方法便于使用者查看并且修改
	 * 
	 * @return Date对象返回字符串显示形式
	 */
	public SimpleDateFormat getSimpledateformat() {
		return simpleDateFormat;
	}

	/**
	 * 获取时间格式对象
	 * 
	 * @return
	 */
	public SimpleDateFormat getSimpleDateFormat() {
		return simpleDateFormat;
	}

	/**
	 * 插入节点
	 * 
	 * @param parentNode
	 *            父节点
	 * @param obj
	 *            插入节点对象
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void insertElement(Element parentNode, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		insertElement(parentNode, null, obj);
	}

	/**
	 * 插入节点
	 * 
	 * @param root
	 *            根节点
	 * @param nodeName
	 *            节点名
	 * @param obj
	 *            数据对象
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void insertElement(Element root, String nodeName, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		// 如果传入的对象为空，则转换为空字符串。
		obj = (obj == null) ? "" : obj;
		// 父元素节点和传入的对象均不能为空。
		if (root != null) {
			// 创建一个节点
			Element currentNode = null;
			// 获取传入对象的类名。
			String className = obj.getClass().getSimpleName().toLowerCase();
			// 传入的对象为基本数据类型。
			if (isBaseDataType(obj.getClass())) {
				// 如果节点名不为空，添加的元素名为节点名，否则为传入类的类名。
				currentNode = root.addElement((nodeName != null && !nodeName
						.equals("")) ? nodeName : className);
				// 添加子节点的值。
				currentNode.addText((obj instanceof Date) ? simpleDateFormat
						.format(obj) : obj.toString());
			}
			// 传入的对象为集合类型。
			else if (obj instanceof Collection) {
				// 插入集合中的对象到元素。
				insertElementFromCollection(root, obj);
			}
			// 传入的对象为Map类型。
			else if (obj instanceof Map) {
				// 插入Map中的对象到元素。
				insertElementFromMap(root, obj);
			}
			// 其他类类型(自定义类型)。
			else {
				// 插入自定义的对象到元素。
				insertElementFromOtherobj(root, obj);
			}
		}
	}

	/**
	 * 从集合对象中插入节点
	 * 
	 * @param root
	 *            根节点
	 * @param obj
	 *            集合对象
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void insertElementFromCollection(Element root, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		Element currentNode = null;
		// 获取传入对象的全类名(包括包名)。
		String classPath = null;
		// 获取传入对象的类名(不包括包名)。
		String className = null;
		// 转换为集合接口对象。
		Collection<?> collection = (Collection<?>) obj;
		// 集合的子元素。
		Object subObject = null;
		// 集合对象不能为空。
		if (collection != null) {
			// 遍历所有的集合元素。
			for (Iterator<?> iterator = collection.iterator(); iterator
					.hasNext();) {
				// 判断获取的集合的子元素是否为空。
				if ((subObject = iterator.next()) != null) {
					// 判断子元素是否不为一个基本数据类型。
					if (!isBaseDataType(subObject.getClass())) {
						// 获取传入对象的全类名(包括包名)。
						classPath = subObject.getClass().getName();
						// 获取传入对象的类名(不包括包名)。
						className = classPath.substring(classPath
								.lastIndexOf(".") + 1);
						// 添加元素到当前元素。
						currentNode = root.addElement(className);
						// 插入到当前元素。
						insertElement(currentNode, subObject);
					} else {
						// 插入到父元素。
						insertElement(root, subObject);
					}
				}
			}
		}
	}

	/**
	 * 插入MAP对象节点
	 * 
	 * @param root
	 *            根节点
	 * @param obj
	 *            MAP对象
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void insertElementFromMap(Element root, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		// 获取传入对象的全类名(包括包名)。
		String classPath = null;
		// 获取传入对象的类名(不包括包名)。
		String className = null;
		// 表示Map的键。
		Object key = null;
		// 表示Map的值。
		Object value = null;
		// 表示当前元素节点。
		Element currentNode = null;
		// 转换Object为Map类型。
		Map<?, ?> map = (Map<?, ?>) obj;
		// 判断传入的对象是否为空。
		if (map != null) {
			// 遍历所有键集合。
			for (Iterator<?> keyIterator = map.keySet().iterator(); keyIterator
					.hasNext();) {
				// 判断取得的Map中的键是否为空。
				if ((key = keyIterator.next()) != null) {
					// 判断子元素是否不为空。
					if ((value = map.get(key)) != null) {
						// 判断子元素是否不为基本数据类型。
						if (!isBaseDataType(value.getClass())) {
							// 获取传入对象的全类名(包括包名)。
							classPath = value.getClass().getName();
							// 获取传入对象的类名(不包括包名)。
							className = classPath.substring(classPath
									.lastIndexOf(".") + 1);
							// 添加元素到当前元素。
							currentNode = root.addElement(className);
							// 插入到当前元素。
							insertElement(currentNode, value);
						} else {
							// 插入到父元素。
							// 此处需要判断MAP的key是否是以数字开头的,不然XML格式非良好
							insertElement(root,
									key.toString().matches("^[0-9]+?") ? header
											+ key.toString() : key.toString(),
									value);
						}
					}
				}
			}
		}

	}

	/**
	 * 自定义对象插入
	 * 
	 * @param root
	 *            根节点
	 * @param obj
	 *            自定义对象
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void insertElementFromOtherobj(Element root, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		// 表示当前元素节点。
		Element currentNode = null;
		// 表示字段名。
		String fieldName = null;
		// 表示类中的getter方法名。
		String methodName = null;
		// 表示通过getter方法取得的对象。
		Object fieldObject = null;
		// 返回类所定义的字段。
		Field[] fields = obj.getClass().getDeclaredFields();
		// 遍历所有类字段对象。
		for (int i = 0; i < fields.length; i++) {
			// 获取单个字段对象。
			Field field = fields[i];
			// 获取字段名。
			fieldName = field.getName();
			// 获取以getter起始的方法名。
			// System.out.println(field.getType());
			methodName = "get" + Character.toUpperCase(fieldName.charAt(0))
					+ fieldName.substring(1);
			// 如果属性是boolean或者BOOLEAN则创建isxxx方法取值
			// || field.getType().equals(Boolean.class)
			if (field.getType().equals(boolean.class)) {
				methodName = "is" + Character.toUpperCase(fieldName.charAt(0))
						+ fieldName.substring(1);
			}
			try {
				// 获取所有的方法对象。
				Method method = obj.getClass().getMethod(methodName);
				// 判断getter方法是否为public。
				if (Modifier.isPublic(method.getModifiers())) {
					// 判断从getter方法获取的对象是否为空。
					if ((fieldObject = method.invoke(obj)) != null
							&& !isBaseDataType(fieldObject.getClass())) {
						// 用字段名为节点名添加一个节点元素到指定的父节点。
						currentNode = root.addElement(fieldName);
						// 添加节点元素到当前新插入的节点下。
						insertElement(currentNode, fieldName, fieldObject);
					} else {
						// 添加节点元素节点到父节点下。
						insertElement(root, fieldName, fieldObject);
					}
				}
			} catch (NoSuchMethodException nex) {
				nex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 是否基本类型
	 * 
	 * @param cls
	 *            需要判断的类的字节码
	 * @return
	 */
	private boolean isBaseDataType(Class<? extends Object> cls) {
		return (cls.equals(String.class) || cls.equals(Integer.class)
				|| cls.equals(Byte.class) || cls.equals(Long.class)
				|| cls.equals(Double.class) || cls.equals(Float.class)
				|| cls.equals(Character.class) || cls.equals(Short.class)
				|| cls.equals(BigDecimal.class) || cls.equals(BigInteger.class)
				|| cls.equals(Boolean.class) || cls.equals(Date.class) || cls
					.isPrimitive());
	}

	/**
	 * 解析方法
	 * 
	 * @param obj
	 *            需要解析的对象
	 * @return DOM对象根节点的内容字符串
	 */
	public String parse(Object obj) {
		// 获取一个对象工厂
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		// DOM对象工厂生成一个DOM对象
		document = documentFactory.createDocument(encoding);
		// 给对象添加名字 名字为源JAVA对象的类名
		document.addElement(obj.getClass().getSimpleName());
		// 获取根节点 之后的内容都是在根节点上添加
		root = document.getRootElement();
		// 在根节点上插入元素
		try {
			insertElement(root, obj.getClass().getSimpleName().toLowerCase(),
					obj);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		// 将根节点取出以字符串形式返回
		return document.asXML();
	}

	/**
	 * 设置XML文件字符集定义
	 * 
	 * @param encoding
	 *            XML文件字符集定义字符串
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 设置数字标签前导字符串
	 * 
	 * @param header
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * 设置Date对象返回字符串形式
	 * 
	 * @param simpleDateFormat
	 */
	public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
		this.simpleDateFormat = simpleDateFormat;
	}
}