package com.yuchengtech.bob.core;

/*
 * Created on 2007-10-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
/**
 * 
 * @description 加密、解密工具类
 * @author  chenlin
 *
 * @date 2014-10-11
 */
public class AESHelper {
	private static String desKey = "12345678";

	/**
	 * 定义加密算法
	 */
	private static String Algorithm = "DES"; // 定义 加密算法,可用
	// DES,DESede,Blowfish

	static {
		//Security.addProvider(new com.sun.crypto.provider.SunJCE());
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}

	/**
	 * keyStr为Key对字符串加密
	 * 
	 * @param keyStr
	 *            String
	 * @param str
	 *            String 需加密字符串
	 * @throws Exception
	 * @return String 加密后HEX码
	 */
	public static String encodeByKey(String keyStr, String str)
			throws Exception {
		byte[] key = keyStr.getBytes();
		return toHex(encode(str.getBytes(), key));
	}

	/**
	 * 使用预设的DesKey
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encodeStr(String str) throws Exception {
		return encodeByKey(getDesKey(), str);
	}

	/**
	 * keyStr为Key对字符串解密
	 * 
	 * @param keyStr
	 *            String
	 * @param str
	 *            String 需解密HEX字符串
	 * @throws Exception
	 * @return String 解密后字符串
	 */
	private static String decodeByKey(String keyStr, String str)
			throws Exception {
		byte[] key = keyStr.getBytes();
		return new String(decode(hexToBytes(str), key));
	}

	/**
	 * 使用预设的DesKey
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String decodeStr(String str) throws Exception {
		return decodeByKey(getDesKey(), str);
	}

	/**
	 * 加密
	 * 
	 * @param input
	 *            byte[] 需加密字节码
	 * @param key
	 *            byte[] 密钥
	 * @throws Exception
	 * @return byte[] 加密后字节码
	 */
	private static byte[] encode(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherByte = c1.doFinal(input);
		return cipherByte;
	}

	/**
	 * 使用预设的DesKey
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	private static byte[] encode(byte[] input) throws Exception {
		return encode(input, getDesKey().getBytes());
	}

	/**
	 * 解密
	 * 
	 * @param input
	 *            byte[] 需解密字节码
	 * @param key
	 *            byte[] 密钥
	 * @throws Exception
	 * @return byte[] 解密后字节码
	 */
	private static byte[] decode(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);

		byte[] clearByte = c1.doFinal(input);
		return clearByte;
	}
	
	/**
	 * 使用预设的DesKey
	 * @param input
	 * @return
	 * @throws Exception
	 */
	private static byte[] decode(byte[] input) throws Exception {
		return decode(input, getDesKey().getBytes());
	}

	private static final String toHex(byte hash[]) {
		StringBuffer buf = new StringBuffer(hash.length * 2);
		String stmp = "";

		for (int i = 0; i < hash.length; i++) {
			stmp = (java.lang.Integer.toHexString(hash[i] & 0XFF));
			if (stmp.length() == 1) {
				buf.append(0).append(stmp);
			} else {
				buf.append(stmp);
			}
		}
		return buf.toString();
	}

	private static final byte[] hexToBytes(String hex) {
		if (null == hex) {
			return new byte[0];
		}
		int len = hex.length();
		byte[] bytes = new byte[len / 2];
		String stmp = null;
		try {
			for (int i = 0; i < bytes.length; i++) {
				stmp = hex.substring(i * 2, i * 2 + 2);
				bytes[i] = (byte) Integer.parseInt(stmp, 16);
			}
		} catch (Exception e) {
			return new byte[0];
		}

		return bytes;
	}

	/**
	 * 设置预设DESKey
	 * @param desKey
	 */
	public static void setDesKey(String desKey) {
		AESHelper.desKey = desKey;
	}

	/**
	 * 取预设的DESKey
	 * @return
	 */
	private static String getDesKey() {
		return AESHelper.desKey;
	}

	public static void main(String[] args) throws Exception
	{

		String usercode ="hy_crm_sh";
		System.out.println(encodeStr(usercode));
		System.out.println(decodeStr(encodeStr(usercode)));
	}
}
