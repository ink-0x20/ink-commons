package com.inkblogdb.commons.util;

/**
 * @author ink-0x20
 *
 */
public class StringUtils {

	/**
	 * 文字列が空白かどうか判定
	 * @param cs 文字列
	 * @return 空白かどうか
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs != null && (strLen = cs.length()) != 0) {
			for(int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(cs.charAt(i))) {
					return false;
				}
			}
			return true;
		}
		return true;
	}

	/**
	 * 文字列が空白でないかどうか判定
	 * @param cs 文字列
	 * @return 空白でないかどうか
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * デフォルトの文字列を返却
	 * @param str 文字列
	 * @return デフォルト文字列
	 */
	public static String defaultString(final String str) {
		return defaultString(str, "");
	}

	/**
	 * デフォルトの文字列を返却
	 * @param str 文字列
	 * @param defaultStr デフォルト文字列
	 * @return デフォルト文字列
	 */
	public static String defaultString(final String str, final String defaultStr) {
		return str == null ? defaultStr : str;
	}

	/**
	 * すべての文字列を連結
	 * @param str 文字列
	 * @return 連結文字列
	 */
	public static String join(final String... str) {
		if (str == null) {
			return "";
		}
		if (str.length == 0) {
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (String s : str) {
			stringBuilder.append(s);
		}
		return stringBuilder.toString();
	}

	/**
	 * 文字列を分割
	 * @param str 文字列
	 * @param separatorStr 区切り文字列
	 * @return 分割文字列
	 */
	public static String[] split(final String str, final String separatorStr) {
		if (str == null) {
			return null;
		}
		if (separatorStr == null) {
			return str.split("");
		}
		return str.split(separatorStr);
	}

	/**
	 * 文字列をトリム
	 * @param str 文字列
	 * @return トリムした文字列
	 */
	public static String trim(final String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 文字列を囲う
	 * @param str 囲われる文字列
	 * @param enclose 囲う文字列
	 * @return 文字列
	 */
	public static String enclose(final String str, final String enclose) {
		if (str == null) {
			return "";
		}
		if (enclose == null) {
			return str;
		}
		String value = trim(str);
		StringBuilder stringBuilder = new StringBuilder();
		if (!value.startsWith(enclose)) {
			stringBuilder.append(enclose);
		}
		stringBuilder.append(value);
		if (!value.endsWith(enclose)) {
			stringBuilder.append(enclose);
		}
		return stringBuilder.toString();
	}

}
