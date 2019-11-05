package test.dao;

import mybatis.annotation.MyParam;
import mybatis.annotation.MySelect;
import test.bean.User;

public interface UserDao {
    @MySelect("select id, name, password from user where id = #{id}")
    User selectUser(@MyParam("id") int id);

    void updateUser(User user);
}
