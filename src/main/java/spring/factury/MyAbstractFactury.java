package spring.factury;

import org.dom4j.DocumentException;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IOC容器初始化行为定义抽象类
 */
public abstract class MyAbstractFactury {
//IOC容器
    protected static Map<String, Object> beanMap = new ConcurrentHashMap<>();
//配置类
    protected static Class myConfig;
//扫描xml文件
    protected abstract void loadXml(String rootClass) throws DocumentException;
//加载配置类
    protected abstract void loadConfig(String rootClass);
//扫描并加载组件
    protected abstract void scanComponent() throws URISyntaxException;
//装配组件
    protected abstract void autowiredComponent();
//加载切面类型
    protected abstract void loadAspect();
//@Controller注入web容器 <url， method>
    protected abstract void loadRequestMapping();
}
