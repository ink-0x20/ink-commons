package com.inkblogdb.commons.utils;

import java.util.regex.Pattern;

/**
 * @author ink-0x20
 *
 */
public class RegexpUtils {

	/**
	 * 正規表現で文字列を検索
	 * @param str 検索対象文字列
	 * @param regularExpression 正規表現
	 * @return ヒットすればtrue
	 */
	public static final boolean isMatch(final String str, final String regularExpression) {
		String target = str;
		if (StringUtils.isBlank(target)) {
			target = "";
		}
		String regexp = regularExpression;
		if (StringUtils.isBlank(regexp)) {
			regexp = "";
		}
		return Pattern.compile(regexp).matcher(target).find();
	}

	/**
	 * 正規表現で文字列を検索
	 * @param string 検索対象文字列
	 * @param regularExpression 正規表現
	 * @return ヒットしなければtrue
	 */
	public static final boolean isNotMatch(final String string, final String regularExpression) {
		return !isMatch(string, regularExpression);
	}

}
