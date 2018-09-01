package com.snapcheck.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnapCheckApplication {
	private static String ethereumUrl="http://localhost:8545";

	public static void main(String[] args) {
		if (args.length>0) ethereumUrl = args[0];
		SpringApplication.run(SnapCheckApplication.class, args);
	}
	
	public static String getEthereumUrl() {
		return ethereumUrl;
	}
}
