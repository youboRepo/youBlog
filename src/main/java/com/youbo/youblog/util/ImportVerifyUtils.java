package com.youbo.youblog.util;

import com.youbo.youblog.common.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * 导入校验工具类
 *
 * @author youxiaobo
 * @date 2020/8/30
 */
public class ImportVerifyUtils
{
    /**
     * 日期格式
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d");

    /**
     * 日期时间格式
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d H:m:s");

    /**
     * 年月格式
     */
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M");

    /**
     * 校验必填
     *
     * @param value
     * @param i
     * @param label
     */
    public static void verify(String value, int i, String label)
    {
        Assert.hasText(value, "第[" + (i + 2) + "]行的" + label + "不能为空");
    }

    /**
     * 校验必填
     *
     * @param value
     * @param i
     * @param label
     */
    public static void verify(Object value, int i, String label)
    {
        Assert.notNull(value, "第[" + (i + 2) + "]行的" + label + "不能为空");
    }

    /**
     * 校验已存在
     *
     * @param value
     * @param i
     * @param label
     */
    public static void verifyExist(Object value, int i, String label)
    {
        Assert.isNull(value, "第[" + (i + 2) + "]行的" + label + "已存在");
    }

    /**
     * 校验已存在
     *
     * @param i
     * @param label
     */
    public static void verifyExist(int i, String label)
    {
        throw new BaseException("第[" + (i + 2) + "]行的" + label + "已存在");
    }

    /**
     * 校验不存在
     *
     * @param value
     * @param i
     * @param label
     */
    public static void verifyNotExist(Object value, int i, String label)
    {
        Assert.notNull(value, "第[" + (i + 2) + "]行的" + label + "不存在");
    }

    /**
     * 校验不存在
     *
     * @param value
     * @param i
     * @param label
     */
    public static void verifyNotExist(String value, int i, String label)
    {
        Assert.hasText(value, "第[" + (i + 2) + "]行的" + label + "不存在");
    }

    /**
     * 校验不存在
     *
     * @param values
     * @param i
     * @param label
     */
    public static void verifyNotExist(Collection<?> values, int i, String label)
    {
        Assert.notEmpty(values, "第[" + (i + 2) + "]行的" + label + "不存在");
    }

    /**
     * 校验不存在
     *
     * @param i
     * @param label
     */
    public static void verifyNotExist(int i, String label)
    {
        throw new BaseException("第[" + (i + 2) + "]行的" + label + "不存在");
    }

    /**
     * 校验固定长度
     *
     * @param value
     * @param i
     * @param label
     * @param length
     */
    public static void verifyLength(String value, int i, String label, int length)
    {
        if (StringUtils.isBlank(value))
        {
            return;
        }

        if (value.length() != length)
        {
            throw new BaseException("第[" + (i + 2) + "]行的" + label + "长度错误[必须" + length + "个字符]");
        }
    }

    /**
     * 校验最小长度
     *
     * @param value
     * @param i
     * @param label
     * @param minLength
     */
    public static void verifyMinLength(String value, int i, String label, int minLength)
    {
        if (StringUtils.isBlank(value))
        {
            return;
        }

        if (value.length() < minLength)
        {
            throw new BaseException("第[" + (i + 2) + "]行的" + label + "长度错误[最少" + minLength + "个字符]");
        }
    }

    /**
     * 校验最大长度
     *
     * @param value
     * @param i
     * @param label
     * @param maxLength
     */
    public static void verifyMaxLength(String value, int i, String label, int maxLength)
    {
        if (StringUtils.isBlank(value))
        {
            return;
        }

        if (value.length() > maxLength)
        {
            throw new BaseException("第[" + (i + 2) + "]行的" + label + "长度错误[最多" + maxLength + "个字符]");
        }
    }

    /**
     * 校验整数
     *
     * @param value
     * @param i
     * @param label
     * @return
     */
    public static Integer verifyInteger(String value, int i, String label)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }

        if (StringUtils.isNumeric(value))
        {
            return Integer.valueOf(value);
        }

        throw new BaseException("第[" + (i + 2) + "]行的" + label + "格式错误[必须整数]");
    }

    /**
     * 校验小数
     *
     * @param value
     * @param i
     * @param label
     * @return
     */
    public static Double verifyDouble(String value, int i, String label)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }

        if (NumberUtils.isCreatable(value))
        {
            return Double.valueOf(value);
        }

        throw new BaseException("第[" + (i + 2) + "]行的" + label + "格式错误[必须小数]");
    }

    /**
     * 校验日期
     *
     * @param value
     * @param i
     * @param label
     * @return
     */
    public static LocalDate verifyLocalDate(String value, int i, String label)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }

        try
        {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        }
        catch (Exception e)
        {
            throw new BaseException("第[" + (i + 2) + "]行的" + label + "格式错误[年-月-日]");
        }
    }

    /**
     * 校验日期
     *
     * @param value
     * @param i
     * @param label
     * @param pattern
     * @return
     */
    public static LocalDate verifyLocalDate(String value, int i, String label, String pattern)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }

        DateTimeFormatter formatter = DATE_FORMATTER;

        if (StringUtils.isNotBlank(pattern))
        {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }

        try
        {
            return LocalDate.parse(value, formatter);
        }
        catch (Exception e)
        {
            throw new BaseException("第[" + (i + 2) + "]行的" + label + "格式错误[年-月-日]");
        }
    }

    /**
     * 校验日期时间
     *
     * @param value
     * @param i
     * @param label
     * @return
     */
    public static LocalDateTime verifyLocalDateTime(String value, int i, String label)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }

        try
        {
            return LocalDateTime.parse(label, DateTimeFormatter.ofPattern(value));
        }
        catch (Exception e)
        {
            throw new BaseException("第[" + (i + 2) + "]行的" + label + "格式错误[年-月-日 时:分:秒]");
        }
    }

    /**
     * 校验日期时间
     *
     * @param value
     * @param i
     * @param label
     * @param pattern
     * @return
     */
    public static LocalDateTime verifyLocalDateTime(String value, int i, String label, String pattern)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }

        DateTimeFormatter formatter = DATE_TIME_FORMATTER;

        if (StringUtils.isNotBlank(pattern))
        {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }

        try
        {
            return LocalDateTime.parse(value, formatter);
        }
        catch (Exception e)
        {
            throw new BaseException("第[" + (i + 2) + "]行的" + label + "格式错误[年-月-日 时:分:秒]");
        }
    }

    /**
     * 校验年月
     *
     * @param value
     * @param i
     * @param label
     * @return
     */
    public static YearMonth verifyYearMonth(String value, int i, String label)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }

        try
        {
            return YearMonth.parse(value);
        }
        catch (Exception e)
        {
            throw new BaseException("第[" + (i + 2) + "]行的" + label + "格式错误[年-月]");
        }
    }

    /**
     * 校验年月
     *
     * @param value
     * @param i
     * @param label
     * @param pattern
     * @return
     */
    public static YearMonth verifyYearMonth(String value, int i, String label, String pattern)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }

        DateTimeFormatter formatter = YEAR_MONTH_FORMATTER;

        if (StringUtils.isNotBlank(pattern))
        {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }

        try
        {
            return YearMonth.parse(value, formatter);
        }
        catch (Exception e)
        {
            throw new BaseException("第[" + (i + 2) + "]行的" + label + "格式错误[年-月]");
        }
    }
}
