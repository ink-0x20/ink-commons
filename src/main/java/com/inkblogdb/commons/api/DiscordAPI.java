package com.inkblogdb.commons.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.inkblogdb.commons.util.ConvertUtils;
import com.inkblogdb.commons.util.StringUtils;

/**
 * @author ink-0x20
 */
public class DiscordAPI {

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
				.uri(URI.create("https://discordapp.com/api/channels/" + channelId + "/messages"))
				.header("content-type", "application/json")
				.header("Authorization", "Bot " + token)
				.POST(BodyPublishers.ofString("{\"content\":\"" + escapeMessage(message) + "\"}"))
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
		str = str.replaceAll("\n", "\\\\n");
		return str;
	}

}
