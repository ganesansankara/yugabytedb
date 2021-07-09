package com.ganesan;

import com.ganesan.utils.JdbcHelper;

import java.sql.*;

public class ExasolJdbc {
    private static void createSchema(final Connection conn, boolean dropschema) throws SQLException {

        String ddlStatementsList[] = {

                // "CREATE SCHEMA IF NOT EXISTS GANESAN",
                "SELECT CURRENT_USER",
                "OPEN SCHEMA PUB3715",
        };
        Statement stmt = null;
        for (String st : ddlStatementsList) {

            executeDDLStatement(conn, st);
        }
        if (dropschema) {
            executeDDLStatement(conn, "DROP TABLE IF EXISTS accounts");

            executeDDLStatement(conn, "CREATE TABLE accounts (" +
                    " acct_no VARCHAR(50) NOT NULL," +
                    " name VARCHAR(100) NOT NULL," +
                    " balance DECIMAL(15,2) NOT NULL," +
                    " delete_flag VARCHAR(1) NOT NULL," +
                    " version_no DECIMAL(10) NOT NULL," +
                    " created_at TIMESTAMP NOT NULL," +
                    " updated_at TIMESTAMP NOT NULL" +
                    " )");
        }

    }

    private static void populateTable(Connection conn, String prefix, int maxrecs) {
        PreparedStatement pstmt = null;

        String selectStatementsList[] = {

                // "CREATE SCHEMA IF NOT EXISTS GANESAN",

                // "OPEN SCHEMA PUB3715",
                "SELECT COUNT(1) FROM accounts",
        };

        for (String st : selectStatementsList) {

            executeSelectStatement(conn, st);
        }

        try {
            pstmt = conn.prepareStatement("INSERT INTO accounts ( acct_no, name, balance,version_no,delete_flag,created_at,updated_at) " +
                    " VALUES(?,?,?,?,?, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)");
            String acct, name;
            Double balance;
            for (int idx = 1; idx <= maxrecs; idx++) {
                acct = String.format("%s-acct-%03d", prefix, idx);
                name = String.format("AcctName-%s", acct);
                balance = 10000.99 + (idx * 10);

                pstmt.setString(1, acct);
                pstmt.setString(2, name);
                pstmt.setDouble(3, balance);
                pstmt.setInt(4, 1);
                pstmt.setString(5, "N");

                int ret = pstmt.executeUpdate();

                //System.out.printf("INSERT RETURNED=%d%n", ret);

                if ( idx % 100 == 0 ) {
                    System.out.printf("inserting=%s,%s,%s%n", acct, name, balance);
                    conn.commit();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcHelper.closeStatement(pstmt);
        }

        for (String st : selectStatementsList) {

            executeSelectStatement(conn, st);
        }

    }

    private static void executeDDLStatement(Connection conn, String st) {
        System.out.printf("Executing %s\n", st);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(st);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcHelper.closeStatement(stmt);
        }

    }

    private static void executeSelectStatement(Connection conn, String st) {
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

    private static void showSystemCatalog(Connection conn) {
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

    public static void main(String[] args) {
        JdbcHelper.setConnectionProperties("com.exasol.jdbc.EXADriver",
                "jdbc:exa:DEMODB.EXASOL.COM:8563",
                "PUB3715",
                "NbMCCidzA");

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = JdbcHelper.getConnection();
            createSchema(conn,false);
            showSystemCatalog(conn);

            String prefix = "gan1";
            int maxrecs = 1000;
            populateTable(conn,prefix,maxrecs);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcHelper.closeConnection(conn);
        }
    }
}