package com.msg.util;

import java.util.Random;

/**
 *
 */
public class SaltUtils {
	public static void main(String[] args) {
		System.out.println(getSalt(10));
	}
	/**
	 *
	 * @param n
	 * @return
	 */
	public static String getSalt(int n){
		char[] codes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
		Random random = new Random();
		//StringBuilder
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(codes[random.nextInt(codes.length)]);
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}
}
