package com.sample.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.sample.annotation.Column;
import com.sample.annotation.Include;
import com.sample.types.JudgeType;
import com.sample.types.change.ChangeDateToString;
import com.sample.types.change.ChangePackageToBasic;
import com.sample.types.change.ChangePackageToString;
import com.sample.types.change.ChangeStringToBasic;


public class ClassUtil {

    /**
     * 获取一个类中含有指定注解的成员变量集合
     *
     * @param obj     指定的类对象
     * @param column  指定的Column注解
     * @param include 指定的Include注解
     * @return
     */
    public static <T> List<Field> getFieldsByAnnotation(Class<T> obj, Class<? extends Annotation> column,
                                                        Class<? extends Annotation> include) {
        List<Field> res = new ArrayList<>();
        getFieldsByAnnotation(res, obj, column, include);
        return res;
    }

    /**
     * @param res
     * @param obj
     * @param column
     * @param include
     * @param <T>
     * @return
     */
    private static <T> List<Field> getFieldsByAnnotation(List<Field> res, Class<T> obj, Class<? extends Annotation> column, Class<? extends Annotation> include) {
        Field[] fields = obj.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(column)) {
                res.add(field);
            }
            //如果一个类的成员变量上面有Include这个注解的话，那么我们就要循环遍历这个注解指定的类了。
            if (field.isAnnotationPresent(Include.class)) {
                getFieldsByAnnotation(res, getIncludeAnnotationValue(field), column, include);
            }
        }
        return res;
    }

    /**
     * 返回一个成员变量上面含有Include注解的值
     *
     * @param field 成员变量
     * @return 这个成员变量上面含有Include注解的值
     */
    private static Class<?> getIncludeAnnotationValue(Field field) {
        Include include = field.getAnnotation(Include.class);
        return include.includeClass();
    }

    /**
     * 通过一个成员变量数组，获得此成员变量数组中含有特定注解的value()值，这里设计得不是很好
     * 这里我们写死了，只能获得Column这个注解的值
     *
     * @param fields
     * @param annotation
     * @return
     */
    public static List<String> getAnnotationName(List<Field> fields, Class<? extends Annotation> annotation) {
        List<String> res = new ArrayList<>();
        if (fields != null && fields.size() > 0) {
            for (int i = 0, len = fields.size(); i < len; ++i) {
                Annotation anno = fields.get(i).getAnnotation(annotation);
                if (anno instanceof Column) {
                    Column annota = (Column) anno;
                    res.add(annota.value());
                }
            }
        }
        return res;
    }


    /**
     * 调用实体类指定成员变量的setter方法
     *
     * @param obj   实体类
     * @param field 成员变量
     * @param value 设定的value值
     */
    public static <T> void setValue(T obj, Field field, Object value) {
        Class<?> c = obj.getClass();
        String fieldName = field.getName();
        Class<?> fieldType = field.getType();
        //防止属性和数据库类型不一致，我们这里尝试帮用户转换数据类型
        value = tryChangeValueType(fieldType, value);
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() +
                fieldName.substring(1);
        Method method = null;
        try {
            method = c.getMethod(methodName, fieldType);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            method.invoke(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试帮用户转换数据类型
     *
     * @param src
     * @param value
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T tryChangeValueType(Class<T> src, Object value) {
        //如果数据库获取的对象能够与用户的对象相匹配，我们就直接强转。
        if (src.isInstance(value)) {
            return (T) value;
        }
        try {
            //1 change Date to String
            if (String.class == src
                    && -1 != JudgeType.judgeDateType(value))
                return new ChangeDateToString().changeType(src, value);
                //2.change package to basic
            else if (-1 != JudgeType.judgePackageType(value)
                    && -1 != JudgeType.judgeType(src))
                return new ChangePackageToBasic().changeType(src, value);
                //3.change package to String
            else if (-1 != JudgeType.judgePackageType(value)
                    && String.class == src)
                return new ChangePackageToString().changeType(src, value);
                //4.change String to basic
            else if (String.class == value.getClass()
                    && -1 != JudgeType.judgeBasicType(src))
                return new ChangeStringToBasic().changeType(src, value);
            else
                throw new ClassCastException("数据类型转化异常");
        } catch (ClassCastException e) {
            throw e;
        }
    }

    /**
     * 通过这个类的类类型，来获得这个类的对象,注意这个类中必须要有一个无参的构造方法
     *
     * @param c
     * @return
     */
    public static <T> T getInstance(Class<T> c) {
        try {
            return c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定的类的成员变量的值
     *
     * @param obj     类对象
     * @param varName 成员变量名称
     * @return 成员变量的值
     */
    public static <T> Object getValue(T obj, String varName) {
        Class<?> c = obj.getClass();
        Object value;
        String methodName = "get" + varName.substring(0, 1).toUpperCase() +
                varName.substring(1);
        try {
            Method method = c.getMethod(methodName);
            value = method.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return value;
    }


    /**
     * 获得基本类型的包装类的构造方法
     *
     * @param c      包装类类类型
     * @param params 包装类的构造方法的参数列表
     * @return
     */
    public static <T> Constructor<T> getConstructor(Class<T> c, Class<?>... params) {
        Constructor<T> constructor = null;
        try {
            constructor = c.getConstructor(params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return constructor;
    }

    /**
     * 通过反射获取包装类型所对应的基本数据类型，这里我们其实是调用的api的basicDataTypeName + Value
     * 这个方法
     *
     * @param dest  基本数据类型的Class对象
     * @param src   包装类型的Class对象
     * @param value 设置的值
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBasicValue(Class<T> dest, Class<?> src, Object value) {
        //这里获取的为基本数据类型的名字
        String typeName = dest.getSimpleName();
        String methodName = typeName + "Value";
        T basicValue = null;
        Method method;
        try {
            method = src.getMethod(methodName);
            try {
                basicValue = (T) method.invoke(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return basicValue;
    }


    /**
     * 判断指定的类上面是否含有指定的注解
     *
     * @param c          指定类的类类型
     * @param annotation 注解的类型
     * @return 指定的类上面有这个注解返回true，否则返回false
     */
    public static boolean hasAnnotation(Class<?> c, Class<? extends Annotation> annotation) {
        if (c.getAnnotation(annotation) != null)
            return true;
        return false;
    }

    /**
     * 获取指定类型上面指定方法名的Method
     *
     * @param type
     * @param methodName
     * @return
     */
    public static Method getMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
        try {
            return type.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
