
package com.example.ExpenseTracker;

import com.example.ExpenseTracker.ui.ExpenseConsole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExpenseTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackerApplication.class, args);
    }

    @Bean
    CommandLineRunner runExpenseConsole(ExpenseConsole console) {
        return args -> console.start();
    }
}