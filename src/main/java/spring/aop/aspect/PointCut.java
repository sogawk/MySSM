package spring.aop.aspect;

import utils.SpringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class PointCut {
//    包含该pointCut的切面对象
    Object aspect;
    //   ioc容器中该类的对象
    Object bean;
    //  类的方法名
    String methodName;
    //通知
    ArrayList<Method> advices = new ArrayList<>();

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ArrayList<Method> getAdvices() {
        return advices;
    }

    public void setAdvices(Method method) {
        advices.add(method);
    }

    public Object getAspect() {
        return aspect;
    }

    public void setAspect(Object aspect) {
        this.aspect = aspect;
    }
}
