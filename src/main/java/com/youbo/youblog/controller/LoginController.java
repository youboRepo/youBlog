package com.youbo.youblog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.code.kaptcha.Constants;
import com.youbo.youblog.api.user.entity.UserCustom;
import com.youbo.youblog.api.user.service.UserService;
import com.youbo.youblog.api.user.vo.LoginVO;
import com.youbo.youblog.common.exception.BaseException;
import com.youbo.youblog.common.model.Response;
import com.youbo.youblog.common.security.JwtProperties;
import com.youbo.youblog.common.security.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.zhogjianhao.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 登陆控制层
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@Slf4j
@RestController
public class LoginController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    @PostMapping("login")
    public Response<String> login(@RequestBody LoginVO loginVO, HttpSession session, HttpServletRequest request)
        throws IOException {

        // 校验验证码
        String realKaptcha = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        this.verifyKaptcha(loginVO.getKaptcha(), realKaptcha);

        String username = loginVO.getUsername();
        String password = loginVO.getPassword();
        Authentication authentication = null;

        try {
            // 校验用户密码
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BaseException("登录失败，联系管理员");
        }

        // 生成加密令牌
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        String token = jwtProperties.getToken(securityUser.getUsername());

        // 保存到Redis
        Optional.ofNullable(request.getHeader(jwtProperties.getTokenHeader())).ifPresent(stringRedisTemplate::delete);
        stringRedisTemplate.opsForValue()
            .set(token, JSON.toJSONString(securityUser, SerializerFeature.WriteClassName), 2L, TimeUnit.HOURS);

        // 修改登录时间
        Optional.ofNullable(securityUser.getId()).ifPresent(id -> {
            UserCustom user = new UserCustom();
            user.setId(id);
            user.setLoginTime(LocalDateTime.now());
            userService.updateUser(user);
        });

        // 获取真实客户端IP地址
        String ipAddress = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // 获取浏览器版本
        String userAgent = request.getHeader("User-Agent");

        // 记录操作日志
        String content = String.format("登录成功 | 客户地址=%s, 浏览器=%s", ipAddress, userAgent);
        //operationLogService.createOperationLog(securityUser.getId(), "Login", content);

        return Response.ok(token);
    }

    /**
     * 校验验证码
     *
     * @param kaptcha
     * @param realKaptcha
     */
    private void verifyKaptcha(String kaptcha, String realKaptcha) {
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(realKaptcha)) {
            throw new BaseException("请输入验证码");
        }

        // 比较验证码
        if (!StringUtils.equalsIgnoreCase(realKaptcha, kaptcha)) {
            throw new BaseException("验证码错误");
        }
    }
}
