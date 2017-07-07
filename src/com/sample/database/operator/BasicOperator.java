package com.sample.database.operator;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sample.database.DataBase;
import com.sample.util.ClassUtil;

/**
 * 基本数据类型的实现类
 * @author yizijun
 */
public class BasicOperator extends Operator {

    /**
     * 每次从数据库取出来的值就先保存在这个成员变量上面
     */
    private Object value;


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(ResultSet res, Class<T> src) {
        try {
            if (res.next()) {
                setParamValues(res, null);
                return ClassUtil.tryChangeValueType(src, this.value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public <T> List<T> getList(ResultSet res, Class<T> obj) {
        List<T> result = new ArrayList<>();
        try {
            while (res.next()) {
                setParamValues(res, null);
                result.add(ClassUtil.tryChangeValueType(obj, this.value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected <T> void setParamValues(ResultSet res, T type) {
        ResultSetMetaData metaData = DataBase.getMetaData(res);
        String columnName = null;
        try {
            columnName = metaData.getColumnName(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        value = DataBase.getValue(res, columnName);
        //我们这里还是通过反射机制来获取值，先不通过上面注释的方法
        //value = DataBase.getColumnValue(res, columnName, ColumnClassName);
    }

}
