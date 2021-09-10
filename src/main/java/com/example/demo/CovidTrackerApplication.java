package com.example.demo;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@SpringBootApplication
public class CovidTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CovidTrackerApplication.class, args);
    }

    @Configuration
    public class WebConfiguration implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedMethods("PUT", "POST", "DELETE", "GET", "PUT, POST, DELETE, GET")
                    .allowedOrigins("http://localhost:3000").exposedHeaders("Authorization");
        }
    }


}
