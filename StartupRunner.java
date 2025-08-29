package com.bajaj.hiring;

import com.bajaj.hiring.service.HiringService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {
  private final HiringService service;
  public StartupRunner(HiringService service) {
    this.service = service;
  }
  @Override public void run(ApplicationArguments args) {
    service.runFlow();
  }
}
