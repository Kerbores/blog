package com.kerbores.utils.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.Xmls;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.kerbores.utils.xml.node.XMLNode;

/**
 * XML解析 很恐怖很好用,但是建议不要用
 * 
 * @author Kerbores
 * 
 */
public class IxionXMLParser {
	private static final Log log = Logs.get();
	private static XMLNode root;
	private static XMLNode father;

	/**
	 * 解析文件
	 * 
	 * @param in
	 * @return
	 */
	public static XMLNode parse(File in) {
		Document doc = null;
		XMLNode node = new XMLNode();
			try {
				doc = Xmls.xmls().parse(in);
			} catch (SAXException e) {
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error(e.getMessage());
			} catch (ParserConfigurationException e) {
				log.error(e.getMessage());
			}
		// 处理标签名
		node.setName(doc.getDocumentElement().getNodeName());
		node.setPath("");
		// 处理值
		node.setValue(doc.getDocumentElement().getNodeValue());
		// 处理属性
		node.setAttrs(dell(doc.getDocumentElement().getAttributes()));
		node.setRoot(node);
		root = node;
		// 处理子节点
		node.setChildren(dell(doc.getDocumentElement().getChildNodes(), father));
		father = node;
		return node;
	}

	/**
	 * 处理子节点
	 * 
	 * @param childNodes
	 * @param father
	 * @return
	 */
	private static List<XMLNode> dell(NodeList childNodes, XMLNode father) {
		List<XMLNode> nodes = new ArrayList<XMLNode>();
		if (father == null) {
			father = root;
		}
		for (int i = 0; i < childNodes.getLength(); i++) {
			XMLNode node = new XMLNode();
			Node temp = childNodes.item(i);
			node.setName(temp.getNodeName());
			node.setPath(genPath(father, temp.getNodeName()));
			node.setValue(temp.getNodeValue());
			node.setAttrs(dell(temp.getAttributes()));
			node.setChildren(dell(temp.getChildNodes(), node));
			node.setRoot(root);
			nodes.add(node);
		}
		return nodes;
	}

	private static String genPath(XMLNode father, String nodeName) {
		if (father == null) {
			return nodeName;
		}
		if (father.getPath().trim().length() == 0) {
			return nodeName;
		}
		return father.getPath() + "/" + nodeName;
	}

	/**
	 * 处理属性
	 * 
	 * @param attributes
	 * @return
	 */
	private static NutMap dell(NamedNodeMap attributes) {
		NutMap data = new NutMap();
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				data.put(attributes.item(i).getNodeName(), attributes.item(i).getNodeValue());
			}
		}
		return data;
	}

	/**
	 * 查询节点
	 * 
	 * @param path
	 *            相对路径路径
	 * @param node
	 *            开始查找的节点
	 * @return 结果节点
	 */
	public static XMLNode getNode(String path, XMLNode node) {
		if (node == null) {
			return null;
		}
		String[] paths = path.split("/");
		for (String info : paths) {
			node = findSubNode(node, info);
		}
		return node;
	}

	private static XMLNode findSubNode(XMLNode node, String info) {
		for (XMLNode n : node.getChildren()) {
			if (Strings.equals(n.getName(), info)) {
				return n;
			}
		}
		return null;
	}

	/**
	 * 解析XML字符串
	 * 
	 * @param in
	 * @return
	 */
	public XMLNode parse(String in) {
		File temp = new File(System.getProperty("user.dir") + File.separator + System.nanoTime() + ".xml");
		Files.appendWrite(temp, in);
		return parse(temp);
	}

}
