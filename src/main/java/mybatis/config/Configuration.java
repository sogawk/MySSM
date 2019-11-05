package mybatis.config;

import mybatis.mapper.MapperRegister;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
//    配置路径
    private String configLocation;
//    获取配置属性
    private Properties configProperties;
//    数据源（驱动， name， password）
    private MyDataSource myDataSource;
//    扫描mapper注册（<String, sql>）
    private MapperRegister mapperRegister;

    public Configuration(String configLocation) {
        this.configLocation = configLocation;
        init();
    }

    private void init() {
        loadConfigProperties();

        initDataSource();

        loadMapperRegister();
    }

    private void loadConfigProperties(){
        if (configLocation == null) {
            throw new RuntimeException("configLocation 不为空");
        }
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(configLocation);
        try {
            this.configProperties = new Properties();
            this.configProperties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDataSource() {
        myDataSource = new MyDataSource();
        myDataSource.setDriver(configProperties.getProperty("driverClass"));
        myDataSource.setUrl(configProperties.getProperty("url"));
        myDataSource.setUserName(configProperties.getProperty("name"));
        myDataSource.setPassword(configProperties.getProperty("password"));
    }

    private void loadMapperRegister() {

    }

    public MyDataSource getMyDataSource() {
        return myDataSource;
    }
}
