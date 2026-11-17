package org.example.model;

import org.example.exceptions.InvalidAmountException;
import org.example.exceptions.InvalidPasswordException;
import org.example.exceptions.InsufficientBalanceException;

public class BankAccount {

    private String customerName;
    private Long userId;
    private String userPassword;
    private Double accountBalance;

    // Конструктор
    public BankAccount(String customerName, Long userId, String userPassword) {

        if (customerName == null || customerName.isBlank()) {
            throw new InvalidAmountException(
                    "Ім'я клієнта не може бути порожнім!"
            );
        }

        validatePassword(userPassword);

        this.customerName = customerName;
        this.userId = userId;
        this.userPassword = userPassword;
        this.accountBalance = 0.0;
    }

    // Перевірка пароля
    private void validatePassword(String password) {
        if (password == null ||
                password.length() < 8 ||
                !password.matches(".*\\d.*")) {

            throw new InvalidPasswordException(
                    "Пароль повинен містити щонайменше 8 символів та одну цифру!"
            );
        }
    }

    // Геттери

    public String getCustomerName() {
        return customerName;
    }

    public Long getUserId() {
        return userId;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    // Перевірка пароля користувача
    public boolean checkPassword(String password) {
        return userPassword.equals(password);
    }

    // Внесення коштів
    public void deposit(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidAmountException(
                    "Сума внеску повинна бути більшою за 0!"
            );
        }

        accountBalance += amount;
    }

    // Зняття коштів
    public void withdraw(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidAmountException(
                    "Сума зняття повинна бути більшою за 0!"
            );
        }

        if (amount > accountBalance) {
            throw new InsufficientBalanceException(
                    "Недостатньо коштів на рахунку!"
            );
        }

        accountBalance -= amount;
    }
}