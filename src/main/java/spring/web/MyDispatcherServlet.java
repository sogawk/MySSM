package spring.web;

import spring.context.MyApplicationContext;
import spring.context.MyApplicationContextImpl;
import spring.web.method.MyHandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class MyDispatcherServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("init初始化");
        Properties properties = doLoadConfig();
        String rootClass = properties.getProperty("webRoot");

        try {
            MyApplicationContext myApplicationContext = new MyApplicationContextImpl(rootClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MyHandlerMapping myHandlerMapping = new MyHandlerMapping();
        try {
            myHandlerMapping.request(req, resp);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Properties doLoadConfig() {
        Properties properties = new Properties();

        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("spring.properties"));
            return properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
