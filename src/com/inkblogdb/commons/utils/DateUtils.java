package com.inkblogdb.commons.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ink-0x20
 *
 */
public class DateUtils {

	/** 通常装飾なし日時(yyyyMMdd) */
	public static final String YYYYMMDD = "yyyyMMdd";
	/** 日本表記日時1(yyyy年M月d日) */
	public static final String YYYY__MM__DD1 = "yyyy年M月d日";
	/** 最小限日時(yyMd) */
	public static final String YYMD = "yyMd";
	/** 最小限日時(yyMMdd) */
	public static final String YYMMDD = "yyMMdd";
	/** 通常装飾なし日時+時間(yyMMddHH) */
	public static final String YYMMDDHH = "yyMMddHH";
	/** 年(yyyy) */
	public static final String YYYY = "yyyy";
	/** 装飾なし日時(yyyyMMdd HHmmss) */
	public static final String YYYYMMDD_HHMMSS = "yyyyMMdd HHmmss";
	/** 一般的装飾日時(yyyy/MM/dd HH:mm:ss) */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
	/** ISO8601形式の日時(EEE, dd MMM yyyy HH:mm:ss +0900) */
	public static final String EEE_DD_MMM_YYYY_HH_MM_SS_0900 = "EEE, dd MMM yyyy HH:mm:ss +0900";

	/**
	 * 指定のフォーマットの文字列に変換
	 * @param zonedDateTime 日時
	 * @param format フォーマット
	 * @return 文字列
	 */
	public static final String format(final ZonedDateTime zonedDateTime, final String format) {
		if (zonedDateTime == null) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			return null;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
		return zonedDateTime.format(dateTimeFormatter);
	}

	/**
	 * 時間を加算
	 * @param date 加算する日時
	 * @param amount 加算する時間
	 * @return 加算された日時
	 */
	public static final Date plusHour(final Date date, final int amount) {
		if (date == null) {
			return null;
		}
		ZonedDateTime zonedDateTime = toZonedDateTime(date);
		zonedDateTime = zonedDateTime.plusHours(amount);
		return toDate(zonedDateTime);
	}

	/**
	 * 日付を加算
	 * @param date 加算する日時
	 * @param amount 加算する日数
	 * @return 加算された日時
	 */
	public static final Date plusDay(final Date date, final int amount) {
		if (date == null) {
			return null;
		}
		ZonedDateTime zonedDateTime = toZonedDateTime(date);
		zonedDateTime = zonedDateTime.plusDays(amount);
		return toDate(zonedDateTime);
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param date 変換したいクラス
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final Date date) {
		if (date == null) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault());
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param date 変換したいクラス
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final java.sql.Date date) {
		if (date == null) {
			return null;
		}
		return date.toLocalDate().atStartOfDay(ZoneId.systemDefault());
	}

	/**
	 * ZonedDateTime形式に変換
	 * @param calendar 変換したいクラス
	 * @return ZonedDateTime形式の日時
	 */
	public static final ZonedDateTime toZonedDateTime(final Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.toInstant().atZone(ZoneId.systemDefault());
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
	 * Date形式に変換
	 * @param zonedDateTime 変換したいクラス
	 * @return Date形式の日時
	 */
	public static final Calendar toCalendar(final ZonedDateTime zonedDateTime) {
		if (zonedDateTime == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(toDate(zonedDateTime));
		return calendar;
	}

}
