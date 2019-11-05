package test.Controller;

import spring.ioc.annotation.MyAutowired;
import spring.ioc.annotation.MyController;
import spring.web.annotation.MyJson;
import spring.web.annotation.MyRequestMapping;
import spring.web.annotation.MyRequestParam;
import test.bean.User;

@MyController
@MyRequestMapping("/user")
public class UserController {

    @MyAutowired
    User user;

    @MyRequestMapping("/login")
    public String login() {
        return "/login.jsp";
    }

    @MyRequestMapping("/doLogin")
    public String doLogin(@MyRequestParam("name") String name, @MyRequestParam("password") String password) {
        return new User(1, name, password).toString();
    }

    @MyRequestMapping("/test.do")
    @MyJson()
    public String test() {
        user.sayHello();
        return user.toString();
    }

    @MyRequestMapping("/tiaozhuan.do")
    public String tiaozhuan() {
        return "/user/test.do";
    }

    @MyRequestMapping("/getName.do")
    @MyJson
    public String getName(@MyRequestParam("name") String name) {
        return name;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
