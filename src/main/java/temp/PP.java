package temp;

import org.junit.Test;
import spring.ioc.annotation.MyAutowired;
import spring.ioc.annotation.MyRepository;
import test.bean.User;

@MyRepository
public class PP {

    @MyAutowired
    User user;

    @Test
    public void hehe() {
        System.out.println(user);
    }
}
