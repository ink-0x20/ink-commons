package com.inkblogdb.commons.api;

import com.inkblogdb.commons.util.ConvertUtils;
import com.inkblogdb.commons.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * @author ink-0x20
 */
public class LineAPI {

	/**
	 * Line APIでメッセージを送信
	 *
	 * @see <a href="https://notify-bot.line.me/doc/ja/">Line api</a>
	 * @param token 発行したアクセストークン
	 * @param message 送信したいメッセージ
	 * @return APIレスポンス
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static HttpResponse<String> sendMessage(final String token, final String message) throws IOException, InterruptedException {
		StringBuilder body = new StringBuilder();
		body.append("message=").append(escapeMessage(message));
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://notify-api.line.me/api/notify"))
				.header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
				.header("Authorization", "Bearer " + token)
				.POST(BodyPublishers.ofString(body.toString()))
				.build();
		return HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
	}

	/**
	 * APIメッセージ用にエスケープ
	 * @param message メッセージ
	 * @return エスケープメッセージ
	 */
	public static String escapeMessage(final String message) {
		if (StringUtils.isBlank(message)) {
			return "";
		}
		String str = message;
		// 改行コード置換
		str = ConvertUtils.replaceToLf(str);
		return str;
	}

}
