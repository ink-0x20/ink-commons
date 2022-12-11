package com.inkblogdb.commons.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author ink-0x20
 */
public class DiscordAPI {

	/**Discord APIのベースURL */
	private static final String API_URL = "https://discordapp.com/api/channels/";
	/**Discord APIのメッセージ送信エンドポイント*/
	private static final String API_MESSAGE_END_POINT = "/messages";

	/**
	 * Discord APIでメッセージを送信
	 *
	 * @see <a href="https://discord.com/developers/applications/">Discord Developer Portal</a>
	 * @param token 「Discord Developer Portal」の各アプリケーションで作成した「Bot/TOKEN」のトークン
	 * @param channelId DiscordのチャンネルID
	 * @param message 送信したいメッセージ
	 * @return APIレスポンス
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static HttpResponse<String> sendMessage(final String token, final String channelId, final String message) throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(API_URL + channelId + API_MESSAGE_END_POINT))
				.header("content-type", "application/json")
				.header("Authorization", "Bot " + token)
				.method("POST", HttpRequest.BodyPublishers.ofString("{\"content\":\"" + message + "\"}"))
				.build();
		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
	}

}
