package com.youbo.youblog.common.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * MD5加密器
 *
 * @author youxiaobo
 * @date 2022/9/8
 */
public class Md5PasswordEncoder implements PasswordEncoder
{
    @Override
    public String encode(CharSequence charSequence)
    {
        Assert.notNull(charSequence, "密码为空");
        return DigestUtils.md5Hex(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s)
    {
        Assert.notNull(charSequence, "密码为空");
        return StringUtils.equalsIgnoreCase(DigestUtils.md5Hex(charSequence.toString()), s);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword)
    {
        return PasswordEncoder.super.upgradeEncoding(encodedPassword);
    }
}
