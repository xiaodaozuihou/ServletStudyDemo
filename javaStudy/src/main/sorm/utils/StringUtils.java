package utils;

public class StringUtils {
    public static String firstCharToUpperCase(String str){
        return str.toUpperCase().substring(0,1) + str.substring(1);
    }
}
