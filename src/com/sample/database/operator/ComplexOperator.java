package com.sample.database.operator;

import java.lang.reflect.Field;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sample.annotation.Column;
import com.sample.annotation.Include;
import com.sample.database.DataBase;
import com.sample.util.ClassUtil;

/**
 * 完成数据库中的记录到javabean的映射
 *
 * @author yizijun
 */
public class ComplexOperator extends Operator {

    private List<Field> fields;

    private List<String> annotationNames;

    public <T> ComplexOperator(Class<T> obj) {
        // 获得含有注解的全部成员变量，也就是注解名和数据库名称一样的成员变量
        fields = ClassUtil.getFieldsByAnnotation(obj, Column.class, Include.class);
        // 然后获得与fields一一对应的注解的value()
        annotationNames = ClassUtil.getAnnotationName(fields, Column.class);
    }

    @Override
    public <T> T getObject(ResultSet res, Class<T> obj) {
        T entity = ClassUtil.getInstance(obj);
        try {
            if (res.next()) {
                setParamValues(res, entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public <T> List<T> getList(ResultSet res, Class<T> obj) {
        List<T> entities = new ArrayList<>();
        T entity;
        try {
            while (res.next()) {
                entity = ClassUtil.getInstance(obj);
                setParamValues(res, entity);
                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    protected <T> void setParamValues(ResultSet res, T entity) {
        ResultSetMetaData metaData = DataBase.getMetaData(res);
        try {
            for (int i = 1, columnCount = metaData.getColumnCount(); i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                // 如果实体类也有这个字符串，然后我们就可以调用set方法赋值了
                int position = annotationNames.indexOf(columnName);
                if (-1 != position) {
                    // 然后获得该列的值
                    Object value = DataBase.getValue(res, columnName);
                    //最后通过反射调用setter方法
                    ClassUtil.setValue(entity, fields.get(position),
                            value);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
