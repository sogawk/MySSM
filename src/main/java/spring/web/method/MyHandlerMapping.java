package spring.web.method;

import com.alibaba.fastjson.JSONObject;
import spring.web.annotation.MyJson;
import spring.web.annotation.MyRequestMapping;
import spring.web.annotation.MyRequestParam;
import utils.CoverType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class MyHandlerMapping {
//    mvc容器，存放<url, 方法（method，bean）>
    private static Map<String, MyHandlerMethod> handlerMap = new HashMap<>();

    public void request(HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException, ServletException {
        response.setContentType("text/html; charset=utf-8");
        String url = request.getRequestURI();
        if (!handlerMap.containsKey(url)) {
            response.getWriter().println("404");
        }

        MyHandlerMethod handlerMethod = handlerMap.get(url);
        Object[] args = initParameter(request, response, handlerMethod.method);

        Object result = handlerMethod.method.invoke(handlerMethod.bean, args);
//返回json串
        if (handlerMethod.method.isAnnotationPresent(MyJson.class)) {
            response.setContentType("text/json: charset=utf-8");
            String resJson = JSONObject.toJSONString(result);
            response.getWriter().println(resJson);
        } else {
//页面跳转
            if (result instanceof String) {
                request.getRequestDispatcher(result.toString()).forward(request, response);
            } else if (result instanceof MyModelAndView) {
                MyModelAndView myModelAndView = (MyModelAndView) result;
                for (String key : myModelAndView.keySet()) {
                    Object o = myModelAndView.get(key);
                    request.setAttribute(key, o);
                }
                System.out.println("页面跳转：" + myModelAndView.getViewName());
                request.getRequestDispatcher(myModelAndView.getViewName()).forward(request, response);
            }
        }
    }

    public static void registerMapping(Class clazz, Object bean) {
        MyRequestMapping myRequestMapping = (MyRequestMapping) clazz.getAnnotation(MyRequestMapping.class);
        String url = myRequestMapping.value();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping myRequestMapping1 = method.getAnnotation(MyRequestMapping.class);
                String url1 = myRequestMapping1.value();
                MyHandlerMethod myHandlerMethod = new MyHandlerMethod(method, bean);
                handlerMap.put(url + url1, myHandlerMethod);
            }
        }
    }

    private Object[] initParameter(HttpServletRequest request, HttpServletResponse response, Method method) {
        Parameter[] parameters = method.getParameters();
        Map<String, String[]> paramMap = request.getParameterMap();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < args.length; i++) {
            if (parameters[i].getType() == HttpServletRequest.class) {
                args[i] = request;
            } else if (parameters[i].getType() == HttpServletResponse.class) {
                args[i] = response;
            } else if (parameters[i].getType() == MyModelAndView.class) {
                args[i] = new MyModelAndView();
            } else if (parameters[i].isAnnotationPresent(MyRequestParam.class)) {
                MyRequestParam myRequestParam = parameters[i].getAnnotation(MyRequestParam.class);
                String key = myRequestParam.value();
                if (paramMap.containsKey(key)) {
                    if (paramMap.get(key).length == 1) {
                        args[i] = paramMap.get(key)[0];
                    } else {
                        args[i] = paramMap.get(key);
                    }
                }
            } else {
                try {
                    Object object = parameters[i].getType().getConstructor().newInstance();
                    Field[] fields = parameters[i].getType().getDeclaredFields();
                    for (Field field : fields) {
                        String key = field.getName();
                        if (paramMap.containsKey(key)) {
                            field.setAccessible(true);
                            if (paramMap.get(key) != null) {
                                field.set(object, CoverType.coverTypeTo(paramMap.get(key)[0], field.getType()));
                            }
                        }
                    }
                    args[i] = object;

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return args;
    }

}
