package spring.web.method;

import java.util.HashMap;

public class MyModelAndView extends HashMap<String, Object> {
    private String ViewName;

    public MyModelAndView() {
        this("");
    }

    public MyModelAndView(String viewName) {
        this.ViewName = viewName;
    }

    public String getViewName() {
        return ViewName;
    }

    public void setViewName(String viewName) {
        ViewName = viewName;
    }

    public boolean addObject(String key, String value) {
        return super.put(key, value) == null;
    }
}
