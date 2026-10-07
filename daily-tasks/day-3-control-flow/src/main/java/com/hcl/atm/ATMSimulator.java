package com.hcl.atm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Daily Task 3: Control Flow + Maven
 * Topic: Java Control Flow Statements (do-while, switch, break, continue, enhanced-for)
 *        and Maven Project Configuration with Profiles (dev/prod).
 */
public class ATMSimulator {

    private static final int CORRECT_PIN = 1234;
    private static final int MAX_PIN_ATTEMPTS = 3;

    private static String bankName = "HCL Community Bank";
    private static String environment = "DEVELOPMENT";
    private static double maxWithdrawalLimit = 20000.0;

    private static double balance = 15000.00;
    private static final List<String> transactionHistory = new ArrayList<>();

    static {
        loadProperties();
        transactionHistory.add("ACCOUNT OPENED: Initial Balance = $" + String.format("%.2f", balance));
    }

    private static void loadProperties() {
        try (InputStream input = ATMSimulator.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                if (prop.containsKey("atm.bank.name")) {
                    bankName = prop.getProperty("atm.bank.name");
                }
                if (prop.containsKey("app.environment")) {
                    environment = prop.getProperty("app.environment");
                }
                if (prop.containsKey("atm.max.withdrawal.limit")) {
                    maxWithdrawalLimit = Double.parseDouble(prop.getProperty("atm.max.withdrawal.limit"));
                }
            }
        } catch (Exception e) {
            // Graceful fallback to default configuration
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===============================================================");
        System.out.println("            WELCOME TO " + bankName.toUpperCase());
        System.out.println("            Environment: [" + environment + "]");
        System.out.println("===============================================================\n");

        // 1. PIN Authentication with Maximum 3 Attempts and `break` on lockout
        boolean isAuthenticated = false;
        int attempts = 0;

        while (attempts < MAX_PIN_ATTEMPTS) {
            attempts++;
            System.out.print("Enter your 4-digit security PIN (Attempt " + attempts + "/" + MAX_PIN_ATTEMPTS + "): ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input format! PIN must consist of numbers only.\n");
                scanner.nextLine(); // Clear buffer
                continue; // Skip rest of loop and retry
            }

            int enteredPin = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (enteredPin == CORRECT_PIN) {
                isAuthenticated = true;
                System.out.println("✅ PIN Verified Successfully! Access Granted.\n");
                break; // Break out of PIN verification loop
            } else {
                int remaining = MAX_PIN_ATTEMPTS - attempts;
                if (remaining > 0) {
                    System.out.println("❌ Incorrect PIN. You have " + remaining + " attempt(s) remaining.\n");
                }
            }
        }

        if (!isAuthenticated) {
            System.out.println("🚨 Card Blocked: Maximum PIN attempts exceeded (3/3). Please contact your branch.");
            return;
        }

        // 2. Interactive Menu using `do-while` loop and `switch` statement
        int userChoice = 0;
        do {
            printMenu();
            System.out.print("Select an option (1-5): ");

            // Robust Input Validation: never crashes on unexpected strings/symbols
            if (!scanner.hasNextInt()) {
                System.out.println("⚠️  Invalid entry! Please enter a numerical choice from 1 to 5.\n");
                scanner.nextLine(); // Flush invalid token
                continue; // Continue to redisplay menu
            }

            userChoice = scanner.nextInt();
            scanner.nextLine(); // Clear newline

            switch (userChoice) {
                case 1:
                    handleCheckBalance();
                    break;

                case 2:
                    handleDeposit(scanner);
                    break;

                case 3:
                    handleWithdrawal(scanner);
                    break;

                case 4:
                    handleMiniStatement();
                    break;

                case 5:
                    System.out.println("\n👋 Thank you for banking with " + bankName + ". Have a great day!");
                    break;

                default:
                    System.out.println("⚠️  Option out of range! Please choose between 1 and 5.\n");
                    break;
            }

        } while (userChoice != 5);
    }

    private static void printMenu() {
        System.out.println("---------------- ATM MAIN MENU ----------------");
        System.out.println("1. Check Account Balance");
        System.out.println("2. Deposit Funds");
        System.out.println("3. Withdraw Cash");
        System.out.println("4. Mini-Statement (Recent Transactions)");
        System.out.println("5. Exit & Eject Card");
        System.out.println("-----------------------------------------------");
    }

    private static void handleCheckBalance() {
        System.out.printf("\n💰 Current Available Balance: $%,.2f\n\n", balance);
    }

    private static void handleDeposit(Scanner scanner) {
        System.out.print("\nEnter deposit amount ($): ");
        if (!scanner.hasNextDouble()) {
            System.out.println("❌ Invalid amount! Deposit must be a positive number.\n");
            scanner.nextLine();
            return;
        }

        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("❌ Deposit amount must be greater than $0.00.\n");
            return;
        }

        balance += amount;
        String tx = "DEPOSIT : +$" + String.format("%.2f", amount) + " | Balance = $" + String.format("%.2f", balance);
        transactionHistory.add(tx);
        System.out.printf("✅ Successfully deposited $%,.2f. New Balance: $%,.2f\n\n", amount, balance);
    }

    private static void handleWithdrawal(Scanner scanner) {
        System.out.printf("\nEnter withdrawal amount ($) [Max Limit per Tx: $%,.2f]: ", maxWithdrawalLimit);
        if (!scanner.hasNextDouble()) {
            System.out.println("❌ Invalid amount! Withdrawal must be a numerical value.\n");
            scanner.nextLine();
            return;
        }

        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("❌ Withdrawal amount must be greater than $0.00.\n");
            return;
        }

        if (amount > maxWithdrawalLimit) {
            System.out.printf("❌ Transaction Declined: Amount exceeds single transaction limit of $%,.2f.\n\n", maxWithdrawalLimit);
            return;
        }

        if (amount > balance) {
            System.out.printf("❌ Insufficient Funds! Current balance is $%,.2f.\n\n", balance);
            return;
        }

        balance -= amount;
        String tx = "WITHDRAW: -$" + String.format("%.2f", amount) + " | Balance = $" + String.format("%.2f", balance);
        transactionHistory.add(tx);
        System.out.printf("✅ Successfully dispensed $%,.2f. Remaining Balance: $%,.2f\n\n", amount, balance);
    }

    private static void handleMiniStatement() {
        System.out.println("\n============ MINI-STATEMENT (TRANSACTION HISTORY) ============");
        // 3. Enhanced-for loop for printing statement
        int count = 1;
        for (String tx : transactionHistory) {
            System.out.printf("[%02d] %s\n", count++, tx);
        }
        System.out.println("==============================================================\n");
    }
}
