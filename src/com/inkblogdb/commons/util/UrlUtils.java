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
		return getDomain(url, true, true);
	}

	/**
	 * URLからドメインを抜き出し
	 * @param url ドメインを特定したいURL
	 * @param isHost ホスト(www.等)
	 * @param isTopLevelDomain トップレベルドメイン(.com等)
	 * @return ドメイン
	 */
	public static final String getDomain(final String url, final boolean isHost, final boolean isTopLevelDomain) {
		if (StringUtils.isBlank(url)) {
			return "";
		}
		String domain = null;
		// 開始位置
		int first = url.indexOf("://");
		if (first == -1) {
			return url;
		}
		first += 3;
		// 終了位置
		int last = url.indexOf("/", first);
		if (last == -1) {
			// 最後まで
			domain = url.substring(first);
		} else {
			domain = url.substring(first, last);
		}
		// ホスト・ドメイン・トップレベルドメインに分割
		String[] domains = domain.split("\\.");
		if (domains.length == 2) {
			if (isTopLevelDomain) {
				// ドメイン + トップレベルドメイン
				return domain;
			}
			// ドメイン
			return domains[0];
		}
		if (domains.length == 3) {
			if (isHost) {
				if (isTopLevelDomain) {
					// ホスト + ドメイン + トップレベルドメイン
					return domain;
				}
				// ホスト + ドメイン
				return domains[0] + "." + domains[1];
			}
			if (isTopLevelDomain) {
				// ドメイン + トップレベルドメイン
				return domains[1] + "." + domains[2];
			}
			// ドメイン
			return domains[1];
		}
		return domain;
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
