package mybatis.mapper;

public class MapperData {
    private String sql;
    private Class type;

    public MapperData() {
    }

    public MapperData(String sql, Class type) {
        this.sql = sql;
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
