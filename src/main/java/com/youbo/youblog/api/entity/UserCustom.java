package com.youbo.youblog.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户定制类
 *
 * @author youxiaobo
 * @date 2022/11/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCustom extends User
{
    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Long current;

    /**
     * 每页数量
     */
    private Long size;
}
