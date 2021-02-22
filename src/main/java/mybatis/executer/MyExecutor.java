package mybatis.executer;

import mybatis.annotation.MySelect;
import mybatis.config.Configuration;
import mybatis.executer.statement.MyJDBCStatement;
import utils.CoverType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyExecutor implements Executor {

    MyJDBCStatement myStatement;

    public MyExecutor(Configuration configuration) {
        myStatement = new MyJDBCStatement(configuration);
    }

    @Override
    public Object doQuery(Method method, MySelect mySelect, Object[] args) {
        String querySql = mySelect.value();

//        sql中的形参
        List<String> paramList = myStatement.getSelectParam(querySql);
//        将sql语句处理成可以被预处理的形式
        querySql = myStatement.replaceParam(querySql, paramList);
//      获取方法参数(<name, value>)
        HashMap<String, Object> methodParam = myStatement.getMethodParam(method, args);
//      将实际参数的值放入list
        List<Object> paramValueList = new ArrayList<>();

        for (String param : paramList) {
            Object paramValue = methodParam.get(param);
            paramValueList.add(paramValue);
        }

//得到结果集
        ResultSet resultSet = myStatement.doQuery(querySql, paramValueList);
        try {
            if (!resultSet.next()) {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//将结果转换为相应的对象。
        Class returnType = method.getReturnType();
        try {
            Object object = returnType.newInstance();
            resultSet.previous();
            while (resultSet.next()) {
                Field[] declaredFields = returnType.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    String fileName = declaredField.getName();
                    String fileValue = resultSet.getString(fileName);

                    Class declaredFieldType = declaredField.getType();

                    declaredField.setAccessible(true);

                    declaredField.set(object, CoverType.coverTypeTo(fileValue, declaredFieldType));
                }
            }
            return object;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void doUpdate() {

    }






}
