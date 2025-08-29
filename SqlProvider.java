package com.bajaj.hiring.service;

import org.springframework.core.io.ClassPathResource;
import java.nio.charset.StandardCharsets;

public class SqlProvider {
  public static String loadSql(boolean odd) throws Exception {
    var path = odd ? "sql/q1.sql" : "sql/q2.sql";
    var res = new ClassPathResource(path);
    try (var in = res.getInputStream()) {
      return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
    }
  }
}
