package mybatis.mapper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MapperRegister {
    //    <方法名， sql>
    private Map<String, MapperData> methodSqlMap = new HashMap<>();

    public void doLoadMethodSqlMapping(Properties mapperProperties) {
        Enumeration<?> propertyNames = mapperProperties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            //todo 注入方法和sql~
        }
    }
}
