package com.example.card_test_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CardTestAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardTestAppApplication.class, args);
        System.out.println("Hello");

        int i = 0;
        while(i < 4) {
            System.out.println(i);
            i++;
        }

        for(int j =0; j < 2; j++) {
            System.out.println("Hello");
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(i);
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(i);
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(i);
        }

	}

}
