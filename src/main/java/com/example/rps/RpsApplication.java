package com.example.rps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * --- Rock, Paper and Scissor. ---
 * Logic:
 * Rock beats Scissors
 * Scissors beats Paper
 * Paper beats Rock
 */

@SpringBootApplication
public class RpsApplication {

	public static final String SESSION_ID = "USER_ID";

	public static void main(String[] args) {
		SpringApplication.run(RpsApplication.class, args);
	}

}
