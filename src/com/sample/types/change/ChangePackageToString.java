package com.sample.types.change;

import com.sample.types.JudgeType;

/**
 * 将数据库的包装类型转化为字符串
 * Created by yizijun on 2017/7/3 0003.
 */
public class ChangePackageToString extends ChangeType {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T changeType(Class<T> type, Object value) {
        int srcIndex = JudgeType.judgePackageType(value);
        if(-1 == srcIndex || type != String.class)
            throw new ClassCastException("类型不一致，不能够转换");
        return (T) value.toString();
    }
}
