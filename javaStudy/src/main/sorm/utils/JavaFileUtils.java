package utils;

import bean.ColumnInfo;
import bean.JavaFieldGetSet;
import bean.TableInfo;
import core.DBManager;
import core.TypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaFileUtils {
    /**
     * 根据字段信息生成java属性信息。如：varchar username ----> private String username；并生成相应的get set方法
     * @param column
     * @param convertor
     * @return
     */
    public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column, TypeConvertor convertor){
        JavaFieldGetSet jfgs = new JavaFieldGetSet();
        String javaFieldType = convertor.databaseTypeToJavaType(column.getDataType());
        jfgs.setFieldInfo("\tprivate " + javaFieldType + " " + column.getName() + ";\n");

        //public String getUsername(){return username;} 生成get方法
        StringBuilder getSrc = new StringBuilder();
        getSrc.append("\tpublic " + javaFieldType + " get" + StringUtils.firstCharToUpperCase(column.getName() + "(){\n"));
        getSrc.append("\t\treturn" + column.getName() + ";\n");
        getSrc.append("\t}\n");
        jfgs.setGetInfo(getSrc.toString());

        //public void setUsername(String username){this.username=username;}生成set方法
        StringBuilder setSrc = new StringBuilder();
        setSrc.append("\tpublic void set" + StringUtils.firstCharToUpperCase(column.getName()) +"(" +javaFieldType + " " + column.getName()
        +"){\n");
        setSrc.append("\t\tthis." + column.getName() + " = " + column.getName()+ ";\n");
        setSrc.append("\t\t}\n");
        jfgs.setSetInfo(setSrc.toString());
        return jfgs;
    }

    /**
     * 根据表信息生成java类源代码
     * @param tableInfo
     * @param typeConvertor
     * @return
     */
    public static String createJavaSrc(TableInfo tableInfo, TypeConvertor typeConvertor){
        Map<String, ColumnInfo> columns = tableInfo.getColumns();
        List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();
        for (ColumnInfo c: columns.values()) {
            javaFields.add(createFieldGetSetSRC(c, typeConvertor));
        }
        StringBuilder src = new StringBuilder();
        //生成package语句
        src.append("package " + DBManager.getConf().getPoPackage() + ";\n\n");
        //生成import语句
        src.append("import java.sql.*;\n");
        src.append("import java.util.*;\n\n");
        //生成类声明语句
        src.append("public class " + StringUtils.firstCharToUpperCase(tableInfo.getTname()) + " {\n\n");
        //生成属性列表
        for (JavaFieldGetSet f : javaFields) {
            src.append(f.getFieldInfo());
        }
        //生成get方法列表
        for (JavaFieldGetSet f : javaFields) {
            src.append(f.getGetInfo());
        }
        //生成set方法列表
        for(JavaFieldGetSet f : javaFields){
            src.append(f.getSetInfo());
        }
        //生成类结束
        src.append("}\n");
        return src.toString();
    }

    public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor convertor){
        String src = createJavaSrc(tableInfo, convertor);
        String srcPath = DBManager.getConf().getSrcPath() + "\\";
        String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.","\\\\");
        BufferedWriter bw = null;
        File file = new File(src + srcPath);
        if (!file.exists()){
            file.mkdirs();
        }
        try {
            bw = new BufferedWriter(new FileWriter(file.getAbsolutePath() +"/" + StringUtils.firstCharToUpperCase(tableInfo.getTname()) + ".java" ));
            bw.write(src);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
