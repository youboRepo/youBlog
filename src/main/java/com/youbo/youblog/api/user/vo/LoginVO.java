package com.youbo.youblog.api.user.vo;

import lombok.Data;

/**
 * 登录传输对象
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@Data
public class LoginVO
{
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String kaptcha;
}
