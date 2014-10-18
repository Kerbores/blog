package com.kerbores.utils.common;

import org.nutz.lang.Strings;

/**
 * @author Ixion
 * 
 */
public class StringUtils {
	/**
	 * 字符串去除空白字符后从左到右按照一定位数插入字符
	 * 
	 * @param str
	 *            源
	 * @param n
	 *            位数
	 * @param sp
	 *            插入字符
	 * @return
	 */
	public static String change(String str, int n, String sp) {
		String info = "";
		String nstr = sTrim(str);
		for (int i = 0; i < nstr.length(); i++) {
			info += nstr.charAt(i);
			if (i > 0 && (i + 1) % n == 0) {
				info += sp;
			}
		}
		return info;
	}

	/**
	 * 字符串去除空白字符后从右到左按照一定位数插入字符
	 * 
	 * @param str
	 *            源
	 * @param n
	 *            位数
	 * @param sp
	 *            插入字符
	 * @return
	 */
	public static String rChange(String str, int n, String sp) {
		String info = "";
		String nstr = sTrim(str);
		for (int i = nstr.length() - 1; i >= 0; i--) {
			info = nstr.charAt(i) + info;
			if ((nstr.length() - i) % n == 0) {
				info = sp + info;
			}
		}
		return info;
	}
	/**
	 * 去除全部空白
	 * @param in
	 * @return
	 */
	public static String sTrim(String in) {
		return in.replaceAll("\\s*", "");
	}
	
	public static boolean isNullOrEmpty(Object object) {
		return object == null ? true : Strings.isBlank(object.toString());
	}

	public static String getString(Object obj) {
		return isNullOrEmpty(obj) ? "" : obj.toString();
	}

}
