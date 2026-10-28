
package com.example.ExpenseTracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ExpenseJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ExpenseJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Add a new expense
    public Expense addExpense(
            LocalDate date,
            String description,
            BigDecimal amount,
            Category category) {

        String sql = """
                INSERT INTO expenses
                (description, amount, category, expense_date)
                VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, description);
            ps.setBigDecimal(2, amount);
            ps.setString(3, category.name());
            ps.setDate(4, Date.valueOf(date));

            return ps;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();

        if (generatedId == null) {
            throw new IllegalStateException(
                    "Expense was saved, but its generated ID was not returned.");
        }

        Expense expense = new Expense();
        expense.setId(generatedId.longValue());
        expense.setDate(date);
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setCategory(category);

        return expense;
    }

    // Display all expenses
    public List<Expense> findAll() {
        String sql = """
                SELECT id, description, amount, category, expense_date
                FROM expenses
                ORDER BY expense_date DESC, id DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Expense expense = new Expense();

            expense.setId(rs.getLong("id"));
            expense.setDescription(rs.getString("description"));
            expense.setAmount(rs.getBigDecimal("amount"));
            expense.setCategory(
                    Category.valueOf(
                            rs.getString("category").trim().toUpperCase()
                    )
            );
            expense.setDate(
                    rs.getDate("expense_date").toLocalDate()
            );

            return expense;
        });
    }

    // Display expenses for a particular month and year
    public List<Expense> findAllbyMonth(int year, int month) {
        String sql = """
                SELECT id, description, amount, category, expense_date
                FROM expenses
                WHERE expense_date >= ?
                  AND expense_date < ?
                ORDER BY expense_date DESC, id DESC
                """;

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Expense expense = new Expense();

                    expense.setId(rs.getLong("id"));
                    expense.setDescription(rs.getString("description"));
                    expense.setAmount(rs.getBigDecimal("amount"));
                    expense.setCategory(
                            Category.valueOf(
                                    rs.getString("category")
                                            .trim()
                                            .toUpperCase()
                            )
                    );
                    expense.setDate(
                            rs.getDate("expense_date").toLocalDate()
                    );

                    return expense;
                },
                Date.valueOf(startDate),
                Date.valueOf(endDate)
        );
    }

    // Delete an expense by ID
    // Returns true if a row was deleted, otherwise false
    public boolean deleteById(long id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        int rowsDeleted = jdbcTemplate.update(sql, id);

        return rowsDeleted > 0;
    }
}
