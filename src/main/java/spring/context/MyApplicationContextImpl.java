package spring.context;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import spring.factury.MyBeanFactury;
import utils.CoverType;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * MyApplicationContext的实现类
 */
public class MyApplicationContextImpl extends MyActionApplicationContext implements MyApplicationContext {

    public MyApplicationContextImpl(String configLocation) {
//      扫描xml
        loadXml("mySpring.xml");
//      装配配置类
        loadConfig(configLocation);
//      扫描注解并装配
        scanComponent();
//      注入值 @MyValue
        loadMyValue();
//       加载切面类
        loadAspect();
//      注入值，实现@MyAutoWire
        loadAutoWired();
//       扫描@requestMapping
        super.loadRequestMapping();
    }

    @Override
    public boolean register(String name, Object object) {
        return beanMap.put(name, object) == null ? false : true;
    }

    @Override
    public Map<String, Object> getIoc() {
        return beanMap;
    }

    @Override
    public boolean containsBean(String beanName) {
        return beanMap.containsKey(beanName);
    }

    @Override
    public Object getBean(String beanName) {
        return beanMap.get(beanName);
    }

    @Override
    public <T> T getBean(Class<T> type) {
        for (String key : beanMap.keySet()) {
            Object object = beanMap.get(key);
            if (object.getClass() == type) {
                return (T) object;
            }
        }
        return null;
    }

}
