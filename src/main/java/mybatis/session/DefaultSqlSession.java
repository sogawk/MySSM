package mybatis.session;
import mybatis.config.Configuration;
import mybatis.executer.MyExecutor;
import mybatis.handler.MyUserMapperInvocationHandler;

import java.lang.reflect.Proxy;

public class DefaultSqlSession implements MySqlSession {

    static MyExecutor executor;

    public DefaultSqlSession(Configuration configuration) {
        executor = new MyExecutor(configuration);
    }

    public static <T> T getMapper(Class clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new MyUserMapperInvocationHandler(clazz, executor));
    }
}
