package com.inkblogdb.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author ink-0x20
 *
 */
public class DateUtils {

	/** 年(yyyy) */
	public static final String YYYY = "yyyy";
	/** 最小限日付(yyMd) */
	public static final String YYMD = "yyMd";
	/** 最小限日付(yyMMdd) */
	public static final String YYMMDD = "yyMMdd";
	/** 通常装飾なし日付(yyyyMMdd) */
	public static final String YYYYMMDD = "yyyyMMdd";
	/** 通常日付(yyyy/MM/dd) */
	public static final String YYYY_MM_DD = "yyyy/MM/dd";
	/** 通常装飾なし日時+時間(yyMMddHH) */
	public static final String YYMMDDHH = "yyMMddHH";
	/** 日本表記日付(yyyy年M月d日) */
	public static final String YYYY__M__D = "yyyy年M月d日";
	/** 日本表記日付(yyyy年MM月dd日) */
	public static final String YYYY__MM__DD = "yyyy年MM月dd日";
	/** 装飾なし日時(yyyyMMdd HHmmss) */
	public static final String YYYYMMDD_HHMMSS = "yyyyMMdd HHmmss";
	/** 一般的装飾日時(yyyy/MM/dd HH:mm:ss) */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
	/** ISO8601形式の日時(EEE, dd MMM yyyy HH:mm:ss +0900) */
	public static final String EEE_DD_MMM_YYYY_HH_MM_SS_0900 = "EEE, dd MMM yyyy HH:mm:ss +0900";

	/**
	 * 文字列の日時をフォーマットを変換
	 * @param date 変換したい文字列
	 * @param beforeFormat 変換前フォーマット
	 * @param afterFormat 変換後フォーマット
	 * @return 変換後日時
	 */
	public static final String changeFormat(final String date, final String beforeFormat, final String afterFormat) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		if (StringUtils.isBlank(beforeFormat)) {
			return null;
		}
		if (StringUtils.isBlank(afterFormat)) {
			return null;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(beforeFormat);
		LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
		return toString(localDateTime, afterFormat);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param localDate 日付
	 * @param format フォーマット
	 * @return 文字列
	 */
	public static final String toString(final LocalDate localDate, final String format) {
		return toString(localDate, format, Locale.JAPANESE);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param localDate 日付
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return 文字列
	 */
	public static final String toString(final LocalDate localDate, final String format, final Locale locale) {
		if (localDate == null) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format, l);
		return localDate.format(dateTimeFormatter);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param localDateTime 日時
	 * @param format フォーマット
	 * @return 文字列
	 */
	public static final String toString(final LocalDateTime localDateTime, final String format) {
		return toString(localDateTime, format, Locale.JAPANESE);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param localDateTime 日時
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return 文字列
	 */
	public static final String toString(final LocalDateTime localDateTime, final String format, final Locale locale) {
		if (localDateTime == null) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format, l);
		return localDateTime.format(dateTimeFormatter);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param zonedDateTime 日時
	 * @param format フォーマット
	 * @return 文字列
	 */
	public static final String toString(final ZonedDateTime zonedDateTime, final String format) {
		return toString(zonedDateTime, format, Locale.JAPANESE);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param zonedDateTime 日時
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return 文字列
	 */
	public static final String toString(final ZonedDateTime zonedDateTime, final String format, final Locale locale) {
		if (zonedDateTime == null) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format, l);
		return zonedDateTime.format(dateTimeFormatter);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param date 日時
	 * @param format フォーマット
	 * @return 文字列
	 */
	public static final String toString(final Date date, final String format) {
		return toString(date, format, Locale.JAPANESE);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param date 日時
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return 文字列
	 */
	public static final String toString(final Date date, final String format, final Locale locale) {
		if (date == null) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, l);
		return dateFormat.format(date);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param sqlDate 日時
	 * @param format フォーマット
	 * @return 文字列
	 */
	public static final String toString(final java.sql.Date sqlDate, final String format) {
		return toString(sqlDate, format, Locale.JAPANESE);
	}

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param sqlDate 日時
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return 文字列
	 */
	public static final String toString(final java.sql.Date sqlDate, final String format, final Locale locale) {
		if (sqlDate == null) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, l);
		return dateFormat.format(sqlDate);
	}

	/**
	 * LocalDate形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @return LocalDate形式の日時
	 */
	public static final LocalDate toLocalDate(final String date, final String format) {
		return toLocalDate(date, format, Locale.JAPANESE);
	}

	/**
	 * LocalDate形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return LocalDate形式の日時
	 */
	public static final LocalDate toLocalDate(final String date, final String format, final Locale locale) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format, l);
		return LocalDate.parse(date, dateTimeFormatter);
	}

	/**
	 * LocalDate形式に変換
	 * @param localDateTime 変換したいクラス
	 * @return LocalDate形式の日時
	 */
	public static final LocalDate toLocalDate(final LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.toLocalDate();
	}

	/**
	 * LocalDate形式に変換
	 * @param zonedDateTime 変換したいクラス
	 * @return LocalDate形式の日時
	 */
	public static final LocalDate toLocalDate(final ZonedDateTime zonedDateTime) {
		if (zonedDateTime == null) {
			return null;
		}
		return zonedDateTime.toLocalDate();
	}

	/**
	 * LocalDate形式に変換
	 * @param date 変換したいクラス
	 * @return LocalDate形式の日時
	 */
	public static final LocalDate toLocalDate(final Date date) {
		return toLocalDate(date, ZoneId.systemDefault());
	}

	/**
	 * LocalDate形式に変換
	 * @param date 変換したいクラス
	 * @param zoneId ゾーン
	 * @return LocalDate形式の日時
	 */
	public static final LocalDate toLocalDate(final Date date, final ZoneId zoneId) {
		if (date == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		return LocalDate.ofInstant(date.toInstant(), z);
	}

	/**
	 * LocalDate形式に変換
	 * @param sqlDate 変換したいクラス
	 * @return LocalDate形式の日時
	 */
	public static final LocalDate toLocalDate(final java.sql.Date sqlDate) {
		return toLocalDate(sqlDate, ZoneId.systemDefault());
	}

	/**
	 * LocalDate形式に変換
	 * @param sqlDate 変換したいクラス
	 * @param zoneId ゾーン
	 * @return LocalDate形式の日時
	 */
	public static final LocalDate toLocalDate(final java.sql.Date sqlDate, final ZoneId zoneId) {
		if (sqlDate == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		Date date = new Date(sqlDate.getTime());
		return LocalDate.ofInstant(date.toInstant(), z);
	}

	/**
	 * LocalDateTime形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @return LocalDateTime形式の日時
	 */
	public static final LocalDateTime toLocalDateTime(final String date, final String format) {
		return toLocalDateTime(date, format, Locale.JAPANESE);
	}

	/**
	 * LocalDateTime形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return LocalDateTime形式の日時
	 */
	public static final LocalDateTime toLocalDateTime(final String date, final String format, final Locale locale) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format, l);
		return LocalDateTime.parse(date, dateTimeFormatter);
	}

	/**
	 * LocalDateTime形式に変換
	 * @param localDate 変換したいクラス
	 * @return LocalDateTime形式の日時
	 */
	public static final LocalDateTime toLocalDateTime(final LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return localDate.atStartOfDay();
	}

	/**
	 * LocalDateTime形式に変換
	 * @param zonedDateTime 変換したいクラス
	 * @return LocalDateTime形式の日時
	 */
	public static final LocalDateTime toLocalDateTime(final ZonedDateTime zonedDateTime) {
		if (zonedDateTime == null) {
			return null;
		}
		return zonedDateTime.toLocalDateTime();
	}

	/**
	 * LocalDateTime形式に変換
	 * @param date 変換したいクラス
	 * @return LocalDateTime形式の日時
	 */
	public static final LocalDateTime toLocalDateTime(final Date date) {
		return toLocalDateTime(date, ZoneId.systemDefault());
	}

	/**
	 * LocalDateTime形式に変換
	 * @param date 変換したいクラス
	 * @param zoneId ゾーン
	 * @return LocalDateTime形式の日時
	 */
	public static final LocalDateTime toLocalDateTime(final Date date, final ZoneId zoneId) {
		if (date == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		return LocalDateTime.ofInstant(date.toInstant(), z);
	}

	/**
	 * LocalDateTime形式に変換
	 * @param sqlDate 変換したいクラス
	 * @return LocalDateTime形式の日時
	 */
	public static final LocalDateTime toLocalDateTime(final java.sql.Date sqlDate) {
		return toLocalDateTime(sqlDate, ZoneId.systemDefault());
	}

	/**
	 * LocalDateTime形式に変換
	 * @param sqlDate 変換したいクラス
	 * @param zoneId ゾーン
	 * @return LocalDateTime形式の日時
	 */
	public static final LocalDateTime toLocalDateTime(final java.sql.Date sqlDate, final ZoneId zoneId) {
		if (sqlDate == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		Date date = new Date(sqlDate.getTime());
		return LocalDateTime.ofInstant(date.toInstant(), z);
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final String date, final String format) {
		return toZonedDateTime(date, format, Locale.JAPANESE);
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final String date, final String format, final Locale locale) {
		return toZonedDateTime(date, format, locale, ZoneId.systemDefault());
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @param zoneId ゾーン
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final String date, final String format, final ZoneId zoneId) {
		return toZonedDateTime(date, format, Locale.JAPANESE, zoneId);
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @param locale ロケール
	 * @param zoneId ゾーン
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final String date, final String format, final Locale locale, final ZoneId zoneId) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format, l);
		LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
		return localDateTime.atZone(z);
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param localDate 変換したいクラス
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final LocalDate localDate) {
		return toZonedDateTime(localDate, ZoneId.systemDefault());
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param localDate 変換したいクラス
	 * @param zoneId ゾーン
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final LocalDate localDate, final ZoneId zoneId) {
		if (localDate == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		LocalDateTime localDateTime = localDate.atStartOfDay();
		return localDateTime.atZone(z);
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param localDateTime 変換したいクラス
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final LocalDateTime localDateTime) {
		return toZonedDateTime(localDateTime, ZoneId.systemDefault());
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param localDateTime 変換したいクラス
	 * @param zoneId ゾーン
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final LocalDateTime localDateTime, final ZoneId zoneId) {
		if (localDateTime == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		return localDateTime.atZone(z);
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param date 変換したいクラス
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final Date date) {
		return toZonedDateTime(date, ZoneId.systemDefault());
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param date 変換したいクラス
	 * @param zoneId ゾーン
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final Date date, final ZoneId zoneId) {
		if (date == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		return ZonedDateTime.ofInstant(date.toInstant(), z);
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param sqlDate 変換したいクラス
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final java.sql.Date sqlDate) {
		return toZonedDateTime(sqlDate, ZoneId.systemDefault());
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param sqlDate 変換したいクラス
	 * @param zoneId ゾーン
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final java.sql.Date sqlDate, final ZoneId zoneId) {
		if (sqlDate == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		Date date = new Date(sqlDate.getTime());
		return ZonedDateTime.ofInstant(date.toInstant(), z);
	}

	/**
	 * Date形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @return Date形式の日時
	 * @throws ParseException 変換失敗
	 */
	public static final Date toDate(final String date, final String format) throws ParseException {
		return toDate(date, format, Locale.JAPANESE);
	}

	/**
	 * Date形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return Date形式の日時
	 * @throws ParseException 変換失敗
	 */
	public static final Date toDate(final String date, final String format, final Locale locale) throws ParseException {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, l);
		return simpleDateFormat.parse(date);
	}

	/**
	 * Date形式に変換
	 * @param localDate 変換したいクラス
	 * @return Date形式の日時
	 */
	public static final Date toDate(final LocalDate localDate) {
		return toDate(localDate, ZoneId.systemDefault());
	}

	/**
	 * Date形式に変換
	 * @param localDate 変換したいクラス
	 * @param zoneId ゾーン
	 * @return Date形式の日時
	 */
	public static final Date toDate(final LocalDate localDate, final ZoneId zoneId) {
		if (localDate == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		LocalDateTime localDateTime = localDate.atStartOfDay();
		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, z);
		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * Date形式に変換
	 * @param localDateTime 変換したいクラス
	 * @return Date形式の日時
	 */
	public static final Date toDate(final LocalDateTime localDateTime) {
		return toDate(localDateTime, ZoneId.systemDefault());
	}

	/**
	 * Date形式に変換
	 * @param localDateTime 変換したいクラス
	 * @param zoneId ゾーン
	 * @return Date形式の日時
	 */
	public static final Date toDate(final LocalDateTime localDateTime, final ZoneId zoneId) {
		if (localDateTime == null) {
			return null;
		}
		ZoneId z = zoneId;
		if (zoneId == null) {
			z = ZoneId.systemDefault();
		}
		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, z);
		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * Date形式に変換
	 * @param zonedDateTime 変換したいクラス
	 * @return Date形式の日時
	 */
	public static final Date toDate(final ZonedDateTime zonedDateTime) {
		if (zonedDateTime == null) {
			return null;
		}
		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * Date形式に変換
	 * @param sqlDate 変換したいクラス
	 * @return Date形式の日時
	 */
	public static final Date toDate(final java.sql.Date sqlDate) {
		if (sqlDate == null) {
			return null;
		}
		return new Date(sqlDate.getTime());
	}

	/**
	 * SqlDate形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return SqlDate形式の日時
	 */
	public static final java.sql.Date toSqlDate(final String date, final String format) {
		return toSqlDate(date, format, Locale.JAPANESE);
	}

	/**
	 * SqlDate形式に変換
	 * @param date 変換したいクラス
	 * @param format フォーマット
	 * @param locale ロケール
	 * @return SqlDate形式の日時
	 */
	public static final java.sql.Date toSqlDate(final String date, final String format, final Locale locale) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		Locale l = locale;
		if (locale == null) {
			l = Locale.JAPANESE;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format, l);
		LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
		return java.sql.Date.valueOf(localDate);
	}

	/**
	 * SqlDate形式に変換
	 * @param localDate 変換したいクラス
	 * @return SqlDate形式の日時
	 */
	public static final java.sql.Date toSqlDate(final LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return java.sql.Date.valueOf(localDate);
	}

	/**
	 * SqlDate形式に変換
	 * @param localDateTime 変換したいクラス
	 * @return SqlDate形式の日時
	 */
	public static final java.sql.Date toSqlDate(final LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return java.sql.Date.valueOf(localDateTime.toLocalDate());
	}

	/**
	 * SqlDate形式に変換
	 * @param zonedDateTime 変換したいクラス
	 * @return SqlDate形式の日時
	 */
	public static final java.sql.Date toSqlDate(final ZonedDateTime zonedDateTime) {
		if (zonedDateTime == null) {
			return null;
		}
		return java.sql.Date.valueOf(zonedDateTime.toLocalDate());
	}

	/**
	 * SqlDate形式に変換
	 * @param date 変換したいクラス
	 * @return SqlDate形式の日時
	 */
	public static final java.sql.Date toSqlDate(final Date date) {
		if (date == null) {
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

}
