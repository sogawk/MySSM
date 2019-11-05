package spring.aop.agent;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import spring.aop.aspect.PointCut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyCglibAgent implements MethodInterceptor {

    private PointCut pointCut;

    /**
     * @param_bean 被代理类
     * @return 代理对象
     */
// todo 静态的好使吗
    public Object getInstance(PointCut pointCut) {
        this.pointCut = pointCut;
        Object bean = pointCut.getBean();

        Enhancer enhancer = new Enhancer();
//        设置父类
        enhancer.setSuperclass(bean.getClass());
//        设置enhancer回调对象
        enhancer.setCallback(this);
//        创建代理对象
        return enhancer.create();
    }

    /**
     * @param o           生成的代理对象
     * @param method      被代理对象方法
     * @param objects     方法参数
     * @param methodProxy 代理方法
     * @return
     * @throws Throwable
     */
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)  {
        before(method.getName());
        Object ret = null;
        try {
            ret = methodProxy.invokeSuper(o, objects);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            after();
        }
        return ret;
    }

    private void before(String methodName){
        Object aspect = pointCut.getAspect();
        if (methodName.equals(pointCut.getMethodName())) {

            for (Method method : pointCut.getAdvices()){

                try {
                    method.invoke(aspect);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void after() {

    }
}
