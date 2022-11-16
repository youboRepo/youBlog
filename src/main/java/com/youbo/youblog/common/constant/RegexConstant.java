package com.youbo.youblog.common.constant;

/**
 * 正则常量类
 *
 * @author youxiaobo
 * @date 2021/3/17
 */
public interface RegexConstant {

    /**
     * 不是中文
     */
    String NOT_CHINESE = "[^\u4e00-\u9fa5]";

    /**
     * 网页地址
     */
    String HTTP_URL = "http://[A-Za-z0-9.]+";

    /**
     * 图片地址
     */
    String IMAGE_SRC = "(?<=src=\")[^\"]+(\\.jpg|jpeg|gif|bmp|bnp|png)[^\"]*(?=\")";

    /**
     * 工号域名
     */
    String JOB_NUMBER_DOMAIN = "http://\\w+.imgs.ltd/\\w+";

    /**
     * 数字尺码
     */
    String NUMERIC_SIZE = "\\d+(.\\d+)?(-\\d+(.\\d+)?)?";

    /**
     * 月份尺码
     */
    String MONTH_SIZE = "\\d+(-\\d+)?( )?(Month|month|Months|months|M|m)";

    /**
     * 年份尺码
     */
    String YEAR_SIZE = "\\d+(-\\d+)?( )?(Year|year|Years|years|Y|y|T|t)";

    /**
     * 国内图片地址
     */
    String IMAGE_URL_28099 = "http://.+:28099/image/product/ftp";

    /**
     * 多个空格
     */
    String MULTI_BLANK = " +";
}
