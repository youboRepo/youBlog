package com.youbo.youblog.util;

import cn.hutool.core.map.MapUtil;
import com.youbo.youblog.common.constant.ContextKey;
import org.apache.commons.lang3.math.NumberUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 上下文工具类
 *
 * @author youxiaobo
 * @date 2020/8/20
 */
public class ContextUtils
{
    /**
     * 本地线程变量
     */
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new InheritableThreadLocal<Map<String, String>>()
    {
        @Override
        protected synchronized Map<String, String> initialValue()
        {
            return MapUtil.newHashMap();
        }

        @Override
        protected Map<String, String> childValue(Map<String, String> parentValue)
        {
            return Optional.ofNullable(parentValue).map(HashMap::new).orElse(null);
        }
    };

    /**
     * 获取本地上下文参数
     *
     * @param key
     * @return
     */
    public static String getContext(String key)
    {
        return THREAD_LOCAL.get().get(key);
    }

    /**
     * 获取整数类型的本地上下文参数
     *
     * @param key
     * @return
     */
    public static Integer getIntegerContext(String key)
    {
        return Optional.ofNullable(key).map(ContextUtils::getContext).filter(NumberUtils::isDigits)
                .map(Integer::valueOf).orElse(null);
    }

    /**
     * 设置本地上下文参数
     *
     * @param key
     * @param value
     */
    public static void setContext(String key, String value)
    {
        THREAD_LOCAL.get().put(key, value);
    }

    /**
     * 设置整数类型的本地上下文参数
     *
     * @param key
     * @param value
     */
    public static void setIntegerContext(String key, Integer value)
    {
        if (value != null)
        {
            setContext(key, value.toString());
        }
        else
        {
            removeContext(key);
        }
    }

    /**
     * 删除本地上下文参数
     *
     * @param key
     */
    public static void removeContext(String key)
    {
        THREAD_LOCAL.get().remove(key);
    }

    /**
     * 清除本地上下文参数
     */
    public static void clearContext()
    {
        THREAD_LOCAL.get().clear();
    }

    /**
     * 获取用户标识
     *
     * @return
     */
    public static Integer getUserId()
    {
        return getIntegerContext(ContextKey.USER_ID);
    }

    /**
     * 获取用户标识
     *
     * @param defaultValue
     * @return
     */
    public static Integer getUserId(Integer defaultValue)
    {
        return Optional.ofNullable(getUserId()).orElse(defaultValue);
    }

    /**
     * 获取用户标识
     *
     * @param supplier
     * @return
     */
    public static Integer getUserId(Supplier<Integer> supplier)
    {
        return Optional.ofNullable(getUserId()).orElseGet(supplier);
    }

    /**
     * 设置用户标识
     *
     * @param userId
     */
    public static void setUserId(Integer userId)
    {
        setIntegerContext(ContextKey.USER_ID, userId);
    }

    /**
     * 设置用户标识
     *
     * @param consumer
     */
    public static void setUserId(Consumer<Integer> consumer)
    {
        Optional.ofNullable(getUserId()).ifPresent(consumer);
    }

    /**
     * 获取登录用户
     *
     * @return
     */
    public static String getUsername()
    {
        return getContext(ContextKey.USERNAME);
    }

    /**
     * 设置登录用户
     *
     * @param username
     */
    public static void setUsername(String username)
    {
        setContext(ContextKey.USERNAME, username);
    }

    /**
     * 获取用户姓名
     *
     * @return
     */
    public static String getName()
    {
        return getContext(ContextKey.NAME);
    }

    /**
     * 设置用户姓名
     *
     * @param name
     */
    public static void setName(String name)
    {
        setContext(ContextKey.NAME, name);
    }

    /**
     * 获取供应商标识
     *
     * @return
     */
    public static Integer getSupplierId()
    {
        return getIntegerContext(ContextKey.SUPPLIER_ID);
    }

    /**
     * 设置供应商标识
     *
     * @param supplierId
     */
    public static void setSupplierId(Integer supplierId)
    {
        setIntegerContext(ContextKey.SUPPLIER_ID, supplierId);
    }

    /**
     * 获取供应商号
     *
     * @return
     */
    public static String getSupplierNumber()
    {
        return getContext(ContextKey.SUPPLIER_NUMBER);
    }

    /**
     * 设置供应商号
     *
     * @param supplierNumber
     */
    public static void setSupplierNumber(String supplierNumber)
    {
        setContext(ContextKey.SUPPLIER_NUMBER, supplierNumber);
    }

    /**
     * 获取用户类型
     *
     * @return
     */
    public static Integer getUserType()
    {
        return getIntegerContext(ContextKey.USER_TYPE);
    }

    /**
     * 设置用户类型
     *
     * @param userType
     */
    public static void setUserType(Integer userType)
    {
        setIntegerContext(ContextKey.USER_TYPE, userType);
    }
}
