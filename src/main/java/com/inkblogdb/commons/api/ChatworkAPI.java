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
public class ChatworkAPI {

	/**
	 * Discord APIでメッセージを送信
	 *
	 * @see <a href="https://developer.chatwork.com/reference/post-rooms-room_id-messages">Chatwork Developer</a>
	 * @param token 「サービス連携/API/APIトークン」で作成したAPIトークン
	 * @param roomId ChatworkのルームID
	 * @param message 送信したいメッセージ
	 * @return APIレスポンス
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static final HttpResponse<String> sendMessage(final String token, final String roomId, final String message) throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.chatwork.com/v2/rooms/" + roomId + "/messages"))
				.header("accept", "application/json")
				.header("content-type", "application/x-www-form-urlencoded")
				.header("x-chatworktoken", token)
				.POST(BodyPublishers.ofString("self_unread=0&body=" + escapeMessage(message)))
				.build();
		return HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
	}

	/**
	 * APIメッセージ用にエスケープ
	 * @param message メッセージ
	 * @return エスケープメッセージ
	 */
	public static final String escapeMessage(final String message) {
		if (StringUtils.isBlank(message)) {
			return "";
		}
		String str = message;
		// 改行コード置換
		str = ConvertUtils.replaceToLf(str);
		return str;
	}

}
