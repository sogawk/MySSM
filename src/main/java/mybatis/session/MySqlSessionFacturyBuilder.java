package mybatis.session;

import mybatis.config.Configuration;

public class MySqlSessionFacturyBuilder {

    public MySqlSessionFactury build(String configLocation) {

        Configuration configuration = getConfig(configLocation);
        return new DefaultSqlSessionFactury(configuration);
    }

    private Configuration getConfig(String configLocation) {
        return new Configuration(configLocation);
    }

}
