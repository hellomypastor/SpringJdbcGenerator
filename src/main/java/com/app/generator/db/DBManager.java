package com.app.generator.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.app.generator.Column;
import com.app.generator.StringUtils;

public class DBManager
{
    private static DBManager instance = null;
    //private static final String ID = "id";
    private static final String queryStart = "select * from ";
    private static final String queryEnd = " where 1 = 2 ";

    private String userID;
    private String password;
    private String url;
    private String driver;
    private static String pkQuery = "SELECT t.TABLE_NAME,t.CONSTRAINT_TYPE,c.COLUMN_NAME,c.ORDINAL_POSITION FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS t, INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS c WHERE t.TABLE_NAME = c.TABLE_NAME  AND t.TABLE_NAME = '{table_name}' AND t.CONSTRAINT_TYPE = 'PRIMARY KEY'";

    private DBManager(Properties properties)
    {
        userID = properties.getProperty("db.username");
        password = properties.getProperty("db.password");
        url = properties.getProperty("db.url");
        driver = properties.getProperty("db.driver");
    }

    public static DBManager getInstance(Properties properties)
    {
        if (instance == null)
        {
            instance = new DBManager(properties);
        }
        return instance;
    }

    private void getPK(final String model, final List<Column> cols)
    {
        Connection conn = null;
        PreparedStatement pstmt;
        try
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, userID, password);
            pkQuery = pkQuery.replaceAll("\\{table_name\\}", model);
            pstmt = (PreparedStatement) conn.prepareStatement(pkQuery);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                String columeName = rs.getString("COLUMN_NAME");
                for (Column column : cols)
                {
                    if (columeName != null && columeName.equals(column.getDbname()))
                    {
                        column.setIspk(true);
                    }
                }
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {

        }
        finally
        {
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public List<Column> getTableInfo(final String model)
    {
        List<Column> columns = new ArrayList<Column>();
        Connection conn = null;
        PreparedStatement pstmt;
        String sql = queryStart + model + queryEnd;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, userID, password);
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= col; i++)
            {
                Column column = new Column();
                column.setDbname(rs.getMetaData().getColumnName(i));
                column.setName(StringUtils.replaceUnderscore(rs.getMetaData().getColumnName(i).toLowerCase()));
                column.setLength(rs.getMetaData().getColumnDisplaySize(i));
                column.setType(DataType.valueOf(
                        (rs.getMetaData().getColumnClassName(i).substring(rs.getMetaData().getColumnClassName(i)
                                .lastIndexOf(".") + 1)).toUpperCase()).toString());
                column.setGetter("get" + StringUtils.firstCaps(StringUtils.replaceUnderscore(column.getDbname()))
                        + "()");
                column.setSetter("set" + StringUtils.firstCaps(StringUtils.replaceUnderscore(column.getDbname())));
                columns.add(column);
            }
            getPK(model, columns);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return columns;
    }
}