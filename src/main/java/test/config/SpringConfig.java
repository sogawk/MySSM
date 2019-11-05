package test.config;

import spring.ioc.annotation.MyBean;
import spring.ioc.annotation.MyComponentScan;
import spring.ioc.annotation.MyConfiguration;
import spring.ioc.annotation.MyValue;
import test.bean.A;

@MyComponentScan("/test")
@MyConfiguration
public class SpringConfig {

    @MyValue("1")
    int id;

    @MyBean
    public A hai() {
        return new A();
    }
}
