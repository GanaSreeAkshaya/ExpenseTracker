package com.example.ExpenseTracker;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpenseTest {

    @Test
    void shouldStoreAndReturnExpenseDetails() {
        Expense expense = new Expense();

        expense.setDescription("Lunch");
        expense.setAmount(new BigDecimal("150.00"));
        expense.setDate(LocalDate.of(2026, 10, 28));

        assertEquals("Lunch", expense.getDescription());
        assertEquals(new BigDecimal("150.00"), expense.getAmount());
        assertEquals(LocalDate.of(2026, 10, 28), expense.getDate());
    }
}