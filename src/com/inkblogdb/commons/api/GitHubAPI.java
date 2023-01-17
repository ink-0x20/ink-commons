package com.inkblogdb.commons.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import com.inkblogdb.commons.utils.ConvertUtils;

/**
 * @author ink-0x20
 */
public class GitHubAPI {

	/** GitHub API バージョン */
	private static final String API_VERSION = "2022-11-28";

	/** 成功時のHTTPステータスコード */
	public static final int SUCCESS_STATUS_CODE = 201;

	/**
	 * GitHub APIのレスポンスが正常かを判定
	 *
	 * @param response APIレスポンス
	 * @return APIが正常の場合true
	 */
	public static final boolean isSuccesses(final HttpResponse<String> response) {
		return response.statusCode() == SUCCESS_STATUS_CODE;
	}

	/**
	 * GitHub APIのレスポンスが正常かを判定
	 *
	 * @param response APIレスポンス
	 * @return APIが正常の場合true
	 */
	public static final boolean isFailed(final HttpResponse<String> response) {
		return !isSuccesses(response);
	}

	/**
	 * GitHub APIでリリースを作成
	 *
	 * @see <a href="https://docs.github.com/ja/rest/releases/releases?apiVersion=2022-11-28#create-a-release">GitHub Docs</a>
	 * @param owner ユーザ
	 * @param repo リポジトリ
	 * @param token 「Settings/Developer settings/Personal access tokens/Tokens(classic)」で作成したトークン
	 * @param tagName タグ名
	 * @param commitish ターゲット
	 * @param body 説明
	 * @return APIレスポンス
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static final HttpResponse<String> makeRelease(final String owner, final String repo, final String token, final String tagName, final String commitish, final String body) throws IOException, InterruptedException {
		HashMap<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", tagName);
		requestBody.put("tag_name", tagName);
		requestBody.put("target_commitish", commitish);
		requestBody.put("body", body);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.github.com/repos/" + owner + "/" + repo + "/releases"))
				.header("Accept", "application/vnd.github+json")
				.header("Authorization", "Bearer " + token)
				.header("X-GitHub-Api-Version", API_VERSION)
				.method("POST", HttpRequest.BodyPublishers.ofString(ConvertUtils.mapTpJson(requestBody)))
				.build();
		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
	}

	/**
	 * GitHub APIでリリースに対してファイルをアップロード
	 *
	 * @see <a href="https://docs.github.com/ja/rest/releases/assets?apiVersion=2022-11-28#upload-a-release-asset">GitHub Docs</a>
	 * @param owner ユーザ
	 * @param repo リポジトリ
	 * @param releaseId リリースID（ファイルをアップロードしたいリリースのID）
	 * @param fileName ファイル名
	 * @param token 「Settings/Developer settings/Personal access tokens/Tokens(classic)」で作成したトークン
	 * @param data 送信データ（ファイルの場合、ファイルすべてをbyte配列で読み込んだデータ）
	 * @return APIレスポンス
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static final HttpResponse<String> uploadRelease(final String owner, final String repo, final String releaseId, final String fileName, final String token, final byte[] data) throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://uploads.github.com/repos/" + owner + "/" + repo + "/releases/" + releaseId + "/assets?name=" + fileName))
				.header("Accept", "application/vnd.github+json")
				.header("Authorization", "Bearer " + token)
				.header("X-GitHub-Api-Version", API_VERSION)
				.header("Content-Type", "multipart/form-data")
				.method("POST", HttpRequest.BodyPublishers.ofByteArray(data))
				.build();
		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
	}

	/**
	 * GitHub APIでリリースに対してファイルをアップロード
	 *
	 * @see <a href="https://docs.github.com/ja/rest/releases/assets?apiVersion=2022-11-28#upload-a-release-asset">GitHub Docs</a>
	 * @param owner ユーザ
	 * @param repo リポジトリ
	 * @param releaseId リリースID（ファイルをアップロードしたいリリースのID）
	 * @param filePath ファイルパス
	 * @param token 「Settings/Developer settings/Personal access tokens/Tokens(classic)」で作成したトークン
	 * @param data 送信データ（ファイルの場合、ファイルすべてをbyte配列で読み込んだデータ）
	 * @return APIレスポンス
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static final HttpResponse<String> uploadRelease(final String owner, final String repo, final String releaseId, final String filePath, final String token) throws IOException, InterruptedException {
		File dataFile = new File(filePath);
		if (!dataFile.exists()) {
			return null;
		}
		try (FileInputStream data = new FileInputStream(dataFile)) {
			return uploadRelease(owner, repo, releaseId, dataFile.getName(), token, data.readAllBytes());
		}
	}

}
