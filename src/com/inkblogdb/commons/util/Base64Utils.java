package com.inkblogdb.commons.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author ink-0x20
 */
public class Base64Utils {

	/**
	 * Base64エンコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64エンコードされたデータ
	 */
	public static byte[] base64Encode(final String str) {
		return base64Encode(ConvertUtils.stringToBytes(str, StandardCharsets.UTF_8));
	}

	/**
	 * Base64エンコード
	 * @param bytes 対象
	 * @return Base64エンコードされたデータ
	 */
	public static String base64EncodeToString(final byte[] bytes) {
		return base64EncodeToString(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * Base64エンコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64エンコードされたデータ
	 */
	public static String base64EncodeToString(final String str) {
		return base64EncodeToString(ConvertUtils.stringToBytes(str, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
	}

	/**
	 * Base64エンコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64エンコードされたデータ
	 */
	public static byte[] base64Encode(final String str, final String charset) {
		return base64Encode(ConvertUtils.stringToBytes(str, Charset.forName(charset)));
	}

	/**
	 * Base64エンコード
	 * @param bytes 対象
	 * @param charset 文字コード
	 * @return Base64エンコードされたデータ
	 */
	public static String base64EncodeToString(final byte[] bytes, final String charset) {
		return base64EncodeToString(bytes, Charset.forName(charset));
	}

	/**
	 * Base64エンコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64エンコードされたデータ
	 */
	public static String base64EncodeToString(final String str, final String charset) {
		return base64EncodeToString(ConvertUtils.stringToBytes(str, Charset.forName(charset)), Charset.forName(charset));
	}

	/**
	 * Base64エンコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64エンコードされたデータ
	 */
	public static byte[] base64Encode(final String str, final Charset charset) {
		byte[] base64 = ConvertUtils.stringToBytes(str, charset);
		return base64Encode(base64);
	}

	/**
	 * Base64エンコード
	 * @param bytes 対象
	 * @param charset 文字コード
	 * @return Base64エンコードされたデータ
	 */
	public static String base64EncodeToString(final byte[] bytes, final Charset charset) {
		byte[] base64 = base64Encode(bytes);
		return ConvertUtils.bytesToString(base64, charset);
	}

	/**
	 * Base64エンコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64エンコードされたデータ
	 */
	public static String base64EncodeToString(final String str, final Charset charset) {
		byte[] base64 = ConvertUtils.stringToBytes(str, charset);
		base64 = base64Encode(base64);
		return ConvertUtils.bytesToString(base64, charset);
	}

	/**
	 * Base64エンコード
	 * @param bytes 対象
	 * @return Base64エンコードされたデータ
	 */
	public static byte[] base64Encode(final byte[] bytes) {
		if (bytes == null) {
			return new byte[0];
		}
		return Base64.getEncoder().encode(bytes);
	}

	/**
	 * Base64デコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64デコードされたデータ
	 */
	public static byte[] base64Decode(final String str) {
		return base64Decode(ConvertUtils.stringToBytes(str, StandardCharsets.UTF_8));
	}

	/**
	 * Base64デコード
	 * @param bytes 対象
	 * @return Base64デコードされたデータ
	 */
	public static String base64DecodeToString(final byte[] bytes) {
		return base64DecodeToString(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * Base64デコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64デコードされたデータ
	 */
	public static String base64DecodeToString(final String str) {
		return base64DecodeToString(ConvertUtils.stringToBytes(str, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
	}

	/**
	 * Base64デコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64デコードされたデータ
	 */
	public static byte[] base64Decode(final String str, final String charset) {
		return base64Decode(ConvertUtils.stringToBytes(str, Charset.forName(charset)));
	}

	/**
	 * Base64デコード
	 * @param bytes 対象
	 * @param charset 文字コード
	 * @return Base64デコードされたデータ
	 */
	public static String base64DecodeToString(final byte[] bytes, final String charset) {
		return base64DecodeToString(bytes, Charset.forName(charset));
	}

	/**
	 * Base64デコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64デコードされたデータ
	 */
	public static String base64DecodeToString(final String str, final String charset) {
		return base64DecodeToString(ConvertUtils.stringToBytes(str, Charset.forName(charset)), Charset.forName(charset));
	}

	/**
	 * Base64デコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64デコードされたデータ
	 */
	public static byte[] base64Decode(final String str, final Charset charset) {
		byte[] base64 = ConvertUtils.stringToBytes(str, charset);
		return base64Decode(base64);
	}

	/**
	 * Base64デコード
	 * @param bytes 対象
	 * @param charset 文字コード
	 * @return Base64デコードされたデータ
	 */
	public static String base64DecodeToString(final byte[] bytes, final Charset charset) {
		byte[] base64 = base64Decode(bytes);
		return ConvertUtils.bytesToString(base64, charset);
	}

	/**
	 * Base64デコード
	 * @param str 対象
	 * @param charset 文字コード
	 * @return Base64デコードされたデータ
	 */
	public static String base64DecodeToString(final String str, final Charset charset) {
		byte[] base64 = ConvertUtils.stringToBytes(str, charset);
		base64 = base64Decode(base64);
		return ConvertUtils.bytesToString(base64, charset);
	}

	/**
	 * Base64デコード
	 * @param bytes 対象
	 * @return Base64デコードされたデータ
	 */
	public static byte[] base64Decode(final byte[] bytes) {
		if (bytes == null) {
			return new byte[0];
		}
		return Base64.getDecoder().decode(bytes);
	}

}
