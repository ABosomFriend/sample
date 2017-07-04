package com.sample.types.change;

/**
 * 注意我们这个转换并不能保证不会发生精度的丢失，所以精度问题由使用者自己控制
 * Created by yizijun on 2017/7/3 0003.
 */
public abstract class ChangeType {
    /**
     * 将value转化为T类型的值并返回
     * @param type 目标class类型
     * @param value 转化前的值
     * @param <T>  目标Class类型
     * @return  转化后的值
     */
    public abstract <T> T changeType(Class<T> type, Object value);
}
