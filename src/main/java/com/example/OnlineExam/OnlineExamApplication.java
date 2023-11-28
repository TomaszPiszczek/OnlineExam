package com.example.OnlineExam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class OnlineExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineExamApplication.class, args);
	}

}
