package com.sample.types.change;

import com.sample.types.GenerateTypes;
import com.sample.types.JudgeType;
import com.sample.util.ClassUtil;

/**
 * 将数据库的包装类转化为基本数据类型或基本数据类型的包装类型
 * Created by yizijun on 2017/7/3 0003.
 */
public class ChangePackageToBasic extends ChangeType {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T changeType(Class<T> type, Object value) {
        //先获得源值（数据库的值）的类类型和目标类类型的下标
        int srcIndex = JudgeType.judgePackageType(value);
        int destIndex = JudgeType.judgeType(type);

        if (srcIndex == -1 || destIndex == -1)
            throw new ClassCastException("类型不一致，不能够转换");

        return ClassUtil.getBasicValue((Class<T>) GenerateTypes.getBasic(destIndex),
                value.getClass(), value);
    }
}
