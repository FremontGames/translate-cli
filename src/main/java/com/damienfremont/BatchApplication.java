package com.damienfremont;
import org.springframework.boot.SpringApplication;

public class BatchApplication {

	public static void main(String[] args) {
		System.exit( //
				SpringApplication.exit( //
						SpringApplication.run( //
								BatchConfig.class, //
								args)));
	}
}
