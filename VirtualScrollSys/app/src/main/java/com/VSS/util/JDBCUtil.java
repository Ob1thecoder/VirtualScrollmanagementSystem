package com.VSS.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

    private static final String DB_URL = "jdbc:sqlite:./db.sqlite3";
    private static final String DRIVER_CLASS = "org.sqlite.JDBC";

    public static Connection getConnection() throws SQLException {
        try {
            
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found", e);
        }
       
        return DriverManager.getConnection(DB_URL);
    }

   
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
