package com.inkblogdb.commons.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author ink-0x20
 */
public class ConvertUtils {

	/**
	 * 文字列をbyte配列に変換
	 * @param str byte配列に変換する文字列
	 * @return 文字列をbyte配列に変換した結果
	 */
	public static byte[] stringToBytes(final String str) {
		return stringToBytes(str, StandardCharsets.UTF_8);
	}

	/**
	 * 文字列をbyte配列に変換
	 * @param str byte配列に変換する文字列
	 * @param charset 文字コード
	 * @return 文字列をbyte配列に変換した結果
	 */
	public static byte[] stringToBytes(final String str, final String charset) {
		return stringToBytes(str, Charset.forName(charset));
	}

	/**
	 * 文字列をbyte配列に変換
	 * @param str byte配列に変換する文字列
	 * @param charset 文字コード
	 * @return 文字列をbyte配列に変換した結果
	 */
	public static byte[] stringToBytes(final String str, final Charset charset) {
		if (StringUtils.isBlank(str)) {
			return new byte[0];
		}
		return str.getBytes(charset);
	}

	/**
	 * byte配列を文字列に変換
	 * @param bytes 文字列に変換するbyte配列
	 * @return byte配列を文字列に変換した結果
	 */
	public static String bytesToString(byte[] bytes) {
		return bytesToString(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * byte配列を文字列に変換
	 * @param bytes 文字列に変換するbyte配列
	 * @param charset 文字コード
	 * @return byte配列を文字列に変換した結果
	 */
	public static String bytesToString(final byte[] bytes, final String charset) {
		return bytesToString(bytes, Charset.forName(charset));
	}

	/**
	 * byte配列を文字列に変換
	 * @param bytes 文字列に変換するbyte配列
	 * @param charset 文字コード
	 * @return byte配列を文字列に変換した結果
	 */
	public static String bytesToString(final byte[] bytes, final Charset charset) {
		if (bytes == null) {
			return "";
		}
		return new String(bytes, charset);
	}

	/**
	 * シンプルな文字列のMapをJson形式の文字列に変換
	 * ListやMapには未対応
	 * @param map Jsonに変換するMap
	 * @return Json文字列
	 */
	public static String mapTpJson(final Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder("{");
		for (Entry<String, Object> entry : map.entrySet()) {
			stringBuilder.append("\"");
			stringBuilder.append(entry.getKey());
			stringBuilder.append("\":");
			if (entry.getValue() instanceof String) {
				stringBuilder.append("\"");
				stringBuilder.append(String.valueOf(entry.getValue()));
				stringBuilder.append("\"");
			} else if (entry.getValue() instanceof Boolean) {
				stringBuilder.append(String.valueOf(entry.getValue()));
			} else if (entry.getValue() instanceof Integer) {
				stringBuilder.append(String.valueOf(entry.getValue()));
			}
			stringBuilder.append(",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	/**
	 * 文字列の改行コードをCrに統一
	 * @param str 文字列
	 * @return 改行コードをCrに置換した文字列
	 */
	public static String replaceToCr(final String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		return str.replaceAll("\r\n|\r|\n", "\r");
	}

	/**
	 * 文字列の改行コードをLfに統一
	 * @param str 文字列
	 * @return 改行コードをLfに置換した文字列
	 */
	public static String replaceToLf(final String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		return str.replaceAll("\r\n|\r|\n", "\n");
	}

	/**
	 * 文字列の改行コードをCrLfに統一
	 * @param str 文字列
	 * @return 改行コードをCrLfに置換した文字列
	 */
	public static String replaceToCrLf(final String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		return str.replaceAll("\r\n|\r|\n", "\r\n");
	}

}
