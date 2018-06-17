package core;

import bean.ColumnInfo;
import bean.TableInfo;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import utils.JDBCUtils;
import utils.ReflectUtils;
import utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLQuery extends Query {
    @Override
    public Object queryPagenate(int pageNum, int size) {
        return null;
    }
}
