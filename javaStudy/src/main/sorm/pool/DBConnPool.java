package pool;

import core.DBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnPool {
    /**
     * 连接池对象
     */
    private List<Connection> pool;
    /**
     * 最大连接数
     */
    private static final int POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();
    /**
     * 最小连接数
     */
    private static final int POOL_MIN_SIZE = DBManager.getConf().getPoolMinSize();

    /**
     * 初始化连接池，使池中的连接数大于最小连接数
     */
    public void initPool(){
        if (pool == null){
            pool = new ArrayList<Connection>();
        }
        while (pool.size() < DBConnPool.POOL_MIN_SIZE){
            pool.add(DBManager.createCon());
            System.out.println("初始化连接池，池中连接数为：" + pool.size());
        }
    }

    /**
     * 从池中获取一个连接
     * @return
     */
    public synchronized Connection getConnection(){
        int lastIndex = pool.size() - 1;
        Connection conn = pool.get(lastIndex);
        pool.remove(lastIndex);
        return conn;
    }

    /**
     * 将连接放入连接池
     * @param conn
     */
    public synchronized void close(Connection conn){
        if (pool.size() > DBConnPool.POOL_MAX_SIZE){
            try {
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            pool.add(conn);
        }
    }
    public DBConnPool() {
        initPool();
    }
}
