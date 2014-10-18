package com.kerbores.utils.common;

import java.util.ArrayList;
import java.util.List;

import org.nutz.lang.Lang;

/**
 * 字符串内码编码解码方法
 * 
 * @author Kerbores
 * 
 */
public class CharSequence {
	private List<Integer> value = new ArrayList<Integer>();
	private String stringValue;

	public CharSequence(List<Integer> value) {
		this.value = value;
		this.stringValue = toString();
	}

	private CharSequence(String in) {
		for (char c : in.toCharArray()) {
			value.add((int) c);
		}
		this.stringValue = in;
	}

	@SuppressWarnings("unused")
	private String getStringValue() {
		return stringValue;
	}

	@SuppressWarnings("unused")
	private void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String toString() {
		String target = "";
		for (int i : value) {
			target += (char) i;
		}
		return target;
	}

	private List<Integer> getValue() {
		return value;
	}

	@SuppressWarnings("unused")
	private void setValue(List<Integer> value) {
		this.value = value;
	}

	/**
	 * 编码
	 * 
	 * @param info
	 *            带编码字符串
	 * @return 编码结果串
	 */
	public static List<Integer> encode(String info) {
		return new CharSequence(info).getValue();
	}

	/**
	 * 解码
	 * 
	 * @param data
	 *            数据list
	 * @return 原数据
	 */
	public static String decode(List<Integer> data) {
		return new CharSequence(data).toString();
	}

	/**
	 * 解码
	 * 
	 * @param data
	 *            数据串 用 ',' 分隔
	 * @return 原数据
	 */
	public static String decode(String data) {
		List<Integer> value = parse(data);
		return decode(value);
	}

	/**
	 * 解码
	 * 
	 * @param data
	 *            数据数组
	 * @return 原数据
	 */
	public static String decode(Integer[] data) {
		return decode(Lang.array2list(data));
	}

	private static List<Integer> parse(String data) {
		List<Integer> target = new ArrayList<Integer>();
		String[] infos = data.split(",");
		for (String info : infos) {
			target.add(Integer.parseInt(info.trim()));
		}
		return target;
	}
}
