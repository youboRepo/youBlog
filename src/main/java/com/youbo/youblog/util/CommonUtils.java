package com.youbo.youblog.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.youbo.youblog.common.constant.RegexConstant;
import com.youbo.youblog.common.exception.BaseException;
import com.youbo.youblog.common.exception.ThrowExceptionSupplier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 公共工具类
 *
 * @author youxiaobo
 * @date 2019/12/26
 */
public class CommonUtils
{
    /**
     * 默认分隔正则
     */
    private static final String DEFAULT_SPLIT_REGEX = ",， \t\n\r";

    /**
     * 排重集合容器
     */
    private static final Set<String> set = new LinkedHashSet<String>()
    {
        /**
         * 序列化标识
         */
        private static final long serialVersionUID = 1L;

        @Override
        public synchronized boolean add(String e)
        {
            if (this.size() > 1000)
            {
                this.stream().findFirst().ifPresent(this::remove);
            }
            return super.add(e);
        }
    };

    /**
     * 删除交集元素
     *
     * @param a
     * @param b
     */
    public static void removeIntersectElement(List<String> a, List<String> b)
    {
        if (CollectionUtils.isNotEmpty(a) && CollectionUtils.isNotEmpty(b))
        {
            Iterator<String> it = a.iterator();
            while (it.hasNext())
            {
                String c = it.next();
                if (b.remove(c))
                {
                    it.remove();
                }
            }
        }
    }

    /**
     * 分隔字符列表
     *
     * @param text
     * @param regex
     * @return
     */
    public static List<String> splitStringList(String text, String regex)
    {
        String[] split = StringUtils.split(text, regex);
        if (split == null)
        {
            return Lists.newArrayList();
        }
        return Arrays.stream(split).collect(Collectors.toList());
    }

    /**
     * 分隔字符列表
     *
     * @param text
     * @return
     */
    public static List<String> splitStringList(String text)
    {
        return splitStringList(text, DEFAULT_SPLIT_REGEX);
    }

    /**
     * 分隔整数列表
     *
     * @param text
     * @param regex
     * @return
     */
    public static List<Integer> splitIntegerList(String text, String regex)
    {
        String[] split = StringUtils.split(text, regex);
        if (split == null)
        {
            return Lists.newArrayList();
        }
        return Arrays.stream(split).filter(StringUtils::isNumeric).map(Integer::valueOf).collect(Collectors.toList());
    }

    /**
     * 分隔整数列表
     *
     * @param text
     * @return
     */
    public static List<Integer> splitIntegerList(String text)
    {
        return splitIntegerList(text, DEFAULT_SPLIT_REGEX);
    }

    /**
     * 分隔长整列表
     *
     * @param text
     * @param regex
     * @return
     */
    public static List<Long> splitLongList(String text, String regex)
    {
        String[] split = StringUtils.split(text, regex);
        if (split == null)
        {
            return Lists.newArrayList();
        }
        return Arrays.stream(split).filter(StringUtils::isNumeric).map(Long::valueOf).collect(Collectors.toList());
    }

    /**
     * 分隔长整列表
     *
     * @param text
     * @return
     */
    public static List<Long> splitLongList(String text)
    {
        return splitLongList(text, DEFAULT_SPLIT_REGEX);
    }

    /**
     * 创建临时文件
     *
     * @param prefix
     * @param suffix
     * @return
     */
    public static File createTempFile(String prefix, String suffix)
    {
        try
        {
            return File.createTempFile(prefix, suffix);
        }
        catch (IOException e)
        {
            throw new BaseException(e.getMessage(), e);
        }
    }

    /**
     * 转换映射对象
     *
     * @param text
     * @return
     */
    public static Map<String, String> toMap(String text)
    {
        return JSON.parseObject(text, new TypeReference<Map<String, String>>()
        {
        });
    }

    /**
     * 转换整型字符映射对象
     *
     * @param text
     * @return
     */
    public static Map<Integer, String> toIntegerStringMap(String text)
    {
        return JSON.parseObject(text, new TypeReference<Map<Integer, String>>()
        {
        });
    }

    /**
     * 转换映射对象
     *
     * @param text
     * @return
     */
    public static Map<String, String> toLinkedHashMap(String text)
    {
        return JSON.parseObject(text, new TypeReference<LinkedHashMap<String, String>>()
        {
        });
    }

    /**
     * 格式句子
     *
     * @param text
     * @return
     */
    public static String formatSentence(String text)
    {
        return CommonUtils.splitStringList(text, " ").stream().filter(StringUtils::isNotBlank).map(StringUtils::trim)
                .map(StrUtil::upperFirst).collect(Collectors.joining(" "));
    }

    /**
     * 限制长度
     *
     * @param text
     * @param maxLength
     * @return
     */
    public static String limitLength(String text, int maxLength)
    {
        if (StringUtils.isBlank(text))
        {
            return text;
        }

        text = text.trim().replaceAll(RegexConstant.MULTI_BLANK, " ");

        while (text.length() > maxLength)
        {
            if (text.contains(" "))
            {
                text = StringUtils.substringBeforeLast(text, " ");
            }
            else
            {
                text = StringUtils.substring(text, 0, maxLength);
            }
        }

        return text;
    }

    /**
     * 随机字母数字
     *
     * @param prefix
     * @param count
     * @return
     */
    public static String randomAlphanumeric(String prefix, int count)
    {
        Assert.hasText(prefix, "前缀不能为空");

        while (true)
        {
            String text = prefix + RandomStringUtils.randomAlphanumeric(count);
            if (set.add(text.toUpperCase()))
            {
                return text;
            }
        }
    }

    /**
     * 获取结果
     *
     * @param action
     * @param <R>
     * @return
     */
    public static <R> R get(Supplier<R> action)
    {
        if (action == null)
        {
            return null;
        }

        return action.get();
    }

    /**
     * 根据参数获取结果
     *
     * @param t
     * @param action
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R get(T t, Function<T, R> action)
    {
        if (ObjectUtil.hasEmpty(t, action))
        {
            return null;
        }

        return action.apply(t);
    }

    /**
     * 根据参数获取结果
     *
     * @param t
     * @param u
     * @param action
     * @param <T>
     * @param <U>
     * @param <R>
     * @return
     */
    public static <T, U, R> R get(T t, U u, BiFunction<T, U, R> action)
    {
        if (ObjectUtil.hasEmpty(t, u, action))
        {
            return null;
        }

        return action.apply(t, u);
    }

    /**
     * 捕获异常
     *
     * @param action
     * @param <R>
     * @return
     */
    public static <R> R tryCatch(ThrowExceptionSupplier<R> action)
    {
        try
        {
            return action.get();
        }
        catch (BaseException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BaseException(e.getMessage(), e);
        }
    }

    /**
     * 去除重复
     *
     * @param function
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinct(Function<T, ?> function)
    {
        Set<Object> set = ConcurrentHashMap.newKeySet();
        return t -> set.add(function.apply(t));
    }

    /**
     * 谓词取反
     *
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> negate(Predicate<T> predicate)
    {
        return predicate.negate();
    }

    /**
     * 是否为空
     *
     * @param function
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> isEmpty(Function<T, ?> function)
    {
        return t -> ObjectUtil.isEmpty(function.apply(t));
    }

    /**
     * 是否不空
     *
     * @param function
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> isNotEmpty(Function<T, ?> function)
    {
        return t -> ObjectUtil.isNotEmpty(function.apply(t));
    }
}
