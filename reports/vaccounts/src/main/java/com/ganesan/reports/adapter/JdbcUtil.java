package com.ganesan.reports.adapter;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import com.ganesan.config.Config;

public class JdbcUtil {

    public static Connection getJdbcDataSource(final String dbconfig) throws SQLException, FileNotFoundException {

        final Map<String, Object> dbconf = (Map<String, Object>) Config.getConfig(dbconfig);
        System.out.println("DBCONF" + dbconf.toString());

        final String url = (String) dbconf.get("jdbcurl");
        System.out.println("jdbcurl=" + url);

        final Properties pr = new Properties();
        pr.setProperty("user", (String) dbconf.get("user"));
        pr.setProperty("password", (String) dbconf.get("password"));
        pr.setProperty("ssl", "false");

        final Connection con = DriverManager.getConnection(url, pr);
        return con;
    }

    public static ResultSet getResultSet(final Connection conn, final String sql, final ArrayList<String> params)
            throws SQLException {

        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        int idx = 1;
        try {
            prepStmt = conn.prepareStatement(sql);

            for (final String bindValue : params) {
                // Get entry set from map

                prepStmt.setString(idx++, bindValue);
            }

            rs = prepStmt.executeQuery();
            return rs;
        } catch (final SQLException ex) {
            close(rs);
            close(prepStmt);
            throw ex;

        }
    }

    public static void close(final java.lang.AutoCloseable cl) {
        if (cl != null) {

            try {
                cl.close();
            } catch (final Exception e) {
            }

        }
    }

    public static void close(final Closeable cl) {
        if (cl != null) {

            try {
                cl.close();
            } catch (final Exception e) {
            }

        }
    }

}