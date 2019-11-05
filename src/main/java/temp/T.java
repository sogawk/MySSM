package temp;


import test.Controller.UserController;
import test.bean.User;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;

public class T {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException {

        UserController userController = new UserController();
        userController.setUser(new User(1, "2"));
        MyCglibAgent myCglibAgent = new MyCglibAgent();
        UserController proxy = (UserController) myCglibAgent.getInstance(userController);


        Field[] fields = userController.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            System.out.println("**************************");
            System.out.println(field.get(userController));
            System.out.println(field.get(proxy));

            System.out.println("**************************");
            field.set(proxy, field.get(userController));
        }

        System.out.println("-----------------------");
        Field[] declaredFields = proxy.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getName());
        }
        System.out.println("------------------------");
        Field field = proxy.getClass().getDeclaredField("User");
        System.out.println(field);


    }


}
