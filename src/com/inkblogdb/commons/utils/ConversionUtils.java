package com.inkblogdb.commons.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author ink-0x20
 */
public class ConversionUtils {

	/**
	 * 文字列をbyte配列に変換
	 * @param str - byte配列に変換する文字列
	 * @return - 文字列をbyte配列に変換した結果
	 */
	public static byte[] stringToBytes(final String str) {
		return stringToBytes(str, StandardCharsets.UTF_8);
	}

	/**
	 * 文字列をbyte配列に変換
	 * @param str - byte配列に変換する文字列
	 * @param charset 文字コード
	 * @return - 文字列をbyte配列に変換した結果
	 */
	public static byte[] stringToBytes(final String str, final String charset) {
		return stringToBytes(str, Charset.forName(charset));
	}

	/**
	 * 文字列をbyte配列に変換
	 * @param str - byte配列に変換する文字列
	 * @param charset 文字コード
	 * @return - 文字列をbyte配列に変換した結果
	 */
	public static byte[] stringToBytes(final String str, final Charset charset) {
		if (str == null) {
			return new byte[0];
		}
		return str.getBytes(charset);
	}

	/**
	 * byte配列を文字列に変換
	 * @param bytes - 文字列に変換するbyte配列
	 * @return - byte配列を文字列に変換した結果
	 */
	public static final String bytesToString(byte[] bytes) {
		return bytesToString(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * byte配列を文字列に変換
	 * @param bytes - 文字列に変換するbyte配列
	 * @param charset 文字コード
	 * @return - byte配列を文字列に変換した結果
	 */
	public static final String bytesToString(final byte[] bytes, final String charset) {
		return bytesToString(bytes, Charset.forName(charset));
	}

	/**
	 * byte配列を文字列に変換
	 * @param bytes - 文字列に変換するbyte配列
	 * @param charset 文字コード
	 * @return - byte配列を文字列に変換した結果
	 */
	public static final String bytesToString(final byte[] bytes, final Charset charset) {
		if (bytes == null) {
			return "";
		}
		return new String(bytes, charset);
	}

}
