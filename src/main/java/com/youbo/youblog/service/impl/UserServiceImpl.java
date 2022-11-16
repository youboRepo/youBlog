package com.youbo.youblog.service.impl;


import com.youbo.youblog.api.entity.User;
import com.youbo.youblog.api.service.UserService;
import com.youbo.youblog.common.jdbc.MyServiceImpl;
import com.youbo.youblog.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户服务层实现
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@Service
public class UserServiceImpl extends MyServiceImpl<UserMapper, User> implements UserService
{
}
