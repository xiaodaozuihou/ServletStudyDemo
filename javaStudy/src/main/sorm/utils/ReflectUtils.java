package utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    /**
     * 调用object对象对应属性fieldName的get方法
     * @param fieldName
     * @param object
     * @return
     */
    public static Object invokeGet(String fieldName, Object object) {
        try {
            Class c = object.getClass();
            Method m = c.getDeclaredMethod("get" + StringUtils.firstCharToUpperCase(fieldName), null);
            return m.invoke(object, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void invokSet(Object object,String columnName,Object columnValue){
        //调用rowObj对象的setUsername(String uname)方法，将columnValue的值设置进去
        try {
            if (columnValue != null){
                Method m = object.getClass().getDeclaredMethod("set" + StringUtils.firstCharToUpperCase(columnName),
                        columnValue.getClass());
                m.invoke(object, columnValue);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
