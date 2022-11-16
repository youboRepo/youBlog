package com.youbo.youblog.common.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应对象
 *
 * @author youxiaobo
 * @date 2020/10/22
 */
@Data
public class Response<T> {

    /**
     * 响应代码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 附加信息
     */
    private Map<String, Object> extra;

    private String location;

    /**
     * 操作成功
     *
     * @return
     */
    public static <T> Response<T> ok() {
        return ok(null);
    }

    /**
     * 操作成功
     *
     * @param data
     * @return
     */
    public static <T> Response<T> ok(T data) {
        Response<T> response = new Response<>();
        response.setCode(20000);
        response.setData(data);
        return response;
    }

    /**
     * 操作失败
     *
     * @param message
     * @return
     */
    public static <T> Response<T> error(String message) {
        return error(50000, message);
    }

    /**
     * 操作失败
     *
     * @param code
     * @param message
     * @return
     */
    public static <T> Response<T> error(int code, String message) {
        if (StringUtils.isBlank(message)) {
            message = "未知系统错误";
        }

        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static Response<String> location(String location) {
        Response<String> response = new Response<>();
        response.setLocation(location);
        response.setData(location);
        return response;
    }

    /**
     * 附加信息
     *
     * @param key
     * @param value
     * @return
     */
    public Response<T> put(String key, Object value) {
        if (StringUtils.isNotBlank(key) || value != null) {
            if (this.extra == null) {
                this.extra = new HashMap<>(16);
            }
            this.extra.put(key, value);
        }
        return this;
    }
}
