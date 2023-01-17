package com.inkblogdb.commons.util;

/**
 * @author ink-0x20
 *
 */
public class UrlUtils {

	/**
	 * URLからドメインを抜き出し
	 * @param url ドメインを特定したいURL
	 * @return ドメイン
	 */
	public static final String getDomain(final String url) {
		if (StringUtils.isBlank(url)) {
			return "";
		}
		int i = url.indexOf("://");
		if (i == -1) {
			return url;
		}
		i += 3;
		int j = url.indexOf("/", i);
		if (j == -1) {
			return url;
		}
		return url.substring(i, j);
	}

	/**
	 * URLにリクエストパラメタを付与
	 * @param url 付与するURL
	 * @param paramName リクエストパラメタ名
	 * @param paramValue リクエストパラメタ値
	 * @return リクエストパラメタを付与したURL
	 */
	public static final String addUrlParam(final String url, final String paramName, final String paramValue) {
		if (StringUtils.isBlank(url)) {
			return "";
		}
		if (StringUtils.isBlank(paramName)) {
			return url;
		}
		StringBuilder sb = new StringBuilder(url);
		if (url.indexOf("?") == -1) {
			sb.append("?");
		} else {
			sb.append("&");
		}
		sb.append(paramName).append("=");
		return sb.append(StringUtils.defaultString(paramValue)).toString();
	}

	/**
	 * URLにid指定を付与
	 * @param url 付与するURL
	 * @param id ID値
	 * @return IDを付与したURL
	 */
	public static final String addUrlId(final String url, final String id) {
		if (StringUtils.isBlank(url)) {
			return "";
		}
		if (StringUtils.isBlank(id)) {
			return url;
		}
		if (url.indexOf("#") != -1) {
			return url;
		}
		StringBuilder sb = new StringBuilder(url);
		sb.append("#");
		sb.append(StringUtils.defaultString(id));
		return sb.toString();
	}

}
