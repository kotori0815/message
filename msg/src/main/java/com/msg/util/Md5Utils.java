package com.msg.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5
 * @author Administrator
 *
 */
public class Md5Utils {
	
	/**
	 *  Md5
	 * @param password
	 * @return
	 */
	public static String generateMD5Code(String password){
		StringBuilder sb = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] digest = messageDigest.digest(password.getBytes());
			sb = new StringBuilder();
			//byte  -128   127    0--255
			for (byte b : digest) {
				int l = b & 0xff; 
				//System.out.println(l);  //  1  0x1 2 0x2 3 0x3 4 0x4  10 0xa  16 0x10 17 0x11
				if(l<16){
					sb.append("0");
				}
				sb.append(Integer.toHexString(l));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String generateMD5Code = Md5Utils.generateMD5Code("bigdobee123456");
		System.out.println(generateMD5Code);
		System.out.println(generateMD5Code.length());
	}
}
