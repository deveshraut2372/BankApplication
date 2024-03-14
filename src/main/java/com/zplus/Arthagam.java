package com.zplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Arthagam {
	public static void main(String[] args) {
		SpringApplication.run(Arthagam.class, args);
		System.out.println("Arthagam Urban Nidhi");
	}

}
