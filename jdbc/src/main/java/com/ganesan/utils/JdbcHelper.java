
package com.ganesan.utils;

import java.sql.*;

/**
 * @author GANESAN
 */
public class JdbcHelper {

   private static String driverName;
   private static String url;
   private static String userName;
   private static String password;

   public static void setConnectionProperties ( String DriverName, String URL, String UserName, String Password) {
      driverName = DriverName;
      url = URL;
      userName = UserName;
      password = Password;

      System.out.printf ("DB Driver=%s, URL=%s, User=%s, Password=%s%n", driverName, url, userName,  password);
      loadDriver();
      
    }

    private static void loadDriver () {
        try {
            Class.forName(driverName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("ERROR OCCURRED = %s", e.toString());
        }
    }

    public static Connection getConnection() throws SQLException {
       Connection connection = DriverManager.getConnection(url, userName, password);
       connection.setAutoCommit(false);
        return connection;
    }

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                System.out.printf("ERROR OCCURRED = %s", e.toString());
            }
        }
    }

    public static void closeStatement(Statement stmt)  {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("ERROR OCCURRED = %s", e.toString());
            }
        }
    }

    public static void closeResultSet(ResultSet rs)  {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("ERROR OCCURRED = %s", e.toString());
            }
        }
    }

}