package com.rm.business_lib.utils.aes;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
public class AESUtil {
    //-- 算法/模式/填充
    private static final String CIPHER_MODE = "AES/CBC/PKCS7Padding";
    private static final String CHARSET_UTF = "UTF-8";
    private static final String KEY_SPEC_AES = "AES";
    //--创建密钥
    private static SecretKeySpec createKey(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(password);
        String s ;
        while (sb.length() < 32) {
            sb.append(" ");//--密码长度不够32补足到32
        }
        s =sb.substring(0,32);//--截取32位密码
        try {
            data = s.getBytes(CHARSET_UTF);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, KEY_SPEC_AES);
    }

    //--创建偏移量
    private static IvParameterSpec createIV(String iv) {
        byte[] data = null;
        if (iv == null) {
            iv = "";
        }
        StringBuilder sb = new StringBuilder(16);
        sb.append(iv);
        String s =null;
        while (sb.length() < 16) {
            sb.append(" ");//--偏移量长度不够16补足到16
        }
        s =sb.substring(0,16);//--截取16位偏移量
        try {
            data = s.getBytes(CHARSET_UTF);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }

    //--加密字节数组到字节数组
    public static byte[] encryptByte2Byte(byte[] content,String password,String iv){
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //--加密字符串到base64
    public static String encryptString2Base64(String content, String password, String iv){
        byte[] data = null;
        try {
            data = content.getBytes(CHARSET_UTF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encryptByte2Byte(data,password,iv);
        String result =new String(Base64.encode(data,Base64.DEFAULT));
        return result;
    }

    //-- 解密字节数组到字节数组
    public static byte[] decryptByte2Byte(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.DECRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //-- 解密字节数组到字符串
    public static String decryptByte2String(byte[] content, String password, String iv) {
        byte[] data =decryptByte2Byte(content,password,iv);
        String result =new String(data);
        return result;
    }
}
