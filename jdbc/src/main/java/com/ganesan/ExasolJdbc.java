package com.ganesan;

import com.ganesan.utils.JdbcHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExasolJdbc {
    private static void createSchema(final Connection conn) throws SQLException {

        String statementsList[] = {

                "CREATE SCHEMA IF NOT EXISTS GANESAN",
                "OPEN SCHEMA GANESAN",
                "DROP TABLE IF EXISTS accounts",
                "CREATE TABLE accounts (" +
                        " acct_no VARCHAR(50) NOT NULL," +
                        " name VARCHAR(100) NOT NULL," +
                        " balance DECIMAL(15,2) NOT NULL," +
                        " delete_flag VARCHAR(1) NOT NULL," +
                        " version_no DECIMAL(10) NOT NULL," +
                        " created_at TIMESTAMP NOT NULL," +
                        " updated_at TIMESTAMP NOT NULL" +
                        " )"
        };
        Statement stmt = null;
        for (String st : statementsList) {

            executeDDLStatement(conn,st);
        }
    }

    private static void  executeDDLStatement(Connection conn, String st) {
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

    public static void main(String[] args) {
        JdbcHelper.setConnectionProperties("com.exasol.jdbc.EXADriver",
                "jdbc:exa:DEMODB.EXASOL.COM:8563",
                "PUB3715",
                "NbMCCidzA");

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = JdbcHelper.getConnection();
            createSchema(conn);
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
            JdbcHelper.closeConnection(conn);
        }
    }
}