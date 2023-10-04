package com.inkblogdb.commons.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author ink-0x20
 *
 */
public class EscapeUtils {

	/** 一般的なHTMLエスケープ */
	public static final Map<String, String> BASIC_HTML_ESCAPE;

	/**
	 * 初期処理
	 */
	static {
		// 一般的なHTMLエスケープ
		Map<String, String> initialBasicHtmlEscape = new HashMap<>();
		initialBasicHtmlEscape.put("&", "&amp;");
		initialBasicHtmlEscape.put("<", "&lt;");
		initialBasicHtmlEscape.put(">", "&gt;");
		initialBasicHtmlEscape.put("\"", "&quot;");
		initialBasicHtmlEscape.put("'", "&#39;");
		initialBasicHtmlEscape.put(" ", "&nbsp;");
		initialBasicHtmlEscape.put("©", "&copy;");
		initialBasicHtmlEscape.put("®", "&reg;");
		initialBasicHtmlEscape.put("™", "&trade;");
		BASIC_HTML_ESCAPE = Collections.unmodifiableMap(initialBasicHtmlEscape);
	}

	/**
	 * HTMLエスケープ
	 * @param str 文字列
	 * @return HTMLエスケープ後の文字列
	 */
	public static final String htmlEscape(final String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		String escape = str;
		for (Entry<String, String> entry : BASIC_HTML_ESCAPE.entrySet()) {
			escape = escape.replace(entry.getKey(), entry.getValue());
		}
		return escape;
	}

	/**
	 * HTMLエスケープを元に戻す
	 * @param str 文字列
	 * @return HTMLエスケープ前の文字列
	 */
	public static final String htmlUnescape(final String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		String escape = str;
		for (Entry<String, String> entry : BASIC_HTML_ESCAPE.entrySet()) {
			escape = escape.replace(entry.getValue(), entry.getKey());
		}
		return escape;
	}

}
