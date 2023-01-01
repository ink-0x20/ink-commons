package com.inkblogdb.commons.utils;

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
	public static final boolean isBlank(final CharSequence cs) {
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
	public static final boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * デフォルトの文字列を返却
	 * @param str 文字列
	 * @return デフォルト文字列
	 */
	public static final String defaultString(final String str) {
		return defaultString(str, "");
	}

	/**
	 * デフォルトの文字列を返却
	 * @param str 文字列
	 * @param defaultStr デフォルト文字列
	 * @return デフォルト文字列
	 */
	public static final String defaultString(final String str, final String defaultStr) {
		return str == null ? defaultStr : str;
	}

}
