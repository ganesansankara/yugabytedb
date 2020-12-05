package com.ganesan.reports;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ganesan.reports.adapter.JdbcUtil;
import com.ganesan.reports.adapter.StreamingJsonResultSetExtractor;
import com.ganesan.reports.util.JasportReportExporter;

import org.junit.Test;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JsonDataSource;

/**
 * Unit test for simple App.
 */
public class JasperReportExporterTest {
    /**
     * Rigorous Test :-)
     * 
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws JRException
     */
    @Test
    public void shouldAnswerWithTrue() throws SQLException, FileNotFoundException, JRException {
        main(null);
        assertTrue(true);
    }

    public static void main(final String[] args) throws SQLException, FileNotFoundException, JRException {

        // SimpleReportFiller simpleReportFiller = new SimpleReportFiller();

        ArrayList<String> sqlParams = new ArrayList<String>();
        sqlParams.add("init1acct-00");

        ExtractJDBCToJSon("ganesan-db", "SELECT * FROM ACCOUNTS WHERE ACCT_NO=?", sqlParams, "vaccounts-postgres.json");

        ExtractJDBCToJSon("ganesan-dremio", "SELECT * FROM s2b.vaaccounts.vds_reports WHERE ACCT_NO=?", sqlParams, "vaccounts-dremio.json");

        

        /*
         * SimpleReportExporter simpleExporter = new SimpleReportExporter();
         * simpleExporter.setJasperPrint(simpleReportFiller.getJasperPrint());
         * 
         * simpleExporter.exportToPdf("vaccounts.pdf", "VACCOUNTS");
         * simpleExporter.exportToXlsx("vaccounts.xlsx", "VACCOUNTS DATA");
         * simpleExporter.exportToCsv("vaccounts.csv");
         * simpleExporter.exportToHtml("vaccounts.html");
         */

    }

    private static void ExtractJDBCToJSon(final String dbconfig, final String sql, final ArrayList<String> SqlParams, final String outFileName)
            throws SQLException, FileNotFoundException, JRException {
      

        final Connection conn = JdbcUtil.getJdbcDataSource(dbconfig);

        final ResultSet rs = JdbcUtil.getResultSet(conn, sql, SqlParams);
        final String outFullFileName = "/tmp/" + outFileName;

        final FileOutputStream fos = new FileOutputStream(outFullFileName);

        final StreamingJsonResultSetExtractor jsonStream = new StreamingJsonResultSetExtractor(fos);
        jsonStream.extractData(rs);
        JdbcUtil.close(rs);
        JdbcUtil.close(conn);

        final HashMap<String, Object> ReportParams = new HashMap<>();
        ReportParams.put("title", "Employee Report Example");
        ReportParams.put("minSalary", 15000.0);
		final JRDataSource jrd = new JsonDataSource(new FileInputStream(outFullFileName));
        JasportReportExporter.generateReport(jrd,
         "/home/ganesh/Ganesan/MyOwn/dremiopostgres/reports/vaccounts/src/reports/vaaccounts.jrxml",
         "/tmp/vaaccounts.pdf", ReportParams);
    }
}
