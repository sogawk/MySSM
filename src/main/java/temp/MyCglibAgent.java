package temp;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyCglibAgent implements MethodInterceptor {

    private Object proxy;

    /**
     *
     * @param proxy 被代理类
     * @return 代理对象
     */
    public Object getInstance(Object proxy) {
        this.proxy = proxy;
        Enhancer enhancer = new Enhancer();
//        设置父类
        enhancer.setSuperclass(this.proxy.getClass());
//        设置enhancer回调对象
        enhancer.setCallback(this);
//        创建代理对象
        Object res = enhancer.create();
        Field[] declaredFields = proxy.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {

        }
        return res;
    }

    /**
     *
     * @param o 生成的代理对象
     * @param method 被代理对象方法
     * @param objects 方法参数
     * @param methodProxy 代理方法
     * @return
     * @throws Throwable
     */
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before__________");
        Object ret = methodProxy.invokeSuper(o, objects);
        System.out.println("after____________");
        return ret;
    }
}