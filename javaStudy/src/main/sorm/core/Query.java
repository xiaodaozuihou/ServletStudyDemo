package core;

import bean.ColumnInfo;
import bean.TableInfo;
import utils.JDBCUtils;
import utils.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责查询(提供服务的核心类)
 */
public abstract class Query implements Cloneable {

    /**
     * 采用模板方法将jdbc操作封装为模板，便于重用
     * @param sql
     * @param params
     * @param clazz
     * @param bake
     * @return
     */
    public Object executeQueryTemplate(String sql, Object[] params, Class clazz, CallBake bake){

        Connection con = DBManager.getCon();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            JDBCUtils.handleParams(ps,params);
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            return bake.doExecute(con, ps, rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBManager.close(ps,con);
        }
    }

    /**
     * 直接执行一个DML语句
     * @param sql
     * @param params
     * @return 返回执行语句影响的行数
     */
    public int executeDML(String sql, Object[] params){
        Connection con = DBManager.getCon();
        int count = 0;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            JDBCUtils.handleParams(ps,params);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.close(ps,con);
        }
        return count;
    }

    /**
     * 将一个对象存储到数据库中
     * @param obj
     */
    public void insert(Object obj){
        Class<?> c = obj.getClass();
        List<Object> params = new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        StringBuilder sql = new StringBuilder("insert into " + tableInfo.getTname() + " (");
        int countNotNullField = 0;
        Field[] fields = c.getDeclaredFields();
        for (Field field: fields) {
            String fieldName = field.getName();
            Object fieldValue = ReflectUtils.invokeGet(fieldName, c);
            if (fieldValue != null){
                countNotNullField++;
                sql.append(fieldValue + ",");
                params.add(fieldValue);
            }
            sql.setCharAt(sql.length() - 1,')');
            sql.append(" values (");
            for (int i = 0; i < countNotNullField; i++) {
                sql.append("?, ");
            }
            sql.setCharAt(sql.length() - 1,')');
            executeDML(sql.toString(),params.toArray());
        }
    }

    /**
     * 删除clazz表示类对应的表中的记录（id为主键）
     * @param clazz
     * @param id
     * @return
     */
    public void delete(Class clazz, Object id){
        TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        String sql = "delete from " + tableInfo.getTname() + "where" + onlyPriKey.getName() + "= ?";
        executeDML(sql, new Object[]{id});
    }

    /**
     * 删除对象在数据库中对应的记录
     * @param object
     * @return
     */
    public void delete(Object object){
        Class c = object.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        //通过反射机制，调用属性对应的get方法或set方法
        Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(), object);
        delete(c,priKeyValue);
    };

    /**
     *更新对象对应的记录，并且只更新指定的字段值
     * @param object 所需要更新的对象
     * @param fieldNames 更新的属性列表
     * @return 影响的行数
     */
    public int update(Object object, String[] fieldNames){
        Class<?> c = object.getClass();
        List<Object> params = new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        StringBuilder sql = new StringBuilder("update " + tableInfo.getTname() + " set");
        for (String fName:fieldNames) {
            Object fValue = ReflectUtils.invokeGet(fName, object);
            params.add(fValue);
            sql.append(fName + "=?,");
        }
        sql.setCharAt(sql.length() - 1, ' ');
        sql.append(" where ");
        sql.append(onlyPriKey.getName() + "=? ");
        params.add(ReflectUtils.invokeGet(onlyPriKey.getName(),object));
        return executeDML(sql.toString(), params.toArray());
    }

    /**
     *查询返回多行记录，并将每行记录封装到clazz对象中
     * @param sql
     * @param clazz 封装数据的javabean
     * @param params 查询参数
     * @return 返回查询到的返回结果
     */
    public List queryRows(final String sql, final Class clazz, final Object[] params) {

       return (List<?>) executeQueryTemplate(sql, params, clazz, new CallBake() {
            public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
                List list = null;
                try {
                    ResultSetMetaData metaData = rs.getMetaData();
                    while (rs.next()) {
                        if (list == null){
                            list = new ArrayList();
                        }
                        //相当于调用javaBean的无参构造器
                        Object rowOj = clazz.newInstance();
                        for (int i = 0; i < metaData.getColumnCount(); i++) {
                            String columnName = metaData.getColumnLabel(i + 1);
                            Object columnValue = rs.getObject(i + 1);
                            //调用rowObj对象的setUsername(String uname)方法，将columnValue的值设置进去
                            ReflectUtils.invokSet(rowOj, columnName, columnValue);
                        }
                        list.add(rowOj);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return list;
            }
        });
    }
    /**
     *查询返回一行记录，并将该记录封装到clazz对象中
     * @param sql
     * @param clazz 封装数据的javabean
     * @param params 查询参数
     * @return 返回查询到的返回结果
     */
    public Object queryUniqueRow(String sql, Class clazz, Object[] params){
        List list = queryRows(sql, clazz, params);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }
    /**
     *查询返回一个值
     * @param sql
     * @param params 查询参数
     * @return 返回查询到的返回结果
     */
    public Object queryValue(String sql, Object[] params){
      /*原始
       Connection con = DBManager.getCon();
        Object value = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            JDBCUtils.handleParams(ps,params);
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()){
                value = rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
*/

       return executeQueryTemplate(sql, params, null, new CallBake() {
            public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
                Object value = null;
                try {
                    while (rs.next()){
                        value = rs.getObject(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return value;
            }
        });

    }
    /**
     *查询返回一个数字
     * @param sql
     * @param params 查询参数
     * @return 返回查询到的返回结果
     */
    public Number queryNumber(String sql, Object[] params){
        return (Number) queryValue(sql,params);
    }

    public abstract Object queryPagenate(int pageNum, int size);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
