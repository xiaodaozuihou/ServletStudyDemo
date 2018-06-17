package bean;

/**
 * 封装java get set方法的源码
 */
public class JavaFieldGetSet {
    private String fieldInfo;
    private String setInfo;
    private String getInfo;

    public JavaFieldGetSet() {
    }

    public JavaFieldGetSet(String fieldInfo, String setInfo, String getInfo) {
        this.fieldInfo = fieldInfo;
        this.setInfo = setInfo;
        this.getInfo = getInfo;
    }

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }

    public String getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(String getInfo) {
        this.getInfo = getInfo;
    }
}

