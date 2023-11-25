# ink-commons [![license](https://img.shields.io/badge/license-MIT-green.svg?style=flat-square)](https://github.com/ink-0x20/ink-commons/blob/main/LICENSE)
#### [English](https://translate.google.com/translate?sl=ja&tl=en&u=https://github.com/ink-0x20/ink-commons) (by Google Translate)  
## 概要
Javaの外部ライブラリを使用せずに作成したライブラリです。

会社などで外部ライブラリを導入できない場合や、Javaの勉強用に処理を知りたい場合など、お役に立つと幸いです。

昨今外部ライブラリで脆弱性が見つかるなど、不安な方も是非参考程度に・・・。

## 開発環境
Java17

## 使い方
### ./src/com/inkblogdb/commons/api
#### ChatworkAPI
##### Chatworkでメッセージ送信
```Java
HttpResponse<String> chatworkResponse = ChatworkAPI.sendMessage(
	  "xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
	, "12345678"
	, "test\nです"
);
System.out.println(chatworkResponse.body());
```

#### DiscordAPI
##### Discordでメッセージ送信
```Java
HttpResponse<String> discordkResponse = DiscordAPI.sendMessage(
	  "xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
	, "12345678"
	, "test\nです"
);
System.out.println(discordkResponse.body());
```

#### LineAPI
##### Lineでメッセージ送信
```Java
HttpResponse<String> lineResponse = LineAPI.sendMessage(
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
        , "test\nです"
        );
        System.out.println(lineResponse.body());
```

#### SlackAPI
##### Slackでメッセージ送信
```Java
HttpResponse<String> slackResponse = SlackAPI.sendMessage(
	  "xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
	, "12345678"
	, "test\nです"
);
System.out.println(slackResponse.body());
```

#### GitHubAPI
##### GitHubでRelease作成
```Java
HttpResponse<String> makeReleaseResponse = GitHubAPI.makeRelease(
	  "ink-0x20"
	, "ink-commons"
	, "xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
	, "v1.0.0"
	, "main"
	, "説明！"
);
System.out.println(makeReleaseResponse.body());
```

##### GitHubでReleaseにファイルをアップロード
```Java
String responseJson = makeReleaseResponse.body();
String releaseId = responseJson.substring(responseJson.indexOf("\"id\":") + 5);
releaseId = releaseId.substring(0, releaseId.indexOf(","));

File uploadFile = new File("./test.jar");
try (FileInputStream fileInputStream = new FileInputStream(uploadFile)) {
	HttpResponse<String> uploadReleaseResponse =
			GitHubAPI.uploadRelease(
					  "ink-0x20"
					, "ink-commons"
					, releaseId
					, uploadFile.getName()
					, "xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
					, fileInputStream.readAllBytes()
			);
	System.out.println(uploadReleaseResponse.body());
} catch (Exception e) {
	System.out.println("Failed to upload release");
	System.out.println(e.getMessage());
}
```

## LICENSE
[MIT](https://github.com/ink-0x20/ink-commons/blob/main/LICENSE)
