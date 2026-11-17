package org.example.model;

import org.example.exceptions.AccountException;
import org.example.exceptions.InvalidAmountException;
import org.example.exceptions.InsufficientBalanceException;

import java.time.LocalDate;

public class CheckingAccount extends BankAccount {

    private double dailyWithdrawalLimit = 300.0;

    private double withdrawnToday = 0.0;
    private LocalDate lastWithdrawalDate = LocalDate.now();

    // Конструктор
    public CheckingAccount(String customerName, Long userId,
                           String userPassword) {
        super(customerName, userId, userPassword);
    }

    // Геттер
    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    // Сеттер
    public void setDailyWithdrawalLimit(double limit) {
        if (!Double.isFinite(limit) || limit <= 0) {
            throw new InvalidAmountException(
                    "Денний ліміт повинен бути більшим за 0!"
            );
        }

        dailyWithdrawalLimit = limit;
    }

    // Перевизначення методу зняття коштів
    @Override
    public void withdraw(double amount) {

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidAmountException(
                    "Сума зняття повинна бути більшою за 0!"
            );
        }

        // Перевірка нового дня
        if (!LocalDate.now().equals(lastWithdrawalDate)) {
            withdrawnToday = 0.0;
            lastWithdrawalDate = LocalDate.now();
        }

        if (withdrawnToday + amount > dailyWithdrawalLimit) {
            throw new AccountException(
                    "Перевищено денний ліміт зняття коштів!"
            );
        }

        if (amount > getAccountBalance()) {
            throw new InsufficientBalanceException(
                    "Недостатньо коштів на рахунку!"
            );
        }

        super.withdraw(amount);
        withdrawnToday += amount;
    }
}