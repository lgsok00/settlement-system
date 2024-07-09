package com.sparta.settlementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SettlementSystemApplication {

  public static void main(String[] args) {
    SpringApplication.run(SettlementSystemApplication.class, args);
  }

}
