package core;

import bean.ColumnInfo;
import bean.Configuration;
import bean.TableInfo;
import utils.JavaFileUtils;
import utils.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构
 */
public class TableContext {
    //表名为key，表信息对象为value
    public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();
    //将po的class对象和表信息对象关联起来，便于使用
    public static Map<Class, TableInfo> poClassTableMap = new HashMap<Class, TableInfo>();

    public TableContext() {
    }
    static {
        Connection con = DBManager.getCon();
        try {
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet tablesRet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});
            while (tablesRet.next()){
                String tableName = (String) tablesRet.getObject("TABLE_NAME");
                TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String, ColumnInfo>());
                tables.put(tableName,ti);
                //查询表中所有的字段
                ResultSet set = dbmd.getColumns(null, "%", tableName, "%");
                while (set.next()){
                    ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),set.getString("TYPE_NAME"),0);
                    ti.getColumns().put(set.getString("COLUMN_NAME"),ci);
                }
                //查询表中的主键
                ResultSet set1 = dbmd.getPrimaryKeys(null, "%", tableName);
                while (set1.next()){
                    ColumnInfo ci1 = ti.getColumns().get(set1.getObject("COLUMN_NAME"));
                    ci1.setKeyType(1);
                    ti.getPriKey().add(ci1);
                }

                //取唯一主键使用，如果是联合主键，则为空
                if (ti.getPriKey().size() > 0){
                    ti.setOnlyPriKey(ti.getPriKey().get(0));
                }

            }
            //更新类结构
            updateJavaPoFile();
            //加载po下面所有的类，便于重用，提高效率
            loadPOTables();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, TableInfo> getTables() {
        return tables;
    }

    public static void setTables(Map<String, TableInfo> tables) {
        TableContext.tables = tables;
    }

    public static Map<Class, TableInfo> getPoClassTableMap() {
        return poClassTableMap;
    }

    public static void setPoClassTableMap(Map<Class, TableInfo> poClassTableMap) {
        TableContext.poClassTableMap = poClassTableMap;
    }

    /**
     * 根据表结构，更新配置的po包下面的java类
     */
    public static void updateJavaPoFile(){
        Map<String, TableInfo> map = TableContext.tables;
        for (TableInfo t:map.values()) {
            JavaFileUtils.createJavaPOFile(t,new MysqlTypeConvertor());
        }
    }

    /**
     * 加载po类
     */
    public static void loadPOTables(){
        for (TableInfo tableInfo:tables.values()) {
            try {
                Class c = Class.forName(DBManager.getConf().getPoPackage()
                        + "." + StringUtils.firstCharToUpperCase(tableInfo.getTname()));
                poClassTableMap.put(c,tableInfo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
