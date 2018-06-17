package core;

/**
 * 负责java数据类型和数据库类型的互相转换
 */
public interface TypeConvertor {
    /**
     * 数据库类型转换为java类型
     * @param columnType
     * @return
     */
    public String databaseTypeToJavaType(String columnType);

    /**
     * java类型转换为数据库类型
     * @param javaType
     * @return
     */
    public String javaTypeToDatabaseType(String javaType);
}
