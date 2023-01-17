package com.inkblogdb.commons.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author ink-0x20
 */
public class EncryptUtils {

	/** 自動補完暗号化キーの長さ */
	private static final int AUTO_KEY_LENGTH = 128;
	/** byteずらし */
	private static final int SHIFT_BYTE = 73;
	/** 区切り文字1 */
	private static final byte SPLIT1 = ConvertUtils.stringToBytes("_")[0];
	/** 区切り文字2 */
	private static final byte SPLIT2 = ConvertUtils.stringToBytes("-")[0];

	/**
	 * セキュアなランダムbyte配列を作成
	 * @param length 作成するbyte数
	 * @return ランダムbyte配列
	 * @throws NoSuchAlgorithmException 使用不可や不正なアルゴリズムの場合に発生
	 */
	public static byte[] secureRandomBytes(int length) throws NoSuchAlgorithmException {
		if (length <= 0) {
			return new byte[0];
		}
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		byte[] randomBytes = new byte[length];
		secureRandom.nextBytes(randomBytes);
		return randomBytes;
	}

	/**
	 * セキュアなランダム文字列を作成
	 * @param length 作成する文字数
	 * @return ランダム文字列
	 * @throws NoSuchAlgorithmException 使用不可や不正なアルゴリズムの場合に発生
	 */
	public static String secureRandomString(int length) throws NoSuchAlgorithmException {
		return ConvertUtils.bytesToString(secureRandomBytes(length));
	}

	/**
	 * 暗号文の中に拡張子を保存
	 * ファイルを暗号化する際に、元ファイル拡張子を隠す際に使用
	 * @param target 暗号文
	 * @param extension 付与する拡張子
	 * @return 拡張子を付与した暗号文
	 */
	public static byte[] saveExtension(byte[] target, String extension) {
		byte[] bytes = ArrayUtils.addAll(ConvertUtils.stringToBytes(extension), SPLIT2);
		bytes = ArrayUtils.addAll(bytes, target);
		return Base64.getEncoder().encode(bytes);
	}

	/**
	 * 暗号文の中に保存されている拡張子を削除
	 * ファイルを暗号化する際に、元ファイル拡張子を隠す際に使用
	 * @param target 暗号文
	 * @return 拡張子を削除した暗号文
	 */
	public static byte[] deleteExtension(byte[] target) {
		byte[] bytes = Base64.getDecoder().decode(target);
		return Arrays.copyOfRange(bytes, ArrayUtils.indexOf(bytes, SPLIT2) + 1, bytes.length);
	}

	/**
	 * 暗号文の中に保存されている拡張子を取得
	 * ファイルを暗号化する際に、元ファイル拡張子を隠す際に使用
	 * @param target 暗号文
	 * @return 暗号文に付与された拡張子
	 */
	public static String loadExtension(byte[] target) {
		byte[] bytes = Base64.getDecoder().decode(target);
		bytes = Arrays.copyOfRange(bytes, 0, ArrayUtils.indexOf(bytes, SPLIT2));
		return ConvertUtils.bytesToString(bytes);
	}

	/**
	 * 暗号化キーなしでbyte配列をAES暗号化
	 * @param target 平文
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(byte[] target) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(EncryptAlgorithm.AES, target, null, null);
	}

	/**
	 * 暗号化キーありでbyte配列をAES暗号化
	 * @param target 平文
	 * @param encryptKey 暗号化キー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(byte[] target, byte[] encryptKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(EncryptAlgorithm.AES, target, encryptKey, null);
	}

	/**
	 * 暗号化キーありでbyte配列をAES暗号化
	 * @param target 平文
	 * @param encryptKey 暗号化キー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(byte[] target, String encryptKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(EncryptAlgorithm.AES, target, ConvertUtils.stringToBytes(encryptKey), null);
	}

	/**
	 * すべての暗号化キーを指定して、byte配列をAES暗号化
	 * @param target 平文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(byte[] target, byte[] encryptKey, byte[] ivKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(EncryptAlgorithm.AES, target, encryptKey, ivKey);
	}

	/**
	 * すべての暗号化キーを指定して、byte配列をAES暗号化
	 * @param target 平文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(byte[] target, String encryptKey, String ivKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(EncryptAlgorithm.AES, target, ConvertUtils.stringToBytes(encryptKey), ConvertUtils.stringToBytes(ivKey));
	}

	/**
	 * 暗号化キーなしでbyte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 平文
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(EncryptAlgorithm algorithm, byte[] target) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(algorithm, target, null, null);
	}

	/**
	 * 暗号化キーありでbyte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 平文
	 * @param encryptKey 暗号化キー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(EncryptAlgorithm algorithm, byte[] target, byte[] encryptKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(algorithm, target, encryptKey, null);
	}

	/**
	 * 暗号化キーありでbyte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 平文
	 * @param encryptKey 暗号化キー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(EncryptAlgorithm algorithm, byte[] target, String encryptKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(algorithm, target, ConvertUtils.stringToBytes(encryptKey), null);
	}

	/**
	 * すべての暗号化キーを指定して、byte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 平文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(EncryptAlgorithm algorithm, byte[] target, byte[] encryptKey, byte[] ivKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(algorithm, target, encryptKey, ivKey);
	}

	/**
	 * すべての暗号化キーを指定して、byte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 平文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doEncrypt(EncryptAlgorithm algorithm, byte[] target, String encryptKey, String ivKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return manageEncrypt(algorithm, target, ConvertUtils.stringToBytes(encryptKey), ConvertUtils.stringToBytes(ivKey));
	}

	/**
	 * 暗号化キーなしでbyte配列をAES暗号化
	 * @param target 暗号文
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(byte[] target) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(EncryptAlgorithm.AES, target, null, null);
	}

	/**
	 * 暗号化キーありでbyte配列をAES暗号化
	 * @param target 暗号文
	 * @param encryptKey 暗号化キー
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(byte[] target, byte[] encryptKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(EncryptAlgorithm.AES, target, encryptKey, null);
	}

	/**
	 * 暗号化キーありでbyte配列をAES暗号化
	 * @param target 暗号文
	 * @param encryptKey 暗号化キー
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(byte[] target, String encryptKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(EncryptAlgorithm.AES, target, ConvertUtils.stringToBytes(encryptKey), null);
	}

	/**
	 * すべての暗号化キーを指定して、byte配列をAES暗号化
	 * @param target 暗号文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(byte[] target, byte[] encryptKey, byte[] ivKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(EncryptAlgorithm.AES, target, encryptKey, ivKey);
	}

	/**
	 * すべての暗号化キーを指定して、byte配列をAES暗号化
	 * @param target 暗号文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(byte[] target, String encryptKey, String ivKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(EncryptAlgorithm.AES, target, ConvertUtils.stringToBytes(encryptKey), ConvertUtils.stringToBytes(ivKey));
	}

	/**
	 * 暗号化キーなしでbyte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 暗号文
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(EncryptAlgorithm algorithm, byte[] target) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(algorithm, target, null, null);
	}

	/**
	 * 暗号化キーありでbyte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 暗号文
	 * @param encryptKey 暗号化キー
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(EncryptAlgorithm algorithm, byte[] target, byte[] encryptKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(algorithm, target, encryptKey, null);
	}

	/**
	 * 暗号化キーありでbyte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 暗号文
	 * @param encryptKey 暗号化キー
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(EncryptAlgorithm algorithm, byte[] target, String encryptKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(algorithm, target, ConvertUtils.stringToBytes(encryptKey), null);
	}

	/**
	 * すべての暗号化キーを指定して、byte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 暗号文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(EncryptAlgorithm algorithm, byte[] target, byte[] encryptKey, byte[] ivKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(algorithm, target, encryptKey, ivKey);
	}

	/**
	 * すべての暗号化キーを指定して、byte配列を指定の暗号方式で暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 暗号文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	public static byte[] doDecrypt(EncryptAlgorithm algorithm, byte[] target, String encryptKey, String ivKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return manageDecrypt(algorithm, target, ConvertUtils.stringToBytes(encryptKey), ConvertUtils.stringToBytes(ivKey));
	}



	/**
	 * 暗号化を管理
	 * @param algorithm 暗号アルゴリズム
	 * @param target 平文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @param saveKey 暗号化キーを暗号文字列に含むか否か
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	private static byte[] manageEncrypt(EncryptAlgorithm algorithm, byte[] target, byte[] encryptKey, byte[] ivKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// 準備
		EncryptUtils.EncryptBean encryptBean = new EncryptUtils().new EncryptBean();
		encryptBean.setSaveEncryptKey(encryptKey == null);
		encryptBean.setSaveIvKey(ivKey == null);
		if (encryptBean.isSaveEncryptKey()) {
			encryptBean.setEncryptKey(secureRandomBytes(AUTO_KEY_LENGTH));
		} else {
			encryptBean.setEncryptKey(encryptKey);
		}
		if (encryptBean.isSaveIvKey()) {
			encryptBean.setIvKey(secureRandomBytes(AUTO_KEY_LENGTH));
		} else {
			encryptBean.setIvKey(ivKey);
		}
		// 暗号化
		byte[] encryptBytes = encrypt(algorithm, target, encryptBean);
		// 独自暗号化して返却
		return doOriginal(encryptBytes, encryptBean);
	}

	/**
	 * 暗号化
	 * @param algorithm 暗号アルゴリズム
	 * @param target 平文
	 * @param encKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	private static byte[] encrypt(EncryptAlgorithm algorithm, byte[] target, EncryptBean encryptBean) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (target == null) {
			return new byte[0];
		}
		if (encryptBean.getEncryptKey() == null || encryptBean.getIvKey() == null) {
			throw new NullPointerException();
		}
		SecretKeySpec secretKeySpec = new SecretKeySpec(DigestUtils.sha256(encryptBean.getEncryptKey()), algorithm.getEncrypt());
		IvParameterSpec ivParameterSpec = new IvParameterSpec(DigestUtils.md5(encryptBean.getIvKey()));
		Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		return cipher.doFinal(target);
	}

	/**
	 * 独自にbyte配列を改ざん
	 * 初期化ベクトルキー、暗号文、暗号化キー、の順で連結し返却
	 * Base64を使用し、初期化ベクトルキー、暗号文、暗号化キーはエンコードをかける（Base64は英数字と「/」「+」）
	 * 各3つの間をBase64では使用しない「-」「_」で区切る
	 * 最終的にすべてのキーを保存する場合は、初期化ベクトルキー_暗号文-暗号化キーとなる
	 * byteを+3ずらす
	 * @param encryptBytes 暗号化
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @param saveKey 暗号化キーを暗号文字列に含むか否か
	 * @return 独自改竄byte配列
	 * @throws EP0102EncryptKeyNoneException 暗号化キーがない場合に発生
	 */
	private static byte[] doOriginal(byte[] encryptBytes, EncryptBean encryptBean) {
		if (encryptBytes == null) {
			return new byte[0];
		}
		byte[] original;
		if (encryptBean.isSaveEncryptKey()) {
			if (encryptBean.isSaveIvKey()) {
				// 初期化ベクトルキー + 区切り文字1
				original = ArrayUtils.addAll(Base64.getEncoder().encode(ArrayUtils.reverse(encryptBean.getIvKey())), SPLIT1);
				// 初期化ベクトルキー + 区切り文字1 + 暗号文
				original = ArrayUtils.addAll(original, Base64.getEncoder().encode(ArrayUtils.reverse(encryptBytes)));
				// 初期化ベクトルキー + 区切り文字1 + 暗号文 + 区切り文字2
				original = ArrayUtils.addAll(original, SPLIT2);
				// 初期化ベクトルキー + 区切り文字1 + 暗号文 + 区切り文字2 + 暗号化キー
				original = ArrayUtils.addAll(original, Base64.getEncoder().encode(ArrayUtils.reverse(encryptBean.getEncryptKey())));
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			if (encryptBean.isSaveIvKey()) {
				// 初期化ベクトルキー + 区切り文字1
				original = ArrayUtils.addAll(Base64.getEncoder().encode(ArrayUtils.reverse(encryptBean.getIvKey())), SPLIT1);
				// 初期化ベクトルキー + 区切り文字1 + 暗号文
				original = ArrayUtils.addAll(original, Base64.getEncoder().encode(ArrayUtils.reverse(encryptBytes)));
			} else {
				// 暗号文のみ
				original = Base64.getEncoder().encode(ArrayUtils.reverse(encryptBytes));
			}
		}
		// byteをずらす(ずらし分 + インデックス / 3)
		for (int i = 0; i < original.length; i++) {
			original[i] = (byte) (original[i] + SHIFT_BYTE + i / 3);
		}
		// Base64エンコードして返却
		return Base64.getEncoder().encode(original);
	}

	/**
	 * 復号を管理
	 * @param algorithm 暗号アルゴリズム
	 * @param encrypt 暗号文
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @param saveKey 暗号化キーを暗号文字列に含むか否か
	 * @return 平文byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	private static byte[] manageDecrypt(EncryptAlgorithm algorithm, byte[] encrypt, byte[] encryptKey, byte[] ivKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// 独自復号
		DecryptBean decryptBean = undoOriginal(encrypt, encryptKey, ivKey);
		// 復号して返却
		return decrypt(algorithm, decryptBean.getDecrypt(), decryptBean.getEncryptKey(), decryptBean.getIvKey());
	}

	/**
	 * 独自に改竄したbyte配列を戻
	 * 初期化ベクトルキー、暗号文字列、暗号化キー、の順で連結し返却
	 * @param encryptBytes 暗号化
	 * @param encryptKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @param saveKey 暗号化キーを暗号文字列に含むか否か
	 * @return 独自改竄byte配列
	 * @throws EP0102EncryptKeyNoneException 暗号化キーがない場合に発生
	 */
	private static DecryptBean undoOriginal(byte[] encryptBytes, byte[] encryptKey, byte[] ivKey) {
		EncryptUtils.DecryptBean decryptBean = new EncryptUtils().new DecryptBean();
		// Base64デコード
		byte[] original = Base64.getDecoder().decode(encryptBytes);
		// byteを戻す(ずらし分 インデックス / 3)
		for (int i = 0; i < original.length; i++) {
			original[i] = (byte) (original[i] - SHIFT_BYTE - i / 3);
		}
		if (encryptKey == null) {
			if (ivKey == null) {
				// 暗号化キー
				decryptBean.setEncryptKey(ArrayUtils.reverse(Base64.getDecoder().decode(Arrays.copyOfRange(original, ArrayUtils.indexOf(original, SPLIT2) + 1, original.length))));
				// 暗号文
				decryptBean.setDecrypt(ArrayUtils.reverse(Base64.getDecoder().decode(Arrays.copyOfRange(original, ArrayUtils.indexOf(original, SPLIT1) + 1, ArrayUtils.indexOf(original, SPLIT2)))));
				// IV
				decryptBean.setIvKey(ArrayUtils.reverse(Base64.getDecoder().decode(Arrays.copyOfRange(original, 0, ArrayUtils.indexOf(original, SPLIT1)))));
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			if (ivKey == null) {
				// 暗号化キー
				decryptBean.setEncryptKey(encryptKey);
				// 暗号文
				decryptBean.setDecrypt(ArrayUtils.reverse(Base64.getDecoder().decode(Arrays.copyOfRange(original, ArrayUtils.indexOf(original, SPLIT1) + 1, original.length))));
				// IV
				decryptBean.setIvKey(ArrayUtils.reverse(Base64.getDecoder().decode(Arrays.copyOfRange(original, 0, ArrayUtils.indexOf(original, SPLIT1)))));
			} else {
				decryptBean.setDecrypt(ArrayUtils.reverse(Base64.getDecoder().decode(original)));
				decryptBean.setEncryptKey(encryptKey);
				decryptBean.setIvKey(ivKey);
			}
		}
		return decryptBean;
	}

	/**
	 * 復号
	 * @param algorithm 暗号アルゴリズム
	 * @param encrypt 暗号文
	 * @param encKey 暗号化キー
	 * @param ivKey 初期化ベクトルキー
	 * @return 暗号化byte配列
	 * @throws InvalidAlgorithmParameterException 無効または不適切なアルゴリズム・パラメータの場合に発生
	 * @throws InvalidKeyException 無効な符号化、長さの誤り、未初期化などの無効なキーの場合に発生
	 * @throws NoSuchPaddingException あるパディング・メカニズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws NoSuchAlgorithmException ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合に発生
	 * @throws BadPaddingException 特定のパディング・メカニズムが入力データに対して予期されているのにデータが適切にパディングされない場合に発生
	 * @throws IllegalBlockSizeException ブロック暗号に提供されたデータの長さが正しくない場合、つまり暗号のブロック・サイズと一致しない場合に発生
	 */
	private static byte[] decrypt(EncryptAlgorithm algorithm, byte[] encrypt, byte[] encKey, byte[] ivKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		if (encrypt == null) {
			return new byte[0];
		}
		if (encKey == null || ivKey == null) {
			throw new NullPointerException();
		}
		SecretKeySpec secretKeySpec = new SecretKeySpec(DigestUtils.sha256(encKey), algorithm.getEncrypt());
		IvParameterSpec ivParameterSpec = new IvParameterSpec(DigestUtils.md5(ivKey));
		Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		return cipher.doFinal(encrypt);
	}

	/**
	 * 暗号アルゴリズム
	 * @author ink-0x20
	 *
	 */
	public enum EncryptAlgorithm {
		/** AES/CBC/PKCS5Padding */
		  AES("AES", "AES/CBC/PKCS5Padding")
		/** RSA/ECB/PKCS1Padding */
		, RSA("RSA", "RSA/ECB/PKCS1Padding")
		;

		private String encrypt;
		private String algorithm;

		/**
		 * デフォルトコンストラクタ
		 * @param encrypt 暗号の種類
		 * @param algorithm 暗号アルゴリズム
		 */
		private EncryptAlgorithm(String encrypt, String algorithm) {
			this.encrypt = encrypt;
			this.algorithm = algorithm;
		}

		/**
		 * 暗号の種類を取得
		 * @return 暗号の種類
		 */
		public String getEncrypt() {
			return this.encrypt;
		}

		/**
		 * 暗号アルゴリズムを取得
		 * @return 暗号アルゴリズム
		 */
		public String getAlgorithm() {
			return this.algorithm;
		}

	}

	/**
	 * 暗号化時のデータ
	 * @author ink-0x20
	 */
	private class EncryptBean {
		private byte[] encryptKey;
		private byte[] ivKey;
		private boolean saveEncryptKey;
		private boolean saveIvKey;

		/**
		 * 暗号化キー取得
		 * @return 暗号化キー
		 */
		public byte[] getEncryptKey() {
			return this.encryptKey;
		}

		/**
		 * 暗号化キー設定
		 * @param encryptKey 暗号化キー
		 */
		public void setEncryptKey(byte[] encryptKey) {
			this.encryptKey = encryptKey;
		}

		/**
		 * IVキー取得
		 * @return IVキー
		 */
		public byte[] getIvKey() {
			return this.ivKey;
		}

		/**
		 * IVキー設定
		 * @param ivKey IVキー
		 */
		public void setIvKey(byte[] ivKey) {
			this.ivKey = ivKey;
		}

		/**
		 * 暗号化キーを保存するかのフラグ取得
		 * @return 保存フラグ
		 */
		public boolean isSaveEncryptKey() {
			return this.saveEncryptKey;
		}

		/*
		 * 暗号化キーを保存するか設定
		 */
		public void setSaveEncryptKey(boolean saveEncryptKey) {
			this.saveEncryptKey = saveEncryptKey;
		}

		/**
		 * IVキーを保存するかのフラグ取得
		 * @return 保存フラグ
		 */
		public boolean isSaveIvKey() {
			return this.saveIvKey;
		}

		/*
		 * IVキーを保存するか設定
		 */
		public void setSaveIvKey(boolean saveIvKey) {
			this.saveIvKey = saveIvKey;
		}
	}

	/**
	 * 復号時のデータ
	 * @author ink-0x20
	 */
	private class DecryptBean {
		private byte[] decrypt;
		private byte[] encryptKey;
		private byte[] ivKey;

		/**
		 * 復号データ取得
		 * @return 復号データ
		 */
		public byte[] getDecrypt() {
			return this.decrypt;
		}

		/**
		 * 復号データ設定
		 * @param decrypt 復号データ
		 */
		public void setDecrypt(byte[] decrypt) {
			this.decrypt = decrypt;
		}

		/**
		 * 暗号化キー取得
		 * @return 暗号化キー
		 */
		public byte[] getEncryptKey() {
			return this.encryptKey;
		}

		/**
		 * 暗号化キー設定
		 * @param encryptKey 暗号化キー
		 */
		public void setEncryptKey(byte[] encryptKey) {
			this.encryptKey = encryptKey;
		}

		/**
		 * IVキー取得
		 * @return IVキー
		 */
		public byte[] getIvKey() {
			return this.ivKey;
		}

		/**
		 * IVキー設定
		 * @param ivKey IVキー
		 */
		public void setIvKey(byte[] ivKey) {
			this.ivKey = ivKey;
		}
	}
}
