
package com.example.ExpenseTracker.ui;

import com.example.ExpenseTracker.Category;
import com.example.ExpenseTracker.Expense;
import com.example.ExpenseTracker.ExpenseJdbcRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class ExpenseConsole {

    private final Scanner scanner = new Scanner(System.in);
    private final ExpenseJdbcRepository repository;

    public ExpenseConsole(ExpenseJdbcRepository repository) {
        this.repository = repository;
    }

    // Main menu
    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("""
                    
                    ===== EXPENSE TRACKER =====
                    1. Add Expense
                    2. Display Expenses
                    3. Delete Expense by ID
                    4. Exit
                    """);

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addExpense();
                case "2" -> displayExpenses();
                case "3" -> deleteRecord();
                case "4" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println(
                        "Invalid choice. Please choose 1, 2, 3, or 4.");
            }
        }
    }

    // Add an expense
    private void addExpense() {
        try {
            System.out.print(
                    "Enter date (yyyyMMdd, e.g. 20261026): ");
            String dateInput = scanner.nextLine().trim();

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("uuuuMMdd");

            LocalDate date = LocalDate.parse(dateInput, formatter);

            System.out.print("Enter expense description: ");
            String description = scanner.nextLine().trim();

            if (description.isEmpty()) {
                System.out.println("Description cannot be empty.");
                return;
            }

            System.out.print("Enter amount spent: ");
            BigDecimal amount =
                    new BigDecimal(scanner.nextLine().trim());

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println(
                        "Amount must be greater than zero.");
                return;
            }

            System.out.println("""
                    Choose category:
                    FOOD
                    SHOPPING
                    WELLNESS
                    PARTY
                    HANGOUT
                    MISCELLANEOUS
                    """);

            System.out.print("Enter category: ");
            String categoryInput =
                    scanner.nextLine().trim().toUpperCase();

            Category category = Category.valueOf(categoryInput);

            Expense expense = repository.addExpense(
                    date, description, amount, category);

            System.out.println("\nExpense saved successfully!");
            System.out.println("ID: " + expense.getId());
            System.out.println("Description: " + expense.getDescription());
            System.out.println("Amount: " + expense.getAmount());
            System.out.println("Category: " + expense.getCategory());
            System.out.println("Date: " + expense.getDate());

        } catch (DateTimeParseException e) {
            System.out.println(
                    "Invalid date. Use yyyyMMdd and enter a real date.");
        } catch (NumberFormatException e) {
            System.out.println(
                    "Invalid amount. Example: 250.50");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Invalid category. Please choose a listed category.");
        } catch (Exception e) {
            System.out.println(
                    "Could not save expense: " + e.getMessage());
        }
    }

    // Display submenu
    private void displayExpenses() {
        System.out.println("""
                
                ===== DISPLAY EXPENSES =====
                1. Display All Expenses
                2. Display Monthly Expenses
                """);

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> displayAllExpenses();
            case "2" -> displayMonthlyExpenses();
            default -> System.out.println(
                    "Invalid choice. Please choose 1 or 2.");
        }
    }

    // Display all expenses
    private void displayAllExpenses() {
        List<Expense> expenses = repository.findAll();

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        printExpenses(expenses);
    }

    // Display expenses for a selected month and year
    private void displayMonthlyExpenses() {
        try {
            System.out.print("Enter year (e.g. 2026): ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter month (1-12): ");
            int month = Integer.parseInt(scanner.nextLine().trim());

            if (month < 1 || month > 12) {
                System.out.println(
                        "Month must be between 1 and 12.");
                return;
            }

            List<Expense> expenses =
                    repository.findAllbyMonth(year, month);

            if (expenses.isEmpty()) {
                System.out.println(
                        "No expenses found for that month.");
                return;
            }

            printExpenses(expenses);

        } catch (NumberFormatException e) {
            System.out.println(
                    "Please enter a valid numeric year and month.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date.");
        } catch (java.time.DateTimeException e) {
            System.out.println("Invalid year or month.");
        }
    }

    // Shared table-printing method

    private void printExpenses(List<Expense> expenses) {
        System.out.println();

        System.out.printf(
                "%-5s | %-25s | %12s | %-16s | %-12s%n",
                "ID", "DESCRIPTION", "AMOUNT", "CATEGORY", "DATE"
        );

        System.out.println("-".repeat(84));

        BigDecimal total = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            System.out.printf(
                    "%-5d | %-25.25s | %12.2f | %-16s | %-12s%n",
                    expense.getId(),
                    expense.getDescription(),
                    expense.getAmount(),
                    expense.getCategory(),
                    expense.getDate()
            );

            total = total.add(expense.getAmount());
        }

        System.out.println("-".repeat(84));

        System.out.printf("Number of expenses: %d%n", expenses.size());
        System.out.printf("Total expenses:     ₹%.2f%n", total);

        System.out.println("-".repeat(84));
    }


    // Delete an expense by ID
    private void deleteRecord() {
        try {
            System.out.print("Enter expense ID to delete: ");
            long id = Long.parseLong(scanner.nextLine().trim());

            if (id <= 0) {
                System.out.println("ID must be greater than zero.");
                return;
            }

            System.out.print(
                    "Are you sure you want to delete expense ID "
                            + id + "? (yes/no): ");

            String confirmation =
                    scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            boolean deleted = repository.deleteById(id);

            if (deleted) {
                System.out.println(
                        "Expense with ID " + id + " deleted successfully.");
            } else {
                System.out.println(
                        "No expense found with ID " + id + ".");
            }

        } catch (NumberFormatException e) {
            System.out.println(
                    "Invalid ID. Please enter a numeric ID.");
        } catch (Exception e) {
            System.out.println(
                    "Could not delete expense: " + e.getMessage());
        }
    }
}
