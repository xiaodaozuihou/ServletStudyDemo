package core;

import bean.Configuration;
import pool.DBConnPool;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息，维持连接对象的管理（增加连接池功能）
 */
public class DBManager {
    private static Configuration conf;

    private static DBConnPool pool;
    static {
        Properties pros = new Properties();
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        conf = new Configuration();
        conf.setDriver(pros.getProperty("driver"));
        conf.setPoPackage(pros.getProperty("poPackage"));
        conf.setSrcPath(pros.getProperty("srcPath"));
        conf.setUsingDB(pros.getProperty("usingDB"));
        conf.setUrl(pros.getProperty("url"));
        conf.setUsername(pros.getProperty("username"));
        conf.setPassword(pros.getProperty("password"));
        conf.setPoolMaxSize(Integer.parseInt(pros.getProperty("poolMaxSize")));
        conf.setPoolMinSize(Integer.parseInt(pros.getProperty("poolMinSize")));
    }

    public static Connection createCon(){
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(), conf.getUsername(), conf.getPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Connection getCon(){
        if (pool == null){
            pool = new DBConnPool();
        }
       return pool.getConnection();
    }

    public static void close(ResultSet rs, Statement ps, Connection con){
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        pool.close(con);
    }

    public static void close(Statement ps, Connection con){
        if (ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        pool.close(con);
    }

    public static Configuration getConf() {
        return conf;
    }

    public static void setConf(Configuration conf) {
        DBManager.conf = conf;
    }
}
