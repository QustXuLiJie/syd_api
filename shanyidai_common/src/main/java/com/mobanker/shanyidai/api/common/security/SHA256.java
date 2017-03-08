package com.mobanker.shanyidai.api.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: liuyafei
 * @date 创建时间：2016年8月23日
 * @version 1.0
 * @parameter
 * @return
 */
public class SHA256 {

	/**
	 * SHA256 256加密
	* @author: liuyafei
	* @date 创建时间：2016年8月23日
	* @version 1.0 
	* @parameter  
	* @return
	 */
	public static String getSHA256(String strText) {
		return SHA(strText, "SHA-256");
	}

	/**
	 * 字符串 SHA 加密
	 * 
	 * @param strSourceText
	 * @return
	 */
	public static String SHA(String strText, String strType) {
		// 返回值
		String strResult = null;

		// 是否是有效字符串
		if (strText != null && strText.length() > 0) {
			try {
				// SHA 加密开始
				// 创建加密对象 并傳入加密類型
				MessageDigest messageDigest = MessageDigest
						.getInstance(strType);
				// 传入要加密的字符串
				messageDigest.update(strText.getBytes());
				// 得到 byte 類型结果
				byte byteBuffer[] = messageDigest.digest();

				// 將 byte 轉換爲 string
				StringBuffer strHexString = new StringBuffer();
				// 遍歷 byte buffer
				for (int i = 0; i < byteBuffer.length; i++) {
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1) {
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// 得到返回結果
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return strResult;
	}
}
