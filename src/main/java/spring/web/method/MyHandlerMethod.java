package spring.web.method;

import java.lang.reflect.Method;

public class MyHandlerMethod {
    final Method method;
    final Object bean;

    public MyHandlerMethod(Method method, Object bean) {
        this.method = method;
        this.bean = bean;
    }
}
