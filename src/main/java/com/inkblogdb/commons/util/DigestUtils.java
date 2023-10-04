package com.inkblogdb.commons.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ink-0x20
 */
public class DigestUtils {

	/** MD5アルゴリズム */
	public static final String MD5 = "MD5";

	/**SHA-256アルゴリズム */
	public static final String SHA_256 = "SHA-256";
	/**SHA-512アルゴリズム */
	public static final String SHA_512 = "SHA-512";

	/**SHA3-256アルゴリズム */
	public static final String SHA3_256 = "SHA3-256";
	/**SHA3-512アルゴリズム */
	public static final String SHA3_512 = "SHA3-512";

	/**
	 * MD5でハッシュ
	 *
	 * @param data データ
	 * @return MD5 ハッシュ値
	 * @throws IllegalArgumentException
	 */
	public static byte[] md5(final byte[] data) throws IllegalArgumentException {
		return digest(data, MD5);
	}

	/**
	 * SHA-256でハッシュ
	 *
	 * @param data データ
	 * @return SHA-256 ハッシュ値
	 * @throws IllegalArgumentException
	 */
	public static byte[] sha256(final byte[] data) throws IllegalArgumentException {
		return digest(data, SHA_256);
	}

	/**
	 * SHA-512でハッシュ
	 *
	 * @param data データ
	 * @return SHA-512 ハッシュ値
	 * @throws IllegalArgumentException
	 */
	public static byte[] sha512(final byte[] data) throws IllegalArgumentException {
		return digest(data, SHA_256);
	}

	/**
	 * SHA3-256でハッシュ
	 *
	 * @param data データ
	 * @return SHA3-256 ハッシュ値
	 * @throws IllegalArgumentException
	 */
	public static byte[] sha3_256(final byte[] data) throws IllegalArgumentException {
		return digest(data, SHA3_256);
	}

	/**
	 * SHA3-512でハッシュ
	 *
	 * @param data データ
	 * @return SHA3-512 ハッシュ値
	 * @throws IllegalArgumentException
	 */
	public static byte[] sha3_512(final byte[] data) throws IllegalArgumentException {
		return digest(data, SHA3_256);
	}

	/**
	 * 指定アルゴリズムでハッシュ
	 *
	 * @param data データ
	 * @param algorithm アルゴリズム
	 * @return ハッシュ値
	 * @throws IllegalArgumentException
	 */
	public static byte[] digest(final byte[] data, final String algorithm) throws IllegalArgumentException {
		return getDigest(algorithm).digest(data);
	}

	/**
	 * 指定したMessageDigestインスタンスを取得
	 *
	 * @param algorithm アルゴリズム
	 * @return MessageDigestインスタンス
	 * @throws IllegalArgumentException
	 */
	public static MessageDigest getDigest(final String algorithm) throws IllegalArgumentException {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
