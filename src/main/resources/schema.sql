CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(200) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    category VARCHAR(50),
    expense_date DATE NOT NULL
);