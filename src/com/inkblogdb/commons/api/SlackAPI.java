package com.inkblogdb.commons.api;

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
public class SlackAPI {

	/**
	 * Slack APIでメッセージを送信
	 *
	 * @see <a href="https://api.slack.com/methods/chat.postMessage">Slack api</a>
	 * @param token 発行したトークン「token Bot User OAuth Token」
	 * @param channelId SlackのチャンネルID
	 * @param message 送信したいメッセージ
	 * @return APIレスポンス
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static final HttpResponse<String> sendMessage(final String token, final String channelId, final String message) throws IOException, InterruptedException {
		StringBuilder body = new StringBuilder();
		body.append("token=").append(token).append("&");
		body.append("channel=").append(channelId).append("&");
		body.append("text=").append(message);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://slack.com/api/chat.postMessage"))
				.header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
				.POST(BodyPublishers.ofString(body.toString()))
				.build();
		return HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
	}

}
