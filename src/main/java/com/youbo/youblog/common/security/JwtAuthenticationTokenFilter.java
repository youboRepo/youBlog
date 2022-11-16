package com.youbo.youblog.common.security;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.youbo.youblog.util.ContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * JWT权限过滤器
 *
 * @author youxiaobo
 * @date 2022/9/13
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        String token = request.getHeader(jwtProperties.getTokenHeader());
        if (StrUtil.isEmpty(token))
        {
            chain.doFilter(request, response);
            return;
        }

        // 获取安全用户
        SecurityUser securityUser = this.getSecurityUser(token);

        Optional.ofNullable(securityUser).ifPresent(user -> {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        });

        chain.doFilter(request, response);
    }

    /**
     * 获取安全用户
     *
     * @param token
     * @return
     */
    private SecurityUser getSecurityUser(String token)
    {
        String tokenValue = stringRedisTemplate.opsForValue().get(token);

        if (StringUtils.isBlank(tokenValue))
        {
            return null;
        }

        // 解析JSON信息
        ParserConfig.getGlobalInstance().putDeserializer(SwitchUserGrantedAuthority.class, new GrantedAuthorityDeserializer());
        TypeUtils.addMapping("org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority", SwitchUserGrantedAuthority.class);
        SecurityUser securityUser = JSON.parseObject(tokenValue, SecurityUser.class);

        // 设置本地参数
        ContextUtils.clearContext();
        ContextUtils.setUserId(securityUser.getId());
        ContextUtils.setUsername(securityUser.getUsername());
        ContextUtils.setName(securityUser.getName());

        return securityUser;
    }
}
