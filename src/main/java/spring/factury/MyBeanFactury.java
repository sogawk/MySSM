package spring.factury;

/**
 * IOC容器的顶层接口
 */
public interface MyBeanFactury {
    boolean containsBean(String beanName);

    Object getBean(String beanName);

    <T> T getBean(Class<T> type);
}
