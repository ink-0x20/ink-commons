package com.inkblogdb.commons.util;

import java.lang.Character.UnicodeBlock;

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

	/**
	 * すべての文字列を連結
	 * @param str 文字列
	 * @return 連結文字列
	 */
	public static final String join(final String... str) {
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
	public static final String[] split(final String str, final String separatorStr) {
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
	public static final String trim(final String str) {
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
	public static final String enclose(final String str, final String enclose) {
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

	/**
	 * 文字列にひらがなが含まれているかどうか判定
	 * @param str 文字列
	 * @return ひらがなが含まれているかどうか
	 */
	public static final boolean containsHiragana(final String str) {
		return containsUnicodeBlocks(str
				, UnicodeBlock.HIRAGANA);
	}

	/**
	 * 文字列にカタカナが含まれているかどうか判定
	 * @param str 文字列
	 * @return カタカナが含まれているかどうか
	 */
	public static final boolean containsKatagana(final String str) {
		return containsUnicodeBlocks(str
				, UnicodeBlock.KATAKANA);
	}

	/**
	 * 文字列に半角カタカナか全角アルファベットが含まれているかどうか判定
	 * @param str 文字列
	 * @return 半角カタカナか全角アルファベットが含まれているかどうか
	 */
	public static final boolean containsHalfWidthAndFullWidthForms(final String str) {
		return containsUnicodeBlocks(str
				, UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
	}

	/**
	 * 文字列に日本語が含まれているかどうか判定
	 * 日本語を扱うにおいて、ひらがなとカタカナがあることを前提で判定する
	 * 漢字があるかどうかは判定材料とはしない
	 * @param str 文字列
	 * @return 日本語が含まれているかどうか
	 */
	public static final boolean containsJapanese(final String str) {
		return containsUnicodeBlocks(str
				, UnicodeBlock.HIRAGANA
				, UnicodeBlock.KATAKANA
				, UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
	}

	/**
	 * 文字列にUnicodeBlockが含まれているかどうか判定
	 * @param str 文字列
	 * @param containsUnicodeBlock UnicodeBlock
	 * @return UnicodeBlockが含まれているかどうか
	 */
	public static final boolean containsUnicodeBlocks(final String str, final UnicodeBlock... containsUnicodeBlock) {
		if (str == null) {
			return false;
		}
		for (char ch : str.toCharArray()) {
			UnicodeBlock unicodeBlock = UnicodeBlock.of(ch);
			for (UnicodeBlock checkUnicodeBlock : containsUnicodeBlock) {
				if (checkUnicodeBlock == null) {
					continue;
				}
				if (checkUnicodeBlock.equals(unicodeBlock)) {
					return true;
				}
			}
		}
		return false;
	}

}
