package com.youbo.youblog.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.youbo.youblog.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程工具类
 *
 * @author youxiaobo
 * @date 2020/1/14
 */
@Slf4j
public class ThreadUtils
{
    /**
     * 线程等待不限时间
     *
     * @param executor
     */
    public static void await(ExecutorService executor)
    {
        await(executor, Long.MAX_VALUE);
    }

    /**
     * 线程等待指定时间
     *
     * @param executor
     */
    public static void await(ExecutorService executor, long timeout)
    {
        try
        {
            executor.shutdown();
            executor.awaitTermination(timeout, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            log.error(e.getMessage(), e);
            throw new BaseException("线程等待错误");
        }
    }

    /**
     * 获取线程执行结果
     *
     * @param future
     * @param <T>
     * @return
     */
    public static <T> T getFuture(Future<T> future)
    {
        try
        {
            return future.get(300, TimeUnit.SECONDS);
        }
        catch (Throwable e)
        {
            if (e.getCause() != null)
            {
                e = e.getCause();
            }

            log.error(e.getMessage(), e);

            if (e instanceof BaseException)
            {
                throw (BaseException) e;
            }

            throw new BaseException(e.getMessage(), e);
        }
    }

    /**
     * 获取线程执行结果列表
     *
     * @param futures
     * @param <T>
     * @return
     */
    public static <T> List<T> getFutureList(List<Future<T>> futures)
    {
        List<T> results = new ArrayList<>();
        for (Future<T> future : futures)
        {
            T result = getFuture(future);
            results.add(result);
        }
        return results;
    }

    /**
     * 线程休眠
     *
     * @param millis
     */
    public static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            log.error(e.getMessage(), e);
            throw new BaseException("线程休眠错误");
        }
    }

    /**
     * 创建固定大小的线程池
     *
     * @param size
     * @param name
     * @return
     */
    public static ThreadPoolExecutor newFixedThreadPool(int size, String name)
    {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(name + "-pool-%d").build();
        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);
    }
}
