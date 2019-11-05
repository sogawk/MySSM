package mybatis.executer;

import mybatis.annotation.MySelect;

import java.lang.reflect.Method;

public interface Executor {
    Object doQuery(Method method, MySelect mySelect, Object[] args);

    void doUpdate();
}
