package mybatis.session;

import mybatis.config.Configuration;

public class DefaultSqlSessionFactury implements MySqlSessionFactury {

    private final Configuration configuration;

    public DefaultSqlSessionFactury(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public MySqlSession openSqlSession() {
        MySqlSession sqlSession = new DefaultSqlSession(configuration);
        return sqlSession;
    }

}
