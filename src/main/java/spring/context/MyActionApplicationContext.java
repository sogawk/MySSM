package spring.context;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import spring.aop.agent.MyCglibAgent;
import spring.aop.annocation.MyAspect;
import spring.aop.annocation.MyBefore;
import spring.aop.annocation.MyPointCut;

import spring.aop.aspect.PointCut;

import spring.factury.MyAbstractFactury;

import spring.ioc.annotation.*;
import spring.web.method.MyHandlerMapping;
import utils.CoverType;
import utils.SpringUtil;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现Ioc容器的初始化行为
 */
public class MyActionApplicationContext extends MyAbstractFactury {
    //代理容器
    private Map<String, Object> proxyMap = new ConcurrentHashMap<>();

    @Override
    protected void loadXml(String rootClass) {

        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(this.getClass().getClassLoader().getResourceAsStream(rootClass));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Element rootElement = document.getRootElement();
        if (rootElement == null) {
            System.out.println("bean的xml配置文件为空");
            return;
        }
        List beans = rootElement.elements("bean");
        Iterator iterator = beans.iterator();
        try {
            while (iterator.hasNext()) {
                Element beanElement = (Element) iterator.next();
                String beanId = beanElement.attributeValue("id");
                String className = beanElement.attributeValue("class");
                Class beanCLass = Class.forName(className);
                Object bean = beanCLass.newInstance();

                Iterator propertyIterator = beanElement.elementIterator();
                while (propertyIterator.hasNext()) {
                    Element propertyElement = (Element) propertyIterator.next();
                    String name = propertyElement.attributeValue("name");
                    String value = propertyElement.attributeValue("value");


                    Field declaredField = beanCLass.getDeclaredField(name);
                    Class type = declaredField.getType();
                    declaredField.setAccessible(true);

                    if (value != null && value.length() > 0) {

                        declaredField.set(bean, CoverType.coverTypeTo(value, type));

                    } else {
                        String ref = propertyElement.attributeValue("ref");
                        declaredField.set(bean, beanMap.get(ref));
                    }

                }

                beanMap.put(beanId, bean);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void loadConfig(String configLocation) {
        try {
            Class Config = Class.forName(configLocation);
            if (!Config.isAnnotationPresent(MyConfiguration.class)) {
                System.out.println("spring的配置类加载错误");
                return;
            }
            myConfig = Config;
            Method[] methods = Config.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(MyBean.class)) {
                    String key = SpringUtil.methodNameTobeenMapKey(method.getName());
                    Object o = method.invoke(Config.newInstance());
                    if (o != null) {
                        beanMap.put(key, o);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("spring配置类不存在");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void scanComponent() {
        String path = null;
        if (myConfig.isAnnotationPresent(MyComponentScan.class)) {
            MyComponentScan myComponentScan = (MyComponentScan) myConfig.getAnnotation(MyComponentScan.class);
            path = myComponentScan.value();
        }
        Queue<String> files = scanPackage(path);
        for (String className : files) {
            String key = coverRelaToKey(className);
            try {
                Class beanClass = Class.forName(className);

                if (beanClass.isAnnotationPresent(MyController.class) ||
                        beanClass.isAnnotationPresent(MyService.class) ||
                        beanClass.isAnnotationPresent(MyRepository.class) ||
                        beanClass.isAnnotationPresent(MyComponent.class) ||
                        beanClass.isAnnotationPresent(MyConfiguration.class)
                ) {
                    Object o = beanClass.getConstructor().newInstance();
                    beanMap.put(key, o);
                }


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    //@Controller注入web容器 <url， method>
    @Override
    protected void loadRequestMapping() {
        for (String key : beanMap.keySet()) {
            Object object = beanMap.get(key);
            Class clazz = object.getClass();
            if (clazz.isAnnotationPresent(MyController.class)) {
                Object proxy = proxyMap.get(key);
                if (proxy != null) {
                    object = proxy;
                }
                MyHandlerMapping.registerMapping(clazz, object);
            }
        }
    }

    @Override
    protected void autowiredComponent() {
        loadMyValue();
        loadAutoWired();
    }

    protected void loadAutoWired() {
        for (String key : beanMap.keySet()) {
            Object object = beanMap.get(key);
            Class clazz = object.getClass();
            Object objectProxy = proxyMap.get(key);
            if (objectProxy != null) {
                object = objectProxy;
            }

            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(MyAutowired.class)) {
                    Class type = declaredField.getType();
                    Object bean = getBean(type);
                    String proxyKey = getBeanKey(bean);
                    objectProxy = proxyMap.get(proxyKey);
                    if (objectProxy != null) {
                        bean = proxyMap.get(proxyKey);
                    }
                    if (bean == null) {
                        System.out.println("组件类型不存在:" + type);
                    }
//                    object是类，bean是object这个类（有@AutoWire）的字段。将object重新赋值
                    try {
                        declaredField.setAccessible(true);
                        declaredField.set(object, bean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    protected void loadMyValue() {
        for (String key : beanMap.keySet()) {
            Object object = beanMap.get(key);
            Class clazz = object.getClass();

            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(MyValue.class)) {
                    MyValue myValue = declaredField.getAnnotation(MyValue.class);
                    String fileValue = myValue.value();
                    Class type = declaredField.getType();
                    try {
                        declaredField.setAccessible(true);
                        declaredField.set(object, CoverType.coverTypeTo(fileValue, type));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void loadAspect() {
        for (String key : beanMap.keySet()) {
            Object object = getBean(key);
            Class clazz = object.getClass();
            if (clazz.isAnnotationPresent(MyAspect.class)) {
//                <pointCut的方法名， pointCut>
                HashMap<String, PointCut> pointCuts = getPointCuts(clazz);
                generateProxy(pointCuts);
            }
        }
    }

    private void generateProxy(HashMap<String, PointCut> pointCuts) {
        for (String key : pointCuts.keySet()) {
            PointCut pointCut = pointCuts.get(key);
            MyCglibAgent myCglibAgent = new MyCglibAgent();

            Object proxy = myCglibAgent.getInstance(pointCut);
            Object bean = pointCut.getBean();
            proxy = copyBeanToProxy(proxy, bean);
            String classNameKey = pointCut.getBean().getClass().getName();
            classNameKey = SpringUtil.classNameTobeenMapKey(classNameKey);
            proxyMap.put(classNameKey, proxy);
        }
    }

    private HashMap<String, PointCut> getPointCuts(Class aspect) {
        Method[] methods = aspect.getMethods();
        HashMap<String, PointCut> pointCuts = new HashMap<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyPointCut.class)) {
                MyPointCut myPointCut = method.getAnnotation(MyPointCut.class);
                String value = myPointCut.value();
                PointCut pointCut = getPointCut(value);
                Object object = null;
                try {
                    object = aspect.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                pointCut.setAspect(object);
                pointCuts.put(method.getName(), pointCut);
            }
        }
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyBefore.class)) {
                MyBefore myBefore = method.getAnnotation(MyBefore.class);
                String myBeforeValue = myBefore.value().replace("()", "");
                PointCut pointCut = pointCuts.get(myBeforeValue);
                if (pointCut != null) {
                    pointCut.setAdvices(method);
                }
                // todo 和取出来再注入的区别
            }
        }
        return pointCuts;
    }

    private PointCut getPointCut(String pointCutValue) {
        try {
            String path = pointCutValue.split("_")[0];
            String method = pointCutValue.split("_")[1];
            Class clazz = Class.forName(path);
            String key = SpringUtil.classNameTobeenMapKey(clazz.getName());
            Object bean = beanMap.get(key);

            PointCut pointCut = new PointCut();
            pointCut.setMethodName(method);
            if (bean == null) {
                throw new RuntimeException("pointCut 注册失败");
            }
            pointCut.setBean(bean);
            return pointCut;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //扫描文件夹，返回类的name
    private Queue<String> scanPackage(String pkg) {
        Queue<String> queue = new LinkedList<>();
        fileFilter(pkg, queue);
        return queue;
    }

    //过滤文件，返回类名pathName（可被直接反射的类名）
    private Queue<String> fileFilter(String pkg, Queue<String> queue) {
        URI uri = null;
        try {
            uri = this.getClass().getClassLoader().getResource(pkg).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File file = new File(uri);
        File[] files = file.listFiles();
        for (File file1 : files) {
            String fileName = file1.getName();
            if (file1.isDirectory()) {
                fileFilter(pkg + "/" + fileName, queue);
            } else {

                if (fileName.endsWith(".class")) {

                    String path = file1.getAbsolutePath();
                    String relaPath = path.replaceAll("\\\\", "/")
                            .replace("D:/Program Files/apache-tomcat-7.0.90/webapps/ROOT/WEB-INF/classes/", "")
                            .replace(".class", "").replaceAll("/", ".");
                    queue.add(relaPath);
                }
            }
        }
        return queue;
    }

    //从pathName获取key值（类名首字母小写）
    private String coverRelaToKey(String className) {
        String res = className.substring(className.lastIndexOf(".") + 1);
        res = String.valueOf(res.charAt(0)).toLowerCase() + res.substring(1);
        return res;
    }

    //pathName转换为全限定类名
    private static String coverToRelaPath(String absolutePath) {
        String relaPath = absolutePath.replaceAll("\\\\", ".");
        relaPath = relaPath.replace("E:.项目.mySSM.src.main.java.", "");
        relaPath = relaPath.substring(0, relaPath.lastIndexOf("."));
        return relaPath;
    }

    //根据name获取bean
    private Object getBean(String beanName) {
        return beanMap.get(beanName);
    }

    //根据type获取bean，主要用于@AUtoWired
    private <T> T getBean(Class<T> type) {
        for (String key : beanMap.keySet()) {
            Object object = beanMap.get(key);
            if (object.getClass() == type) {
                return (T) object;
            }
        }
        return null;
    }

    private String getBeanKey(Object object) {
        for (String key : beanMap.keySet()) {
            if (beanMap.get(key).equals(object)) {
                return key;
            }
        }
        return null;
    }

    private Object copyBeanToProxy(Object proxy, Object bean) {
        try {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                System.out.println("**************************");
                System.out.println(field.get(bean));
                System.out.println(field.get(proxy));

                System.out.println("**************************");
                field.set(proxy, field.get(bean));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return proxy;
    }
}
