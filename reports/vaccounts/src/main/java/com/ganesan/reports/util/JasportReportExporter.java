package com.ganesan.reports.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;

public class JasportReportExporter {

    public static String getDestinationFileName(final String srcFileName) {
        final String destFileName = srcFileName.replaceFirst(".jrxml", ".jasper");
        System.out.println("destFileName=" + destFileName);
        return destFileName;
    }

    public static String compileReport(final String srcJrXMLFile) throws JRException {
        try {

            // final InputStream reportStream = FileInputSream(srcJrXMLFile);
            final String jasperReportOutFileName = getDestinationFileName(srcJrXMLFile);

            JasperCompileManager.compileReportToFile(srcJrXMLFile, jasperReportOutFileName);

            return jasperReportOutFileName;

        } catch (final JRException ex) {
            Logger.getLogger(JasportReportExporter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public static void generateReport(final JRDataSource jrd, final String srcJrXMLFile, final String reportOutFile,
            final Map<String, Object> parameters) throws IOException, JRException   {
        try {

            final String jasperReportFile = compileReport(srcJrXMLFile);

            // JasperFillManager.fillReportToFile(jasperReportFile, reportOutFile,
            // parameters,
            // getConnection());

            InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(jasperReportFile);
            FileOutputStream outStream = new FileOutputStream(reportOutFile);

            JasperRunManager.runReportToPdfStream(inStream, outStream, parameters, jrd);

            outStream.close();
            inStream.close();

        } catch (final JRException ex) {
            Logger.getLogger(JasportReportExporter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

}
