/*
 * Copyright 2014-2015 itas group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.itas.xcnet.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;

import org.itas.xcnet.common.utils.IOUtils;

/**
 * codec util
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月14日上午11:41:12
 */
public final class Bytes {

	private static final String C64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	
	private static final char[] BASE16 = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	
	private static final char[] BASE64 = C64.toCharArray();
	
	private static final Map<Integer, byte[]> DECODE_TABLE_MAP = new ConcurrentHashMap<>(); 
	
	private static final ThreadLocal<MessageDigest> MD = new ThreadLocal<MessageDigest>() {
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		};
	};
	
	/**
	 * byte array copy
	 * 
	 * @param src src byte array
	 * @param len copy byte lenth
	 * @return new byte array
	 */
	public static byte[] copyOf(byte[] src, int len) {
		byte[] bs = new byte[len];
		System.arraycopy(src, 0, bs, 0, Math.min(src.length, len));
		return bs;
	}
	
	/**
	 * to byte array
	 * 
	 * @param vshort value
	 * @return byte array
	 */
	public static byte[] short2bytes(short v) {
		byte[] ret = new byte[2];
		short2bytes(v, ret);
		return ret;
	}
	
	/**
	 * to byte array
	 * 
	 * @param v short value
	 * @param b byte array
	 */
	public static void short2bytes(short v, byte[] b) {
		short2bytes(v, b, 0);
	}
	
	/**
	 * to byte array
	 * 
	 * @param v short value
	 * @param b byte array
	 * @param off begin index
	 */
	public static void short2bytes(short v, byte[] b, int off) {
		b[off + 1] = (byte)(v >>> 0);
		b[off + 0] = (byte)(v >>> 8);
	}
	
	/**
	 * to byte array
	 * 
	 * @param v int value
	 * @return byte array
	 */
	public static byte[] int2bytes(int v) {
		byte[] ret = new byte[4];
		int2bytes(v, ret);
		return ret;
	}
	
	/**
	 * to byte array
	 * 
	 * @param v int value
	 * @param b byte array
	 */
	public static void int2bytes(int v, byte[] b) {
		int2bytes(v, b, 0);
	}
	
	/**
	 * to byte array
	 * 
	 * @param v int value
	 * @param b byte array
	 * @param off begin index
	 */
	public static void int2bytes(int v, byte[] b, int off) {
		b[off + 3] = (byte)(v >>> 0);
		b[off + 2] = (byte)(v >>> 8);
		b[off + 1] = (byte)(v >>> 16);
		b[off + 0] = (byte)(v >>> 24);
	}
	
	/**
	 * to byte array
	 * 
	 * @param v long value
	 * @return byte array
	 */
	public static byte[] long2bytes(long v) {
		byte[] ret = new byte[8];
		long2bytes(v, ret);
		return ret;
	}
	
	/**
	 * to byte array
	 * 
	 * @param v long value
	 * @param b byte array
	 */
	public static void long2bytes(long v, byte[] b) {
		long2bytes(v, b, 0);
	}
	
	/**
	 * to byte array
	 * 
	 * @param v long value
	 * @param b byte array
	 * @param off begin index
	 */
	public static void long2bytes(long v, byte[] b, int off) {
		b[off + 7] = (byte)(v >>> 0);
		b[off + 6] = (byte)(v >>> 8);
		b[off + 5] = (byte)(v >>> 16);
		b[off + 4] = (byte)(v >>> 24);
		b[off + 3] = (byte)(v >>> 32);
		b[off + 2] = (byte)(v >>> 40);
		b[off + 1] = (byte)(v >>> 48);
		b[off + 0] = (byte)(v >>> 56);
	}
	
	/**
	 * to byte array
	 * 
	 * @param v float value
	 * @return byte array
	 */
	public static byte[] float2bytes(float v) {
		byte[] ret = new byte[4];
		float2bytes(v, ret);
		return ret;
	}
	
	/**
	 * to byte array
	 * 
	 * @param v float value
	 * @param b byte array
	 */
	public static void float2bytes(float v, byte[] b) {
		float2bytes(v, b, 0);
	}
	
	/**
	 * to byte array
	 * 
	 * @param v float value
	 * @param b byte array
	 * @param off begin index
	 */
	public static void float2bytes(float v, byte[] b, int off) {
		int i = Float.floatToIntBits(v);
		b[off + 3] = (byte)(i >>> 0);
		b[off + 2] = (byte)(i >>> 8);
		b[off + 1] = (byte)(i >>> 16);
		b[off + 0] = (byte)(i >>> 24);
	}
	
	/**
	 * to byte array
	 * 
	 * @param v double value
	 * @return byte array
	 */
	public static byte[] double2bytes(double v) {
		byte[] ret = new byte[8];
		double2bytes(v, ret);
		return ret;
	}
	
	/**
	 * to byte array
	 * 
	 * @param v double value
	 * @param b byte array
	 */
	public static void double2bytes(double v, byte[] b) {
		double2bytes(v, b, 0);
	}
	
	/**
	 * to byte array
	 * 
	 * @param v double value
	 * @param b byte array
	 * @param off begin index
	 */
	public static void double2bytes(double v, byte[] b, int off) {
		long i = Double.doubleToLongBits(v);
		b[off + 7] = (byte)(i >>> 0);
		b[off + 6] = (byte)(i >>> 8);
		b[off + 5] = (byte)(i >>> 16);
		b[off + 4] = (byte)(i >>> 24);
		b[off + 3] = (byte)(i >>> 32);
		b[off + 2] = (byte)(i >>> 40);
		b[off + 1] = (byte)(i >>> 48);
		b[off + 0] = (byte)(i >>> 56);
	}
	
	/**
	 * to short
	 * 
	 * @param b byte array
	 * @return short
	 */
	public static short bytes2short(byte[] b) {
		return bytes2short(b, 0);
	}
	
	/**
	 * to short
	 * 
	 * @param b byte array
	 * @param off offset
	 * @return short
	 */
	public static short bytes2short(byte[] b, int off) {
		int value = 0;
		value += (b[off + 1] & 0xFF) << 0;
		value += (b[off + 0] & 0xFF) << 8;
		return (short)value;
	}	
	
	/**
	 * to int
	 * 
	 * @param b byte array
	 * @return int
	 */
	public static int bytes2int(byte[] b) {
		return bytes2int(b, 0);
	}
	
	/**
	 * to int
	 * 
	 * @param b byte array
	 * @param off offset
	 * @return int
	 */
	public static int bytes2int(byte[] b, int off) {
		int value = 0;
		value += (b[off + 3] & 0xFF) << 0;
		value += (b[off + 2] & 0xFF) << 8;
		value += (b[off + 1] & 0xFF) << 16;
		value += (b[off + 0] & 0xFF) << 24;
		return value;
	}	
	
	/**
	 * to long
	 * 
	 * @param b byte array
	 * @return long
	 */
	public static long bytes2long(byte[] b) {
		return bytes2long(b, 0);
	}
	
	/**
	 * to long
	 * 
	 * @param b byte array
	 * @param off offset
	 * @return long
	 */
	public static long bytes2long(byte[] b, int off) {
		long value = 0;
		value += (((long)b[off + 7]) & 0xFFL) << 0;
		value += (((long)b[off + 6]) & 0xFFL) << 8;
		value += (((long)b[off + 5]) & 0xFFL) << 16;
		value += (((long)b[off + 4]) & 0xFFL) << 24;
		value += (((long)b[off + 3]) & 0xFFL) << 32;
		value += (((long)b[off + 2]) & 0xFFL) << 40;
		value += (((long)b[off + 1]) & 0xFFL) << 48;
		value += (((long)b[off + 0]) & 0xFFL) << 56;
		return value;
	}	
	
	/**
	 * to float
	 * 
	 * @param b byte array
	 * @return float
	 */
	public static float bytes2float(byte[] b) {
		return bytes2float(b, 0);
	}
	
	/**
	 * to float
	 * 
	 * @param b byte array
	 * @param off offset
	 * @return float
	 */
	public static float bytes2float(byte[] b, int off) {
		int value = 0;
		value += (b[off + 3] & 0xFFL) << 0;
		value += (b[off + 2] & 0xFFL) << 8;
		value += (b[off + 1] & 0xFFL) << 16;
		value += (b[off + 0] & 0xFFL) << 24;
		return Float.intBitsToFloat(value);
	}	
	/**
	 * to double
	 * 
	 * @param b byte array
	 * @return double
	 */
	public static double bytes2double(byte[] b) {
		return bytes2double(b, 0);
	}
	
	/**
	 * to long
	 * 
	 * @param b byte array
	 * @param off offset
	 * @return long
	 */
	public static double bytes2double(byte[] b, int off) {
		long value = 0;
		value += (((long)b[off + 7]) & 0xFFL) << 0;
		value += (((long)b[off + 6]) & 0xFFL) << 8;
		value += (((long)b[off + 5]) & 0xFFL) << 16;
		value += (((long)b[off + 4]) & 0xFFL) << 24;
		value += (((long)b[off + 3]) & 0xFFL) << 32;
		value += (((long)b[off + 2]) & 0xFFL) << 40;
		value += (((long)b[off + 1]) & 0xFFL) << 48;
		value += (((long)b[off + 0]) & 0xFFL) << 56;
		return Double.longBitsToDouble(value);
	}	
	
	/**
	 * to hex string
	 * 
	 * @param bs byte array
	 * @return hex string
	 */
	public static String bytes2hex(byte[] bs) {
		return bytes2hex(bs, 0, bs.length);
	}
	
	/**
	 * to hex string
	 * 
	 * @param bs byte array
	 * @param off offset
	 * @param len length
	 * @return hex string
	 */
	public static String bytes2hex(byte[] bs, int off, int len) {
		if (off < 0) 
			throw new IndexOutOfBoundsException("bytes2hex: offset < 0, offset is " + off );
		if (len < 0) 
			throw new IndexOutOfBoundsException("bytes2hex: length < 0, length is " + len );
		if (off + len > bs.length) 
			throw new IndexOutOfBoundsException("bytes2hex: offset + length > array length.");
		
		int index = 0;
		// max byte value 0xFF
		char[] chs = new char[len*2];
		for (int i = 0 ; i < len; i ++) {
			byte b = bs[off + i];
			chs[index ++] = BASE16[(b >> 4) & 0xF];
			chs[index ++] = BASE16[(b >> 0) & 0xF];
		}
		
		return new String(chs);
	}
	
	/**
	 * from hex string
	 * 
	 * @param str hex string
	 * @return byte array
	 */
	public static byte[] hex2bytes(String str) {
		return hex2bytes(str, 0, str.length());
	}
	
	/**
	 * from hex string
	 * 
	 * @param str hex string
	 * @param off offset
	 * @param len length
	 * @return byte array
	 */
	public static byte[] hex2bytes(String str, int off, int len) {
		if ((len & 1) == 1) 
			throw new IllegalArgumentException("hex2bytes: ( len & 1 ) == 1.");
		if (off < 0) 
			throw new IndexOutOfBoundsException("hex2bytes: offset < 0, offset is " + off );
		if(len < 0) 
			throw new IndexOutOfBoundsException("hex2bytes: length < 0, length is " + len );
		if ((off + len) > str.length()) 
			throw new IndexOutOfBoundsException("hex2bytes: offset + length > array length.");
		
		int index = off;
		byte[] bs = new byte[len/2];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte)(hex(str.charAt(index ++)) << 4 | hex(str.charAt(index ++)));
		}
		return bs;
	}
	
	/**
	 * to base64 string.
	 * 
	 * @param b byte array.
	 * @return base64 string.
	 */
	public static String bytes2base64(byte[] b) {
		return bytes2base64(b, 0, b.length, BASE64);
	}

	/**
	 * to base64 string.
	 * 
	 * @param b byte array.
	 * @return base64 string.
	 */
	public static String bytes2base64(byte[] b, int offset, int length) {
		return bytes2base64(b, offset, length, BASE64);
	}

	/**
	 * to base64 string.
	 * 
	 * @param b byte array.
	 * @param code base64 code string(0-63 is base64 char,64 is pad char).
	 * @return base64 string.
	 */
	public static String bytes2base64(byte[] b, String code) {
		return bytes2base64(b, 0, b.length, code);
	}

	/**
	 * to base64 string.
	 * 
	 * @param b byte array.
	 * @param code base64 code string(0-63 is base64 char,64 is pad char).
	 * @return base64 string.
	 */
	public static String bytes2base64(byte[] b, int offset, int length, String code) {
		if( code.length() < 64 )
			throw new IllegalArgumentException("Base64 code length < 64.");

		return bytes2base64(b, offset, length, code.toCharArray());
	}

	/**
	 * to base64 string.
	 * 
	 * @param b byte array.
	 * @param code base64 code(0-63 is base64 char,64 is pad char).
	 * @return base64 string.
	 */
	public static String bytes2base64(byte[] b, char[] code) {
		return bytes2base64(b, 0, b.length, code);
	}
	
	/**
	 * to base64 string
	 * 
	 * @param bs byte array
	 * @param off offset
	 * @param len length
	 * @param code base64 code(0-63 is base64 char,64 is pad char).
	 * @return base64 string
	 */
	public static String bytes2base64(byte[] bs, int off, int len, char[] code) {
		if (off < 0) 
			throw new IndexOutOfBoundsException("bytes2base64: offset < 0, offset is " + off );
		if (len < 0) 
			throw new IndexOutOfBoundsException("bytes2base64: length < 0, length is " + len );
		if (off + len > bs.length) 
			throw new IndexOutOfBoundsException("bytes2base64: offset + length > array length.");
		if (code.length < 64) 
			throw new IllegalArgumentException("Base64 code length < 64.");
		
		final int MASK6 = 0x3F;//0B00111111
		boolean pad = code.length > 64; //has pad char
		int num = len / 3, rem = len % 3, r = off, w = 0;
		char[] cs = new char[num * 4 + (rem == 0 ? 0 : pad ? 4 : rem + 1)];
		for (int i = 0; i < num; i++) {
			int b1 = bs[r ++] & 0xFF;
			int b2 = bs[r ++] & 0xFF;
			int b3 = bs[r ++] & 0xFF;
			
			cs[w ++] = code[b1 >> 2]; 
			cs[w ++] = code[(b1 << 4) & MASK6 | (b2 >> 4)];
			cs[w ++] = code[(b2 << 2) & MASK6 | (b3 >> 6)];
			cs[w ++] = code[b3 & MASK6];
		}
		
		if (rem == 1) {
			int b1 = bs[r++] & 0xFF;
			cs[w++] = code[b1 >> 2];
			cs[w++] = code[(b1 << 4) & MASK6 ];
			if (pad) {
				cs[w++] = code[64];
				cs[w++] = code[64];
			}
		} else if (rem == 2) {
			int b1 = bs[r++] & 0xFF;
			int b2 = bs[r++] & 0xFF;
			cs[w++] = code[b1 >> 2];
			cs[w++] = code[(b1 << 4) & MASK6 | (b2 >> 4)];
			cs[w++] = code[(b2 << 2) & MASK6];
			if (pad) {
				cs[w++] = code[64];
			}
		}
		
		return new String(cs);
	}
	
	/**
	 * from base64 string.
	 * 
	 * @param str base64 string.
	 * @return byte array.
	 */
	public static byte[] base642bytes(String str) {
		return base642bytes(str, 0, str.length());
	}

	/**
	 * from base64 string.
	 * 
	 * @param str base64 string.
	 * @param offset offset.
	 * @param length length.
	 * @return byte array.
	 */
	public static byte[] base642bytes(String str, int offset, int length) {
		return base642bytes(str, offset, length, C64);
	}

	/**
	 * from base64 string.
	 * 
	 * @param str base64 string.
	 * @param code base64 code(0-63 is base64 char,64 is pad char).
	 * @return byte array.
	 */
	public static byte[] base642bytes(String str, String code) {
		return base642bytes(str, 0, str.length(), code);
	}
	
	/**
	 * from base64 string.
	 * 
	 * @param str base64 string.
	 * @param off offset.
	 * @param len length.
	 * @param code base64 code(0-63 is base64 char,64 is pad char).
	 * @return byte array.
	 */
	public static byte[] base642bytes(String str, int off, int len, String code) {
		if (off < 0) 
			throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off );
		if (len < 0) 
			throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len );
		if ((off + len) > str.length()) 
			throw new IllegalArgumentException("Base64 code length < 64.");
		if( code.length() < 64 )
			throw new IllegalArgumentException("Base64 code length < 64.");
		
		int rem = len % 4;
		if (rem == 1)
			throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");
		
		int num = len / 4, size = num * 3;
		if (code.length() > 64) {
			if (rem != 0) 
				throw new IllegalArgumentException("base642bytes: base64 string length error.");
			
			char pc = code.charAt(64);
			if (str.charAt(off + len - 2) == pc) {
				size -= 2;
				--num;
				rem = 2;
			} else if (str.charAt(off + len - 1) == pc) {
				size --;
				--num;
				rem = 3;
			}
		} else {
			if(rem == 2)
				size ++;
			else if(rem == 3)
				size += 2;
		}
		byte[] t = decodeTable(code);
		
		int r = off, w =0;
		byte[] b = new byte[size];
		for (int i = 0; i < num; i++) {
			int c1 = t[str.charAt(r ++)];
			int c2 = t[str.charAt(r ++)];
			int c3 = t[str.charAt(r ++)];
			int c4 = t[str.charAt(r ++)];
			
			b[w ++] = (byte)(c1 << 2 | (c2 >> 4));
			b[w ++] = (byte)(c2 << 4 | (c3 >> 2));
			b[w ++] = (byte)(c3 << 6 | c4);
		}
		
		if (rem == 2) {
			int c1 = t[str.charAt(r++)];
			int c2 = t[str.charAt(r++)];
			
			b[w ++] = (byte)(c1 << 2 | (c2 >> 4));
		} else if (rem == 3) {
			int c1 = t[str.charAt(r++)];
			int c2 = t[str.charAt(r++)];
			int c3 = t[str.charAt(r++)];
			
			b[w ++] = (byte)(c1 << 2 | (c2 >> 4));
			b[w ++] = (byte)(c2 << 4 | (c3 >> 2));
		}
		
		return b;
	}
	
	/**
	 * from base64 string.
	 * 
	 * @param str base64 string.
	 * @param code base64 code(0-63 is base64 char,64 is pad char).
	 * @return byte array.
	 */
	public static byte[] base642bytes(String str, char[] code) {
		return base642bytes(str, 0, str.length(), code);
	}
	
	/**
	 * from base64 string.
	 * 
	 * @param str base64 string.
	 * @param off offset.
	 * @param len length.
	 * @param code base64 code(0-63 is base64 char,64 is pad char).
	 * @return byte array.
	 */
	public static byte[] base642bytes(String str, int off, int len, char[] code) {
		if (off < 0) 
			throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off );
		if (len < 0) 
			throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len );
		if ((off + len) > str.length()) 
			throw new IllegalArgumentException("Base64 code length < 64.");
		if( code.length < 64 )
			throw new IllegalArgumentException("Base64 code length < 64.");

		int rem = len % 4;
		if (rem == 1)
			throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");
		
		int num = len / 4, size = num * 3;
		if (code.length > 64) {
			if (rem != 0) 
				throw new IllegalArgumentException("base642bytes: base64 string length error.");
			
			char pc = code[64];
			if (str.charAt(off + len - 2) == pc) {
				size -= 2;
			} else if (str.charAt(off + len - 1) == pc) {
				size -= 1;
			}
		} else {
			if(rem == 2)
				size += 1;
			else if(rem == 3)
				size += 2;
		}
		
		int r = off, w =0;
		byte[] b = new byte[size];
		for (int i = 0; i < num; i++) {
			int c1 = indexOf(code, str.charAt(r ++));
			int c2 = indexOf(code, str.charAt(r ++));
			int c3 = indexOf(code, str.charAt(r ++));
			int c4 = indexOf(code, str.charAt(r ++));
			
			b[w ++] = (byte)(c1 << 2 | (c2 >> 4));
			b[w ++] = (byte)(c1 << 4 | (c3 >> 2));
			b[w ++] = (byte)(c2 << 6 | c4);
		}
		
		if (rem == 2) {
			int c1 = indexOf(code, str.charAt(r ++));
			int c2 = indexOf(code, str.charAt(r ++));
			
			b[w ++] = (byte)(c1 << 2 | (c2 >> 4));
		} else if (rem == 3) {
			int c1 = indexOf(code, str.charAt(r ++));
			int c2 = indexOf(code, str.charAt(r ++));
			int c3 = indexOf(code, str.charAt(r ++));
			
			b[w ++] = (byte)(c1 << 2 | (c2 >> 4));
			b[w ++] = (byte)(c1 << 4 | (c3 >> 2));
		}
		
		return b;
	}
	
	/**
	 * zip.
	 * 
	 * @param bytes source.
	 * @return compressed byte array.
	 * @throws IOException.
	 */
	public static byte[] zip(byte[] bytes) throws IOException {
		try (UnsafeByteArrayOutputStream uos = new UnsafeByteArrayOutputStream();
			 OutputStream os = new DeflaterOutputStream(uos)) {
			os.write(bytes);
			return uos.toByteArray();
		}
	}
	
	/**
	 * unzip.
	 * 
	 * @param bytes compressed byte array.
	 * @return byte uncompressed array.
	 * @throws IOException
	 */
	public static byte[] unzip(byte[] bytes) throws IOException {
		try (UnsafeByteArrayInputStream uis = new UnsafeByteArrayInputStream(bytes);
			UnsafeByteArrayOutputStream uos = new UnsafeByteArrayOutputStream();
			InputStream is = new DeflaterInputStream(uis)) {
			IOUtils.write(is, uos);
			return uos.toByteArray();
		}
	}
	
	/**
	 * get md5
	 * 
	 * @param str string source
	 * @return MD5 byte array.
	 */
	public static byte[] getMD5(String str) {
		return getMD5(str.getBytes());
	}
	
	/**
	 * get md5
	 * 
	 * @param bs byte array source
	 * @return md5 byte array
	 */
	public static byte[] getMD5(byte[] bs) {
		return MD.get().digest(bs);
	}
	
	/**
	 * get md5
	 * 
	 * @param file file source
	 * @return MD5 byte array.
	 * @throws IOException
	 */
	public static byte[] getMD5(File file) throws IOException {
		try (InputStream is = new FileInputStream(file)) {
			return getMD5(is);
		}
	}
	
	/**
	 * get md5
	 * 
	 * @param is InputStream
	 * @return md5 byte array
	 * @throws IOException
	 */
	public static byte[] getMD5(InputStream is) throws IOException {
		return getMD(is, 1024 * 8);
	}
	
	private static byte hex(char c) {
		if(c <= '9') {
			return (byte)( c - '0' );
		}
		if(c >= 'a' && c <= 'f') {
			return (byte)( c - 'a' + 10 );
		}
		if(c >= 'A' && c <= 'F') {
			return (byte)( c - 'A' + 10 );
		}
		
		throw new IllegalArgumentException("hex string format error [" + c + "].");
	}
	
	private static byte[] decodeTable(String code) {
		int hash = code.hashCode();
		byte[] ret = DECODE_TABLE_MAP.get(hash);
		if (ret != null) {
			return ret;
		}
		
		if (code.length() < 64) {
			throw new IllegalArgumentException("Base64 code length < 64.");
		}
		
		ret = new byte[128];
		Arrays.fill(ret, (byte)-1);
		for (int i = 0; i < 64; i ++) {
			ret[code.charAt(i)] = (byte)i;
		}
		DECODE_TABLE_MAP.put(hash, ret);
		return ret;
	}
	
	private static int indexOf(char[] cs, char c) {
		for(int i = 0, len = cs.length; i < len; i++) {
			if(cs[i] == c) {
				return i;
			}
		}
		
		return -1;
	}
	
	private static byte[] getMD(InputStream is, int bs) throws IOException {
		MessageDigest md = MD.get();
		byte[] buf = new byte[bs];
		while (is.available() > 0) {
			int read , total = 0;
			do {
				if ((read = is.read(buf, total, bs - total)) <= 0) {
					break;
				}
				total += read;
			} while (total < bs);
			md.update(buf);
		}
		
		return md.digest();
	}
	
}
