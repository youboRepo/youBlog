package com.youbo.youblog.service.user.impl;


import com.youbo.youblog.api.user.entity.User;
import com.youbo.youblog.api.user.entity.UserCustom;
import com.youbo.youblog.api.user.service.UserService;
import com.youbo.youblog.common.jdbc.MyServiceImpl;
import com.youbo.youblog.service.user.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表服务层实现
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@Service
public class UserServiceImpl extends MyServiceImpl<UserMapper, User> implements UserService
{
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserCustom user)
    {
        // 密码加密处理
        if (StringUtils.isNoneBlank(user.getPassword()))
        {
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        }

        // 修改用户对象
        this.update(user);
    }
}
