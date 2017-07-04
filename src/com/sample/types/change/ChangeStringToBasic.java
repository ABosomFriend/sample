package com.sample.types.change;

import com.sample.types.GenerateTypes;
import com.sample.types.JudgeType;
import com.sample.util.ClassUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 将数据库的字符串类型转化为基本类型
 * Created by yizijun on 2017/7/3 0003.
 */
public class ChangeStringToBasic extends ChangeType {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T changeType(Class<T> type, Object value) {
        int destIndex = JudgeType.judgeBasicType(type);
        if(-1 == destIndex || value.getClass() != String.class)
            throw new ClassCastException("类型不一致，不能够转换");
        /*
         *这里我们实际是调用的为每个包装类中的valueOf(String s)方法
         */
        Method method = ClassUtil.getMethod(GenerateTypes.getPackage(destIndex),
                "valueOf", String.class);
        if(method != null) {
            try {
                return (T) method.invoke(type, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
