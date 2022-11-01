package com.youbo.youblog.common.exception;

/**
 * 抛出异常函数接口
 *
 * @author youxiaobo
 * @date 2021/2/5
 */
@FunctionalInterface
public interface ThrowExceptionSupplier<T>
{
    /**
     * 返回一个结果
     *
     * @return
     * @throws Exception
     */
    T get() throws Exception;
}
