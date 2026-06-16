package com.stocksignal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer; // 추가
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableRetry
public class StockSignalApplication extends SpringBootServletInitializer { // 상속 추가

    // 외부 서블릿 컨테이너(Tomcat 등)가 애플리케이션을 로드할 때 실행되는 메서드
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(StockSignalApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(StockSignalApplication.class, args);
    }
}