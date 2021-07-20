package com.ganesan.reports.adapter;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Streams a ResultSet as JSON.
 * 
 * @author Ganesan
 */
public class StreamingJsonResultSetExtractor implements ResultSetExtractor<Long> {

  private OutputStream os = null;

  /**
   * @param os the OutputStream to stream the JSON to
   */
  public StreamingJsonResultSetExtractor(final OutputStream os) {
    this.os = os;
  }

  @Override
  public Long extractData(final ResultSet rs) {

    final ObjectMapper objectMapper = new ObjectMapper();
    JsonGenerator jg = null;
    long rowcount = 0L;

    try {

      jg = objectMapper.getFactory().createGenerator(os, JsonEncoding.UTF8);
       rowcount =writeResultSetToJson(rs, jg);
      jg.flush();

    } catch (IOException | SQLException e) {
      throw new RuntimeException(e);
    } finally {
      JdbcUtil.close(jg);
      JdbcUtil.close(os);
    }

    return rowcount;

  }

 

  private long writeResultSetToJson(final ResultSet rs, final JsonGenerator jg) throws SQLException, IOException {
    final ResultSetMetaData rsmd = rs.getMetaData();
    final int columnCount = rsmd.getColumnCount();
    long rowcount = 0L;
    System.out.printf("COLumn Count%d%n", columnCount);

    jg.writeStartArray();

    while (rs.next()) {
      rowcount++;

      jg.writeStartObject();
      for (int i = 1; i <= columnCount; i++) {
        jg.writeObjectField(rsmd.getColumnName(i), rs.getObject(i));
        //System.out.printf("COL Name=%s, Value=%s%n", rsmd.getColumnName(i), ""+rs.getObject(i));
      }
      jg.writeEndObject();
    }

    
    jg.writeEndArray();

    System.out.printf("Total Rows from JDBC to JSON=%d%n", rowcount);

    return rowcount;

  }
}