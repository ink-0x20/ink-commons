package com.inkblogdb.commons.util;

/**
 * @author ink-0x20
 */
public class ArrayUtils {

	/** 存在しないインデックス */
	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * 配列をクローン
	 *
	 * @param array 配列
	 * @return クローン配列
	 */
	public static byte[] clone(final byte[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * 指定すべての配列を連結
	 *
	 * @param array1 結合元配列
	 * @param array2 結合したい配列
	 * @return 結合配列
	 */
	public static byte[] addAll(final byte[] array1, final byte... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		final byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * インデックスを検索
	 *
	 * @param array 検索配列
	 * @param valueToFind 検索値
	 * @return インデックス
	 */
	public static int indexOf(final byte[] array, final byte valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * インデックスを検索
	 *
	 * @param array 検索配列
	 * @param valueToFind 検索値
	 * @param startIndex 検索開始インデックス
	 * @return インデックス
	 */
	public static int indexOf(final byte[] array, final byte valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		int start = startIndex;
		if (start < 0) {
			start = 0;
		}
		for (int i = start; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 配列を逆順に並び替え
	 * @param array 配列
	 * @return 逆順配列
	 */
	public static byte[] reverse(final byte[] array) {
		if (array == null) {
			return null;
		}
		int length = array.length;
		byte[] bytes = new byte[length];
		for (int i = 0, j = length - 1; i < length; i++, j--) {
			bytes[i] = array[j];
		}
		return bytes;
	}

}
