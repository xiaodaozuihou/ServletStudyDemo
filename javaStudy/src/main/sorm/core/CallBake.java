package core;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface CallBake  {
    public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs);
}
