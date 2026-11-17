
package org.example;

import org.example.model.BankAccount;
import org.example.model.CheckingAccount;
import org.example.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientInterface {

    private final List<BankAccount> accounts = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ClientInterface clientInterface = new ClientInterface();
        clientInterface.start();
    }

    // Головне меню
    public void start() {

        while (true) {
            try {
                System.out.println("\n==============================");
                System.out.println("       БАНКІВСЬКА СИСТЕМА");
                System.out.println("==============================");
                System.out.println("1 - Створити рахунок");
                System.out.println("2 - Видалити рахунок");
                System.out.println("3 - Поповнити рахунок");
                System.out.println("4 - Зняти кошти");
                System.out.println("5 - Переглянути інформацію");
                System.out.println("6 - Вийти");
                System.out.print("\nОберіть дію: ");

                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1:
                        addNewAccount();
                        break;
                    case 2:
                        deleteAccount();
                        break;
                    case 3:
                        deposit();
                        break;
                    case 4:
                        withdraw();
                        break;
                    case 5:
                        printInfo();
                        break;
                    case 6:
                        System.out.println("Дякуємо за використання програми!");
                        return;
                    default:
                        System.out.println("Невірний пункт меню!");
                }

            } catch (RuntimeException e) {
                System.err.println("\nПомилка: " + e.getMessage());
            }
        }
    }

    // Створення нового рахунку
    private void addNewAccount() {

        System.out.println("\n--- Створення нового рахунку ---");

        System.out.print("Введіть ім'я клієнта: ");
        String name = scanner.nextLine();

        System.out.print("Введіть ID користувача: ");
        Long userId = readUserId();

        System.out.print("Введіть пароль: ");
        String password = scanner.nextLine();

        System.out.print("Підтвердіть пароль: ");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordException(
                    "Паролі не збігаються!"
            );
        }

        for (BankAccount account : accounts) {
            if (account.getUserId().equals(userId)) {
                throw new DuplicateUserIdException(
                        "Користувач з таким ID вже існує!"
                );
            }
        }

        CheckingAccount account =
                new CheckingAccount(name, userId, password);

        accounts.add(account);

        System.out.println("Рахунок успішно створено!");
    }

    // Видалення рахунку
    private void deleteAccount() {

        System.out.println("\n--- Видалення рахунку ---");

        CheckingAccount account = authenticate();

        if (account.getAccountBalance() != 0.0) {
            throw new AccountException(
                    "Неможливо видалити рахунок з ненульовим балансом!"
            );
        }

        accounts.remove(account);

        System.out.println("Рахунок успішно видалено!");
    }

    // Поповнення рахунку
    private void deposit() {

        System.out.println("\n--- Поповнення рахунку ---");

        CheckingAccount account = authenticate();

        System.out.print("Введіть суму поповнення: ");
        double amount = readAmount();

        account.deposit(amount);

        System.out.println("Рахунок успішно поповнено!");
        System.out.println(
                "Поточний баланс: " + account.getAccountBalance()
        );
    }

    // Зняття коштів
    private void withdraw() {

        System.out.println("\n--- Зняття коштів ---");

        CheckingAccount account = authenticate();

        System.out.print("Введіть суму зняття: ");
        double amount = readAmount();

        account.withdraw(amount);

        System.out.println("Кошти успішно знято!");
        System.out.println(
                "Поточний баланс: " + account.getAccountBalance()
        );
    }

    // Виведення інформації про рахунок
    private void printInfo() {

        System.out.println("\n--- Інформація про рахунок ---");

        CheckingAccount account = authenticate();

        System.out.println("\nІнформація про рахунок:");
        System.out.println("Ім'я клієнта: " +
                account.getCustomerName());

        System.out.println("ID користувача: " +
                account.getUserId());

        System.out.println("Баланс рахунку: " +
                account.getAccountBalance());

        System.out.println("Тип рахунку: Checking");

        System.out.println("Денний ліміт зняття: " +
                account.getDailyWithdrawalLimit());
    }

    // Перевірка ID та пароля
    private CheckingAccount authenticate() {

        System.out.print("Введіть ID користувача: ");
        Long userId = readUserId();

        System.out.print("Введіть пароль: ");
        String password = scanner.nextLine();

        for (BankAccount account : accounts) {

            if (account.getUserId().equals(userId)) {

                if (!account.checkPassword(password)) {
                    throw new InvalidPasswordException(
                            "Неправильний пароль!"
                    );
                }

                return (CheckingAccount) account;
            }
        }

        throw new AccountNotFoundException(
                "Рахунок з таким ID не знайдено!"
        );
    }

    // Зчитування ID
    private Long readUserId() {

        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new AccountException(
                    "ID повинен бути цілим числом!"
            );
        }
    }

    // Зчитування суми
    private double readAmount() {

        try {
            return Double.parseDouble(
                    scanner.nextLine().replace(',', '.')
            );
        } catch (NumberFormatException e) {
            throw new InvalidAmountException(
                    "Некоректно введена сума!"
            );
        }
    }
}
