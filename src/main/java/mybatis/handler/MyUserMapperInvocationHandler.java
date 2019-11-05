package mybatis.handler;

import mybatis.annotation.MyParam;
import mybatis.annotation.MySelect;
import mybatis.executer.MyExecutor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class MyUserMapperInvocationHandler implements InvocationHandler {

    private Class userMapperClazz;

    private MyExecutor myExecutor;

    public MyUserMapperInvocationHandler(Class userMapperClazz, MyExecutor myExecutor) {
        this.userMapperClazz = userMapperClazz;
        this.myExecutor = myExecutor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        MySelect mySelect = method.getDeclaredAnnotation(MySelect.class);

        if (mySelect != null) {
            return myExecutor.doQuery(method, mySelect, args);
        }

        return null;
    }
}
