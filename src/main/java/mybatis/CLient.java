package mybatis;

import mybatis.session.DefaultSqlSession;
import mybatis.session.MySqlSessionFactury;
import mybatis.session.MySqlSessionFacturyBuilder;
import test.bean.User;
import test.dao.UserDao;

public class CLient {
    public static void main(String[] args) {
        MySqlSessionFactury mySqlSessionFactury = new MySqlSessionFacturyBuilder().build("myMybatis.properties");
        DefaultSqlSession sqlSession = (DefaultSqlSession) mySqlSessionFactury.openSqlSession();
        UserDao userMapper = sqlSession.getMapper(UserDao.class);
        User user = userMapper.selectUser(1);
        System.out.println(user);
    }
}
