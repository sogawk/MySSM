package spring.aop.agent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyJdkAgent {

    static class MyHandler implements InvocationHandler {
        private Object proxy;

        public MyHandler(Object proxy) {
            this.proxy = proxy;
        }

        /**
         *
         * @param proxy 被代理类实例
         * @param method 调用被代理的类的方法
         * @param args 该方法所需要的参数
         * @return
         * @throws Throwable
         */
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("hehe");
            Object res = method.invoke(this.proxy, args);
            System.out.println("haha");
            return res;
        }
    }

    /**
     *
     * @param beProxied 被代理类
     * @param interfaces 被代理类的接口数组
     * @param proxy 被代理类的实例
     * @return
     */
    public static Object getAgent(Class beProxied, Class[] interfaces, Object proxy) {
        /**
         * 参数分别是（被代理类加载器，被代理类接口数组，处理器类的对象）
         */
        return Proxy.newProxyInstance(beProxied.getClassLoader(), interfaces, new MyHandler(proxy));
    }
}
