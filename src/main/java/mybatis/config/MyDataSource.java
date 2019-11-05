package mybatis.config;

public class MyDataSource {
    private String url;
    private String driver;
    private String userName;
    private String password;

    public MyDataSource() {
    }

    public MyDataSource(String url, String driver, String userName, String password) {
        this.url = url;
        this.driver = driver;
        this.userName = userName;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
