package com.sample.types.change;

import com.sample.types.GenerateTypes;
import com.sample.types.JudgeType;
import com.sample.util.ClassUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;

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

        /*
          通过这种方式能够解决下面数据类型的自动转化问题：
          Byte.class,Short.class,Integer.class,Long.class,Float.class,Double.class,
          byte.class,short.class,int.class,long.class,float.class,double.class,
          但是不能够解决下面这两种包装类型的数据类型转化问题
          1.BigDecimal   2.BigInteger
          因为我们这个方法实际上是通过源包装类中的xxxValue()方法反射实现的。而这里虽然这种
          可能非常的小，但是我们还是要注意这个Bug带来的问题。
          比如：
          我们数据库获取出来的类型为Integer，但是用户就是想转化为BigInteger怎么办呢？
          如果我们就按照下面这种思路的话。
          由于BigInteger我们对应的基本类型为long，所以其会调用Integer中的longValue(),将
          我们数据库的Integer类型转化为long类型。而这个时候我们再将long类型强转为BigInteger
          类型的话，其就会抛出java.lang.Long cannot be cast to java.math.BigInteger这个
          异常。

          所以为了解决上面这两个数据类型的转化问题，我们通过调用他们包装类的构造方法重新构造
          一个BigInteger或者BigDecimal的数据类型就可以解决了。
          比如：
                BigDecimal(String val)
         */

        //TODO  这里的第一个参数Class<T>用到不太完美
        Object basicValue = ClassUtil.getBasicValue((Class<T>) GenerateTypes.getBasic(destIndex),
                value.getClass(), value);


        if(type == BigDecimal.class || type == BigInteger.class) {
            return afterGetBasicValue(basicValue, destIndex);
        } else {
            return (T) basicValue;
        }
    }

    /**
     * 获得基本数据值后的后处理方法
     * @param basicValue  基本数据值
     * @param destIndex  目标类型的位置
     * @param <T>
     * @return 转化后的值
     */
    private <T> T afterGetBasicValue(Object basicValue, int destIndex) {
        //我们将这个基本数据类型转化为字符串，因为BigInteger和BigDecimal都有这个构造方法
        String ss = String.valueOf(basicValue);
        Constructor<T> constructor = ClassUtil.getConstructor((Class<T>)GenerateTypes.getPackage(destIndex),
                String.class);
        T result = null;
        try {
            result = constructor.newInstance(ss);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}
