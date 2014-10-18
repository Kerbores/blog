package com.kerbores.utils.xml.node;

import java.util.List;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;

import com.kerbores.utils.xml.IxionXMLParser;


/**
 * XML节点描述
 * 
 * @author Kerbores
 * 
 */
public class XMLNode {

	/**
	 * 节点名
	 */
	private String name;
	/**
	 * 节点值
	 */
	private String value;
	/**
	 * 属性们
	 */
	private NutMap attrs;

	/**
	 * 子节点
	 */
	private List<XMLNode> children;

	/**
	 * 当前节点path
	 */
	private String path;

	private XMLNode root;

	public NutMap getAttrs() {
		return attrs;
	}

	public List<XMLNode> getChildren() {
		return children;
	}

	public double getDoubleValue() {
		return Double.parseDouble(getNodeValue());
	}

	public XMLNode father() {
		String fatherPath = getFatherPath(path);
		if (Strings.equals(fatherPath, "")) {
			return null;
		}
		return IxionXMLParser.getNode(fatherPath, root);
	}

	private String getFatherPath(String path) {
		if (path.indexOf("/") > 0) {
			return path.substring(0, path.lastIndexOf("/"));
		} else {
			return "";
		}
	}

	public int getIntValue() {
		return Integer.parseInt(getNodeValue());
	}

	public String getName() {
		return name;
	}

	public String getNodeValue() {
		if (value == null) {
			return children.get(0).getValue();
		}
		return value;
	}

	public String getPath() {
		return path;
	}

	public XMLNode root() {
		return root;
	}

	public String getValue() {
		return value;
	}

	public boolean isRoot() {
		return this.father() == null;
	}

	public void setAttrs(NutMap attrs) {
		this.attrs = attrs;
	}

	public void setChildren(List<XMLNode> children) {
		this.children = children;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setRoot(XMLNode root) {
		this.root = root;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return Json.toJson(this, JsonFormat.nice());
	}
}
