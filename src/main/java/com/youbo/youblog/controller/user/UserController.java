package com.youbo.youblog.controller.user;

import com.youbo.youblog.api.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户表控制层
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@RestController
@RequestMapping("users")
public class UserController {

    @Resource
    private UserService userService;
}
