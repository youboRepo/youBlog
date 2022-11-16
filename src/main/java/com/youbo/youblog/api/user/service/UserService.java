package com.youbo.youblog.api.user.service;

import com.youbo.youblog.api.user.entity.UserCustom;

/**
 * 用户表服务层接口
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
public interface UserService
{
    /**
     * 修改用户对象
     *
     * @param user
     */
    void updateUser(UserCustom user);
}
