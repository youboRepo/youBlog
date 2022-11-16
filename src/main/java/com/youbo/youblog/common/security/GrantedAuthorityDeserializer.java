package com.youbo.youblog.common.security;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;

import java.lang.reflect.Type;

/**
 * FastJson——自己实现SwitchUserGrantedAuthority类的反序列化方法
 *
 * @author Administrator
 * @date 2022/11/2 0002
 */
public class GrantedAuthorityDeserializer implements ObjectDeserializer
{
    @Override
    public SwitchUserGrantedAuthority deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o)
    {
        JSONObject jsonObject = defaultJSONParser.parseObject();
        String authority = Convert.toStr(jsonObject.get("authority"));
        Authentication source = Convert.convert(Authentication.class, jsonObject.get("source"));
        return new SwitchUserGrantedAuthority(authority, source);
    }

    @Override
    public int getFastMatchToken()
    {
        return 0;
    }
}
