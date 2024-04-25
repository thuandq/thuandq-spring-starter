//package com.ttc.spring.starter;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.TimeZone;
//
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@EnableFeignClients({"com.ttc.spring.starter"})
//@EnableSwagger2
//@EnableScheduling
//@EnableJpaRepositories
//public class Application {
//    public static void main(String[] args) {
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
//        SpringApplication.run(Application.class, args);
//    }
//}
