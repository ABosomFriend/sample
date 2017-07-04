package com.sample.types.change;

import com.sample.types.JudgeType;
import com.sample.util.ClassUtil;

import java.math.BigDecimal;

/**
 * 将数据库的包装类转化为基本数据类型
 * Created by yizijun on 2017/7/3 0003.
 */
public class ChangePackageToBasic extends ChangeType {

    @Override
    public <T> T changeType(Class<T> type, Object value) {
        //先获得源值（数据库的值）的类类型和目标类类型的下标
        int srcIndex = JudgeType.judgePackageType(value);
        int destIndex = JudgeType.judgeBasicType(type);

        if (srcIndex == -1 || destIndex == -1)
            throw new ClassCastException("类型不一致，不能够转换");

        return ClassUtil.getBasicValue(type, value.getClass(), value);
    }

    public static void main(String[] args) {
        byte n = new ChangePackageToBasic().changeType(byte.class, new BigDecimal(1));
        System.out.println(n);
    }
}
