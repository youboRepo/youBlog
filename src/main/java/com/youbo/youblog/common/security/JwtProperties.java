package com.youbo.youblog.common.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT配置类
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * 密钥KEY
     */
    private String secret;

    /**
     * TokenKey
     */
    private String tokenHeader;

    /**
     * 前缀字符
     */
    private String startWith;

    /**
     * 过期时间
     */
    private Integer expiration;

    /**
     * 不需要认证的接口
     */
    private String antMatchers;

    /**
     * 获取令牌
     *
     * @param username
     * @return
     */
    public String getToken(String username) {
        return startWith + Jwts.builder().setId(username)
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS256, secret).compact();
    }
}
