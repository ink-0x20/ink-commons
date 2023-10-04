package com.inkblogdb.commons.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author ink-0x20
 */
public class CompressUtils {

	/** バッファ用配列 */
	private static final byte[] BUFFER = new byte[1024];
	/** 圧縮オブジェクト */
	private static final Deflater DEFLATER = new Deflater();
	/** 圧縮解除オブジェクト */
	private static final Inflater INFLATER = new Inflater();

	/**
	 * 最大圧縮率で圧縮
	 * @param target 圧縮対象
	 * @return 圧縮済みデータ
	 * @throws IOException
	 */
	public static byte[] compressMax(final byte[] target) throws IOException {
		return compress(target, Deflater.BEST_COMPRESSION);
	}

	/**
	 * 最大圧縮率で圧縮
	 * @param target 圧縮対象
	 * @param level 圧縮率
	 * @return 圧縮済みデータ
	 * @throws IOException
	 */
	public static byte[] compress(final byte[] target, final int level) throws IOException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			try {
				DEFLATER.setLevel(level);
				DEFLATER.setInput(target);
				DEFLATER.finish();
				while (!DEFLATER.finished()) {
					byteArrayOutputStream.write(BUFFER, 0, DEFLATER.deflate(BUFFER));
				}
			} finally {
				DEFLATER.reset();
			}
			return byteArrayOutputStream.toByteArray();
		}
	}

	/**
	 * 圧縮解除
	 * @param target 圧縮解除対象
	 * @return 圧縮解除済みデータ
	 * @throws DataFormatException
	 * @throws IOException
	 */
	public static byte[] uncompress(final byte[] target) throws DataFormatException, IOException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			try {
				INFLATER.setInput(target);
				while (!INFLATER.finished()) {
					byteArrayOutputStream.write(BUFFER, 0, INFLATER.inflate(BUFFER));
				}
			} finally {
				INFLATER.reset();
			}
			return byteArrayOutputStream.toByteArray();
		}
	}

}
