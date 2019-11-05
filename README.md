# MySSM
手写实现ssm框架
========
blog地址：
####思路：
#####Spring

######Ioc：
    >>   实现注解： @MyRepository  @Service @Controller @Bean @Configuration @ComponentScan  @MyAutowired @MyValue

     >>  实现了用注解、配置类、xml 注册bean

1、ApplicationContextBuilder.build( InputStream ), 根据配置文件生成配置类

2、扫描xml，装配bean

3、扫描装配Config类

4、扫描注解（@MyRepository  @Service @Controller）

5、@MyVlaue 注入值

6、扫描切面类，注册代理类

7、@Autowired 注入，如果ProxyMap中有对应代理类，注入代理类

8、注册Controller类中的url。handerMap<url(String), method>

######Aop:

    >>   实现了：@MyAspect，@MyPointCut @MyBefore @MyAfter @MyAround

1、扫描到Aspect类

2、根据切面类中的pointCutMethod方法， 生成PointCut对象{aspect（切面对象）， advices（通知）， object(映射的指定类的对象）， Method映射的指定方法) }

3、遍历每个PointCut 中的Object对象，（PointCut）传入generateProxy（）生成代理对象

4、generateProxy（）方法中通过cglib 生成代理对象（）。

5、将ioc容器中目标类（代理的目标类）的实例对象的值，通过反射赋值给代理类

6、将生成的代理对象放入容器ProxyMap<String, Object>

7、ioc容器每次注入的时候，如果ProxyMap中有相应的代理类，就注入代理类。

#####SpringMvc

注解： @MyRequestMapping @MyJson @MyRequestParam

处理：@RequestMapping扫描注入到容器中Map<url, method>

######实现页面跳转
######返回json串
######步骤：

diapatcherServlet 拦截所有请求，初始化ioc容器（同时生成handlerMap<url(String), method > ）
分发请求，匹配handerMap中的方法
通过反射执行相应的方法，得到返回值。根据注解判断返回json还是页面跳转
Mybatis

用注解实现的映射接口的形式

SqlSessionFacturyBuilder.build( xml) 生成相应的配置类
生成SqlSessionFactury ,传入配置类。
执行OpenSession（）方法，通过配置类生成Executer
SqlSession.getMapper（Class clazz）方法，将clazz 和Executer传入指定方法生成代理对象
Executer通过StatementHandler处理sql语句
Executer通过ResultHandler处理sql语句返回结果
