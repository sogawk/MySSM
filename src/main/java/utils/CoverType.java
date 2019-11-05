package utils;

public class CoverType<T> {
    public static Object coverTypeTo(String value, Class type) {
        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        }
        return value;
    }
}
