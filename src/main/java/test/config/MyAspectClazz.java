package test.config;

import spring.aop.annocation.MyAspect;
import spring.aop.annocation.MyBefore;
import spring.aop.annocation.MyPointCut;
import spring.ioc.annotation.MyComponent;

@MyComponent
@MyAspect
public class MyAspectClazz {

    @MyPointCut("test.Controller.UserController_test")
    public void point2() {

    }

    @MyPointCut("test.bean.User_sayHello")
    public void point1() {

    }

    @MyBefore("point1()")
    public void before() {
        System.out.println("this is the before method......");
    }

    @MyBefore("point2()")
    public void before2() {
        System.out.println("this is the UserController test  before method________");
    }
}
