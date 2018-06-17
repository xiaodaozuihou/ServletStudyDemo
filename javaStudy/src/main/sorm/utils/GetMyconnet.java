package utils;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class GetMyconnet {
    static Properties pros = null;

    static {
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getMyconnet() {
        try {

            Class.forName(pros.get("jdbc.driver").toString());
            String url = pros.get("jdbc.url").toString();
            String username = pros.get("username").toString();
            String password = pros.get("password").toString();
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
