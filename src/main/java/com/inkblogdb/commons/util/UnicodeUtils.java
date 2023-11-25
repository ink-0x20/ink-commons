package com.inkblogdb.commons.util;

import java.lang.Character.UnicodeBlock;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author ink-0x20
 *
 */
public class UnicodeUtils {

	/**
	 * 文字列にひらがなが含まれているかどうか判定
	 * @param str 文字列
	 * @return ひらがなが含まれているかどうか
	 */
	public static boolean containsHiragana(final String str) {
		return containsUnicodeBlocks(str
				, UnicodeBlock.HIRAGANA);
	}

	/**
	 * 文字列にカタカナが含まれているかどうか判定
	 * @param str 文字列
	 * @return カタカナが含まれているかどうか
	 */
	public static boolean containsKatagana(final String str) {
		return containsUnicodeBlocks(str
				, UnicodeBlock.KATAKANA);
	}

	/**
	 * 文字列に日本語が含まれているかどうか判定
	 * 判定基準はひらがな or カタカナ or 常用漢字を1文字以上含んでいるか
	 * ※常用漢字（2136文字）2010年11月30日
	 * @param str 文字列
	 * @return 日本語が含まれているかどうか
	 */
	public static boolean containsJapanese(final String str) {
		return containsUnicodeBlocks(str
				, UnicodeBlock.HIRAGANA
				, UnicodeBlock.KATAKANA
				, UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
	}

	/**
	 * 文字列にUnicodeBlockが含まれているかどうか判定
	 * @param str 文字列
	 * @param containsUnicodeBlock UnicodeBlock
	 * @return 指定したUnicodeBlockが含まれているかどうか
	 */
	public static boolean containsUnicodeBlocks(final String str, final UnicodeBlock... containsUnicodeBlock) {
		if (str == null) {
			return false;
		}
		String normalize = Normalizer.normalize(str, Form.NFKC);
		return normalize.codePoints().anyMatch(x ->
			Arrays.stream(containsUnicodeBlock)
				.filter(Objects::nonNull)
				.anyMatch(y -> y.equals(UnicodeBlock.of(x)))
		);
	}

}
