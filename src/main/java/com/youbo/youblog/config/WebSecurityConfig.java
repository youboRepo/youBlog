package com.youbo.youblog.config;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.youbo.youblog.common.model.Response;
import com.youbo.youblog.common.security.JwtAuthenticationTokenFilter;
import com.youbo.youblog.common.security.JwtProperties;
import com.youbo.youblog.common.security.Md5PasswordEncoder;
import com.youbo.youblog.common.security.SecurityUser;
import com.youbo.youblog.common.security.SecurityUserDetailsService;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import top.zhogjianhao.SecurityUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ???????????????
 *
 * @author Administrator
 * @date 2022/11/16
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    private SecurityUserDetailsService securityUserDetailsService;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http?????????????????????????????????????????????????????????????????????
        http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests().antMatchers("/kaptcha.jpg", "/login", "/rest", "/barcode2d").anonymous()
            .anyRequest().authenticated().and().userDetailsService(securityUserDetailsService)
            .addFilter(switchUserFilter())
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class).logout()
            .logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler()).and().exceptionHandling()
            .authenticationEntryPoint(MyAuthenticationEntryPoint()).accessDeniedHandler(accessDeniedHandler());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        // ?????????????????????????????????hash???????????????
        return new Md5PasswordEncoder();
    }

    @Bean
    public SwitchUserFilter switchUserFilter() {
        SwitchUserFilter switchUserFilter = new SwitchUserFilter();
        switchUserFilter.setUserDetailsService(securityUserDetailsService);
        switchUserFilter.setSuccessHandler((request, response, authentication) -> {
            // ??????????????????
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            List<GrantedAuthority> authorities = authentication.getAuthorities().stream().collect(Collectors.toList());
            SecurityUser newSecurityUser = new SecurityUser(securityUser.getUser(), authorities);
            newSecurityUser.setId(securityUser.getId());
            newSecurityUser.setName(securityUser.getName());

            // ?????????Redis
            Optional.ofNullable(request.getHeader(jwtProperties.getTokenHeader())).ifPresent(token -> {
                stringRedisTemplate.opsForValue().set(token,
                    JSON.toJSONString(newSecurityUser, SerializerFeature.WriteClassName,
                        SerializerFeature.DisableCircularReferenceDetect), 1L, TimeUnit.HOURS);
            });

            // ??????????????????
            response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
            response.getWriter().append(JSON.toJSONString(Response.ok())).flush();
        });
        switchUserFilter.setFailureHandler((request, response, exception) -> {
            // ??????????????????
            response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
            response.getWriter().append(JSON.toJSONString(Response.error(403, exception.getMessage()))).flush();
        });
        return switchUserFilter;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        // ??????????????????
        return (request, response, authentication) -> {
            // ??????????????????
            response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
            response.getWriter().append(JSON.toJSONString(Response.ok("/supplier"))).flush();

            // ??????Redis
            stringRedisTemplate.delete(request.getHeader(jwtProperties.getTokenHeader()));
        };
    }

    @Bean
    public AuthenticationEntryPoint MyAuthenticationEntryPoint() {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException e) -> {
            // ??????????????????
            response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
            response.getWriter().append(JSON.toJSONString(Response.error(403, "???????????????"))).flush();
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        // ??????????????????
        return (request, response, accessDeniedException) -> {
            // ??????????????????????????????
            response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
            // ??????????????????????????????
            response.getWriter().append(JSON.toJSONString(Response.error(403, "????????????"))).flush();
        };
    }

    @Bean
    public ServletRegistrationBean kaptchaServlet() {
        ServletRegistrationBean kaptchaServlet = new ServletRegistrationBean(new KaptchaServlet(), "/kaptcha.jpg");
        kaptchaServlet.addInitParameter(Constants.KAPTCHA_BORDER, "no");
        kaptchaServlet.addInitParameter(Constants.KAPTCHA_IMAGE_WIDTH, "160");
        kaptchaServlet.addInitParameter(Constants.KAPTCHA_IMAGE_HEIGHT, "36");
        kaptchaServlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "32");
        kaptchaServlet.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        kaptchaServlet.addInitParameter(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.NoGimpy");
        return kaptchaServlet;
    }
}
