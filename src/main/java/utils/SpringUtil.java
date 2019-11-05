package utils;

public class SpringUtil {
    public static String classNameTobeenMapKey(String name) {
        name = name.substring(name.lastIndexOf(".") + 1);
        return String.valueOf(name.charAt(0)).toLowerCase() + name.substring(1);
    }

    public static String methodNameTobeenMapKey(String name) {
        return String.valueOf(name.charAt(0)).toLowerCase() + name.substring(1);
    }

    public static String coverToRelative(String string) {
        return "";
    }
}
