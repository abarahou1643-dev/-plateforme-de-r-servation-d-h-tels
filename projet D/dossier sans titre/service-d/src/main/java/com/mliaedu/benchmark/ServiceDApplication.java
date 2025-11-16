package com.mliaedu.benchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    basePackages = "com.mliaedu.benchmark",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = com.mliaedu.benchmark.util.DataGenerator.class
    )
)
public class ServiceDApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDApplication.class, args);
    }
}

