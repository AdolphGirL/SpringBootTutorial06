package com.reyes.tutorial;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan(value = "com.reyes.tutorial.mapper")
@EnableCaching
@SpringBootApplication
public class SpringBootTutorial06Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTutorial06Application.class, args);
	}

}
