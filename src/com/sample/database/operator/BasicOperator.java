package com.sample.database.operator;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sample.database.DataBase;
import com.sample.types.GenerateTypes;
import com.sample.types.JudgeType;
import com.sample.util.ClassUtil;

/**
 * 基本数据类型的实现类
 * @author yizijun
 *
 */
public class BasicOperator extends Operator {

	/**
	 * index是在types.GenerateTypes中指定的基本数据类型的位置，
	 * 这样我们就可以方便的获取基本数据的类类型和它们包装类的类类型
	 */
	private int index;
	/**
	 * 每次从数据库取出来的值就先保存在这个成员变量上面
	 */
	private Object value;

	public BasicOperator() {}

	public BasicOperator(int index) {
		this.index = index;
	}

	/**
	 * 将用户需要得到的类型的下标保存在index这个变量里面，如果GenerateTypes里面
	 * 没有用户需要的数据，这里会保存-1
	 * @param c
	 */
	private <T> void setIndex(Class<T> c) {
		if(c != null) {
			this.index = JudgeType.judgeType(c);
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * 这里主要是为了解决一类问题：如果数据库得到的数据为Long型，而用户希望的数据为int类型
	 * 那么这个时候，我们可以帮用户完成这个
	 * @return
	 */
	private <T> T changeValueClass() {

		T value = null;

		Class<T> dest = (Class<T>) GenerateTypes.getPackage(this.index);
		Class<T> src = (Class<T>) GenerateTypes.getBasic(this.index);
		Constructor<T> constructor = ClassUtil.getConstructor(dest, src);

		if(!dest.isInstance(this.value)) {
			int theValueIndex = JudgeType.judgeType(this.value);

			if(-1 != theValueIndex) {
				Class<?> thePackageValue = GenerateTypes.getPackage(theValueIndex);
				this.value = ClassUtil.getBasicValue(src, thePackageValue, this.value);

				try {
					value = constructor.newInstance(this.value);
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}

		}

		//如果用户不是传的基本数据，我们只能说用户是传了它自己喜欢的数据，所以我们这里就强转
		if(null == value) {
			value = (T)this.value;
		}

		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Object> T getObject(ResultSet res, Class<T> obj) {

		setIndex(obj);

		try {
			if(res.next())
				setParamValues(res,null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//不是基本数据类型是不能够转的。到时候，只能由用户自动判断强转
		if(-1 != this.index && null != obj) {
			return changeValueClass();
		} else {
			return (T)this.value;
		}


	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Object> List<T> getList(ResultSet res, Class<T> obj) {


		setIndex(obj);

		List<T> result = new ArrayList<T>();
		T value = null;

		try {
			while (res.next()) {

				setParamValues(res, null);

				//不是基本数据类型是不能够转的。到时候，只能由用户自动判断强转
				if(-1 != this.index && null != obj) {
					value = changeValueClass();
					result.add(value);
				} else {
					result.add((T) this.value);
				}


			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public <T> void setParamValues(ResultSet res, T type) {

		ResultSetMetaData metaData = DataBase.getMetaData(res);
		String columnName = null;
		//String ColumnClassName = null;

		try {
			columnName = metaData.getColumnName(1);
			//ColumnClassName = metaData.getColumnClassName(1);

			//System.out.println(ColumnClassName);


		} catch (SQLException e) {
			e.printStackTrace();
		}
		value = DataBase.getValue(res, columnName);

		//我们这里还是通过反射机制来获取值，先不通过上面注释的方法
		//value = DataBase.getColumnValue(res, columnName, ColumnClassName);
	}

}
