package com.youbo.youblog.controller.user;

import com.youbo.youblog.api.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户表控制层
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@RestController
@RequestMapping("users")
public class UserController
{
    @Autowired
    private UserService userService;
}
