package com.mobanker.shanyidai.api.common.security;

import java.io.UnsupportedEncodingException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/*字符串 DESede(3DES) 加密 
 * ECB模式/使用PKCS7方式填充不足位,目前给的密钥是192位 
 * 3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的 
 * 加密标准），是DES的一个更安全的变形。它以DES为基本模块，通过组合分组方法设计出分组加 
 * 密算法，其具体实现如下：设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的 
 * 密钥，P代表明文，C代表密表，这样， 
 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P))) 
 * 3DES解密过程为：P=Dk1((EK2(Dk3(C))) 
 * */
/**
 * @author: liuyafei
 * @date 创建时间：2016年8月23日
 * @version 1.0
 * @parameter
 * @return
 */
public class ThreeDes {
	private static final String Algorithm = "DESede";

	    public static String encryptThreeDESECB(String src, String key) {
	        String msg = null;
	        try {
	            msg = encryptMode(src.getBytes("utf-8"),key);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return msg;
	    }

	    /**
	     * 3DESECB解密
	     *
	     * @param src
	     *            要解密的密文字符
	     * @param key
	     *            解密的Key key必须是长度大于等于 3*8 = 24 位
	     * @return
	     * @throws Exception
	     */
	    public static String decryptThreeDESECB(String src, String key) {
	        // --通过base64,将字符串转成byte数组
	        String msg = null;
	        try {
	        	 msg = decryptMode(src,key);
	        } catch (Exception e) {
	        }
	        return msg;
	    }

	    public static String  encryptMode(byte[] src,String PASSWORD_CRYPT_KEY) {
	        try {
	            SecretKey deskey = new SecretKeySpec(
	                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
	            Cipher cipher = Cipher.getInstance(Algorithm);
	            cipher.init(Cipher.ENCRYPT_MODE, deskey);

	            try {
	                // 为了防止解密时报javax.crypto.IllegalBlockSizeException: Input length must be multiple of 8 when decrypting with padded cipher异常，
	                // 不能把加密后的字节数组直接转换成字符串
	                byte[] buf = cipher.doFinal(src);

	                return Base64.encode(buf);

	            } catch (IllegalBlockSizeException e) {
	                e.printStackTrace();
	                throw new Exception("IllegalBlockSizeException", e);
	            } catch (BadPaddingException e) {
	                e.printStackTrace();
	                throw new Exception("BadPaddingException", e);
	            }

//	            return cipher.doFinal(src);
	        } catch (java.security.NoSuchAlgorithmException e1) {
	            e1.printStackTrace();
	        } catch (javax.crypto.NoSuchPaddingException e2) {
	            e2.printStackTrace();
	        } catch (Exception e3) {
	            e3.printStackTrace();
	        }
	        return null;
	    }

	    public static String decryptMode(String src, byte[] keybyte){
	        try {
	            //生成密钥
	            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
	            //解密
	            Cipher cipher = Cipher.getInstance(Algorithm);
	            cipher.init(Cipher.DECRYPT_MODE, deskey);

	            try {

	                byte[] buf = cipher.doFinal(Base64.decode(src));

	                return new String(buf);

	            } catch (IllegalBlockSizeException e) {
	                e.printStackTrace();
	                throw new Exception("IllegalBlockSizeException", e);
	            } catch (BadPaddingException e) {
	                e.printStackTrace();
	                throw new Exception("BadPaddingException", e);
	            }

//	            return c1.doFinal(src);
	        } catch (java.security.NoSuchAlgorithmException e1) {
	            // TODO: handle exception
	            e1.printStackTrace();
	        }catch(javax.crypto.NoSuchPaddingException e2){
	            e2.printStackTrace();
	        }catch(Exception e3){
	            e3.printStackTrace();
	        }
	        return null;
	    }

	    public static String decryptMode(String src, String PASSWORD_CRYPT_KEY) {
	        try {
	            SecretKey deskey = new SecretKeySpec(
	                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
	            Cipher cipher = Cipher.getInstance(Algorithm);
	            cipher.init(Cipher.DECRYPT_MODE, deskey);

	            try {

	                byte[] buf = cipher.doFinal(Base64.decode(src));

	                return new String(buf);

	            } catch (IllegalBlockSizeException e) {
	                e.printStackTrace();
	                throw new Exception("IllegalBlockSizeException", e);
	            } catch (BadPaddingException e) {
	                e.printStackTrace();
	                throw new Exception("BadPaddingException", e);
	            }

	        } catch (java.security.NoSuchAlgorithmException e1) {
	            e1.printStackTrace();
	        } catch (javax.crypto.NoSuchPaddingException e2) {
	            e2.printStackTrace();
	        } catch (Exception e3) {
	            e3.printStackTrace();
	        }
	        return null;
	    }

	    public static byte[] build3DesKey(String keyStr)
	            throws UnsupportedEncodingException {
	        byte[] key = new byte[24];
	        byte[] temp = keyStr.getBytes("UTF-8");

	        if (key.length > temp.length) {
	            System.arraycopy(temp, 0, key, 0, temp.length);
	        } else {
	            System.arraycopy(temp, 0, key, 0, key.length);
	        }
	        return key;
	    }

	public static void main(String[] args) throws Exception {
		// 密钥
		String key = "12345678123456781234567812345678abcvghyt";
		// 原文
		String data = "ThreeDes==3==呵呵呵呵";
		// 密文
		String des = ThreeDes.encryptThreeDESECB(data, key);
		
		System.out.println("加密后：" + des);
		// 解密
		String data2 = ThreeDes.decryptThreeDESECB(des, key);
		System.out.println("dec = " + data2);
	}
}
