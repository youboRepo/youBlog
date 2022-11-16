package com.youbo.youblog.util;


import com.youbo.youblog.common.constant.OauthConstant;
import org.apache.commons.lang3.StringUtils;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;

/**
 * 签名工具类
 *
 * @author youxiaobo
 * @date 2022/10/11
 */
public class OauthSignUtils
{

    /**
     * 请求参数签名
     *
     * @param params
     * @param secret
     * @param signMethod
     * @return
     * @throws IOException
     */
    public static String signRequest(Map<String, String> params, String secret, String signMethod) throws IOException
    {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        for (String key : keys)
        {
            String value = params.get(key);
            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value))
            {
                query.append(key).append(value);
            }
        }

        // 第三步：使用hmac_md5加密
        byte[] bytes;
        if (OauthConstant.SIGN_METHOD_HMAC.equals(signMethod))
        {
            bytes = encryptHMAC(query.toString(), secret);
        }
        else
        {
            // 默认hmac_md5加密
            bytes = encryptHMAC(query.toString(), secret);
        }

        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }

    /**
     * HMAC_MD5算法加密
     *
     * @param data
     * @param secret
     * @return
     * @throws IOException
     */
    private static byte[] encryptHMAC(String data, String secret) throws IOException
    {
        byte[] bytes = null;
        try
        {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(OauthConstant.CHARSET_UTF8), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(OauthConstant.CHARSET_UTF8));
        }
        catch (GeneralSecurityException gse)
        {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    /**
     * 把字节流转换为十六进制表示方式
     *
     * @param bytes
     * @return
     */
    public static String byte2hex(byte[] bytes)
    {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1)
            {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
