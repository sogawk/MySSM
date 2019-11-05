package spring.context;

import spring.factury.MyBeanFactury;

import java.util.Map;

/**
 * ApplicationContext的接口
 */
public interface MyApplicationContext extends MyBeanFactury {

    /**
     * 注册一个bean
     * @param name
     * @param object
     * @return
     */
    boolean register(String name, Object object);

    /**
     * 返回ioc容器
     * @return
     */
    Map<String, Object> getIoc();

}
