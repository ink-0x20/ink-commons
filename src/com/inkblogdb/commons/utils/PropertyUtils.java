package com.inkblogdb.commons.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author ink-0x20
 */
public class PropertyUtils {

	/**
	 * propertiesファイルをMap化して読み込み
	 * @param filePath ファイルパス
	 * @return propertiesファイルをMap化
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static Map<String, String> getPropertyMap(final String filePath) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		Map<String, String> propMap = new HashMap<>();

		try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8);){
			prop.load(inputStreamReader);
			for (Entry<Object, Object> entry : prop.entrySet()) {
				propMap.put(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		return propMap;
	}

	/**
	 * propertiesファイルの特定キーを取得します
	 * @param filePath ファイルパス
	 * @param key キー
	 * @return 設定値
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String getProperty(final String filePath, final String key) throws FileNotFoundException, IOException {
		Map<String, String> propMap = getPropertyMap(filePath);
		return propMap.get(key);
	}

}
