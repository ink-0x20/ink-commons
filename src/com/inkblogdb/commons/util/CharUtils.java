package com.inkblogdb.commons.util;

import java.nio.charset.Charset;

/**
 * @author ink-0x20
 *
 */
public class CharUtils {

	/** 日本語を扱える */
	public static final String SJIS = "Shift_JIS";
	/** 日本語を扱うが、英数字の割合が多い場合は効率がいい */
	public static final String UTF8 = "UTF-8";
	/** 日本語を扱うが、日本語の割合が多い場合は効率がいい */
	public static final String UTF16 = "UTF-16";
	/** BOMの付かないビックエンディアン */
	public static final String UTF16BE = "UTF-16BE";
	/** BOMの付かないリトルエンディアン */
	public static final String UTF16LE = "UTF-16LE";

	/** 日本語を扱える */
	public static final Charset SJIS_CHARSET = Charset.forName(SJIS);
	/** 日本語を扱うが、英数字の割合が多い場合は効率がいい */
	public static final Charset UTF8_CHARSET = Charset.forName(UTF8);
	/** 日本語を扱うが、日本語の割合が多い場合は効率がいい */
	public static final Charset UTF16_CHARSET = Charset.forName(UTF16);
	/** BOMの付かないビックエンディアン */
	public static final Charset UTF16BE_CHARSET = Charset.forName(UTF16BE);
	/** BOMの付かないリトルエンディアン */
	public static final Charset UTF16LE_CHARSET = Charset.forName(UTF16LE);

}
