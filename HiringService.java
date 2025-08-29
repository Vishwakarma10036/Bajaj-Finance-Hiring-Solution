package com.bajaj.hiring.service;

import com.bajaj.hiring.dto.GenerateWebhookRequest;
import com.bajaj.hiring.dto.GenerateWebhookResponse;
import com.bajaj.hiring.dto.SubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HiringService {
  private static final Logger log = LoggerFactory.getLogger(HiringService.class);
  private final WebClient client;
  private final String name;
  private final String regNo;
  private final String email;

  public HiringService(WebClient hiringClient,
                       @Value("${app.name}") String name,
                       @Value("${app.regNo}") String regNo,
                       @Value("${app.email}") String email) {
    this.client = hiringClient;
    this.name = name;
    this.regNo = regNo;
    this.email = email;
  }

  public void runFlow() {
    try {
      log.info("▶️ Generating webhook for {}", name);
      var req = new GenerateWebhookRequest(name, regNo, email);
      GenerateWebhookResponse gen = client.post()
          .uri("/generateWebhook/JAVA")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(req)
          .retrieve()
          .bodyToMono(GenerateWebhookResponse.class)
          .block();
      if (gen == null) throw new IllegalStateException("Invalid response");

      boolean odd = isLastTwoOdd(regNo);
      String finalSql = SqlProvider.loadSql(odd);
      log.info("Loaded SQL for {}: {}", odd ? "Q1" : "Q2", finalSql.substring(0, Math.min(80, finalSql.length())));

      var submitReq = new SubmitRequest(finalSql);
      String resp = client.post()
          .uri("/testWebhook/JAVA")
          .header(HttpHeaders.AUTHORIZATION, gen.accessToken())
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(submitReq)
          .retrieve()
          .bodyToMono(String.class)
          .block();
      log.info("Submission response: {}", resp);
    } catch (Exception e) {
      log.error("Flow failed: {}", e.getMessage(), e);
    }
  }

  private static String lastTwo(String regNo) {
    String digits = regNo.replaceAll("\D", "");
    return digits.substring(Math.max(0, digits.length()-2));
  }
  private static boolean isLastTwoOdd(String regNo) {
    int n = Integer.parseInt(lastTwo(regNo));
    return (n % 2) == 1;
  }
}
