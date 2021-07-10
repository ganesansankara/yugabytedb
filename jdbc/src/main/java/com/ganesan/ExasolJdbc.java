package com.ganesan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ganesan.utils.ConfigHelper;
import com.ganesan.utils.JdbcHelper;

public class ExasolJdbc {

    private String dbDriver;

    private String dbURL;

    private String dbUser;

    private String dbPassword;

    private int dbBatchSize;

    private Connection conn;

    private void init() throws SQLException {

        dbDriver = ConfigHelper.getConfig("database.driver", "XX");
        dbURL = ConfigHelper.getConfig("database.url", "XX");
        dbUser = ConfigHelper.getConfig("database.user", "XX");
        dbPassword = ConfigHelper.getConfig("database.password", "XX");
        dbBatchSize = Integer.parseInt(ConfigHelper.getConfig("database.batchsize", "1000"));

        JdbcHelper.setConnectionProperties(dbDriver, dbURL, dbUser, dbPassword);
        conn = JdbcHelper.getConnection();

        setSchema();

    }

    private void closeAll() {
        JdbcHelper.closeConnection(conn);
    }

    private void setSchema() throws SQLException {

        String ddlStatementsList[] = {

                // "CREATE SCHEMA IF NOT EXISTS GANESAN",
                "SELECT CURRENT_USER", "OPEN SCHEMA PUB3715", };
        Statement stmt = null;
        for (String st : ddlStatementsList) {

            executeDDLStatement(conn, st);
        }

    }

    private void showSystemCatalog() {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CAT");
            System.out.println("Schema SYS contains:");
            while (rs.next()) {
                String str1 = rs.getString("TABLE_NAME");
                String str2 = rs.getString("TABLE_TYPE");
                System.out.println(str1 + ", " + str2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcHelper.closeStatement(stmt);
        }
    }

    private void createSchema(boolean dropschema) throws SQLException {

        if (dropschema) {
            executeDDLStatement(conn, "DROP TABLE IF EXISTS accounts");

            executeDDLStatement(conn,
                    "CREATE TABLE accounts (" + " acct_no VARCHAR(50) NOT NULL," + " name VARCHAR(100) NOT NULL,"
                            + " balance DECIMAL(15,2) NOT NULL," + " delete_flag VARCHAR(1) NOT NULL,"
                            + " version_no DECIMAL(10) NOT NULL," + " created_at TIMESTAMP NOT NULL,"
                            + " updated_at TIMESTAMP NOT NULL" + " )");
        }

    }

    private void countTable() throws SQLException {
        String selectStatementsList[] = {

                // "CREATE SCHEMA IF NOT EXISTS GANESAN",

                // "OPEN SCHEMA PUB3715",
                "SELECT COUNT(1) FROM accounts", };

        for (String st : selectStatementsList) {

            executeSelectStatement(conn, st);
        }
        conn.commit();

    }

    private void populateTable(String prefix, int maxrecs) {

        PreparedStatement pstmt = null;

        try {

            countTable();

            pstmt = conn.prepareStatement(
                    "INSERT INTO accounts ( acct_no, name, balance,version_no,delete_flag,created_at,updated_at) "
                            + " VALUES(?,?,?,?,?, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)");

            String acct, name;
            Double balance;
            boolean bCommit = false;

            for (int idx = 1; idx <= maxrecs; idx++) {
                acct = String.format("%s-acct-%03d", prefix, idx);
                name = String.format("AcctName-%s", acct);
                balance = 10000.99 + (idx * 10);

                pstmt.setString(1, acct);
                pstmt.setString(2, name);
                pstmt.setDouble(3, balance);
                pstmt.setInt(4, 1);
                pstmt.setString(5, "N");

                pstmt.addBatch();

                // int ret = pstmt.executeUpdate();

                // System.out.printf("INSERT RETURNED=%d%n", ret);

                bCommit = true;
                if (idx % dbBatchSize == 0) {

                    int ret[] = pstmt.executeBatch();

                    conn.commit();
                    bCommit = false;

                    System.out.printf("inserting=%s,%s,%s%n", acct, name, balance);
                }

            }

            if (bCommit) {
                pstmt.executeBatch();
                conn.commit();
                System.out.printf("Finished=%s,%d%n", prefix, maxrecs);
                countTable();
            }
            countTable();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcHelper.closeStatement(pstmt);
        }

    }

    private void executeDDLStatement(Connection conn, String st) {
        System.out.printf("Executing %s\n", st);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(st);
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcHelper.closeStatement(stmt);
        }

    }

    private void executeSelectStatement(Connection conn, String st) {
        System.out.printf("Executing %s\n", st);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(st);

            String str1;
            while (rs.next()) {
                for (int idx = 1; idx <= rs.getMetaData().getColumnCount(); idx++) {
                    str1 = rs.getString(idx);
                    System.out.printf("%s=%s,", rs.getMetaData().getColumnLabel(idx), str1);
                }
                System.out.println("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcHelper.closeStatement(stmt);
        }

    }

    public static void main(String... args) {

        String prefix = args[0];
        ExasolJdbc myJDBC = new ExasolJdbc();

        try {

            myJDBC.init();

            if ("createschema".equals(prefix)) {
                myJDBC.createSchema(true);
                myJDBC.showSystemCatalog();
            } else {
                int maxrecs = Integer.valueOf(args[1]);
                myJDBC.populateTable(prefix, maxrecs);
            }
        } catch (

        SQLException e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeAll();
            ;
        }
    }
}