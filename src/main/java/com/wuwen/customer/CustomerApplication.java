package com.wuwen.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/** @author wuwen */
@EnableScheduling
@Configuration
@ComponentScan(basePackages = {"com.wuwen"})
@MapperScan(basePackages = {"com.wuwen.customer.domain.*.dao.mapper"})
@SpringBootApplication
public class CustomerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerApplication.class, args);
  }
}
