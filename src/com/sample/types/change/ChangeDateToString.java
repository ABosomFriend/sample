package com.sample.types.change;

import com.sample.types.JudgeType;

/**
 * 将数据库中的时间类型转化成字符串
 * Created by yizijun on 2017/7/3 0003.
 */
public class ChangeDateToString extends ChangeType {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T changeType(Class<T> type, Object value) {
        int srcIndex = JudgeType.judgeDateType(value);
        if(type != String.class || -1 == srcIndex)
            throw new ClassCastException("类型不一致，不能够转换");
        return (T)value.toString();
    }
}
