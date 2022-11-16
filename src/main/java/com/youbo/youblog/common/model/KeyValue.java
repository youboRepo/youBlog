package com.youbo.youblog.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 键值元素元组
 *
 * @author youxiaobo
 * @date 2022/09/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue<K, V> implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 1L;

    /**
     * 第一个元素
     */
    private K key;

    /**
     * 第二个元素
     */
    private V value;

    public static <K, V> KeyValue<K, V> with(K key, V value) {
        return new KeyValue<>(key, value);
    }
}
