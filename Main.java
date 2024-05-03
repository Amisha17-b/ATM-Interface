import java.util.ArrayList;
import java.util.Scanner;

// The main class representing the ATM system
class ATM {
    private ArrayList<User> users;
    private User currentUser;

    // Constructor to initialize the ATM with some dummy users
    public ATM() {
        this.users = new ArrayList<>();
        // Add some dummy users for testing
        this.users.add(new User("123456", "7890"));
        this.users.add(new User("987654", "5432"));
    }

    // Method to start the ATM system
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the ATM!");

        // Prompt the user to enter user ID and PIN for authentication
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        // Authenticate the user
        if (authenticateUser(userId, pin)) {
            System.out.println("Authentication successful!");
            displayMenu();
            int choice;
            // Display menu options and handle user choices
            do {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                switch (choice) {
                    case 1:
                        showTransactionHistory();
                        break;
                    case 2:
                        performWithdrawal();
                        break;
                    case 3:
                        performDeposit();
                        break;
                    case 4:
                        performTransfer();
                        break;
                    case 5:
                        System.out.println("Thank you for using the ATM. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 5);
        } else {
            System.out.println("Authentication failed. Please try again.");
        }
    }
    // Method to authenticate the user
    private boolean authenticateUser(String userId, String pin) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getPin().equals(pin)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    // Method to display the ATM menu
    private void displayMenu() {
        System.out.println("ATM Menu:");
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    // Method to display transaction history for the current user
    private void showTransactionHistory() {
        System.out.println("Transaction History:");
        for (Transaction transaction : currentUser.getTransactionHistory()) {
            System.out.println(transaction);
        }
    }

    // Method to perform a withdrawal transaction
    private void performWithdrawal() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character
        currentUser.withdraw(amount);
    }

    // Method to perform a deposit transaction
    private void performDeposit() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character
        currentUser.deposit(amount);
    }

    // Method to perform a transfer transaction
    private void performTransfer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter recipient's user ID: ");
        String recipientUserId = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        User recipient = null;
        // Find the recipient user by user ID
        for (User user : users) {
            if (user.getUserId().equals(recipientUserId)) {
                recipient = user;
                break;
            }
        }

        if (recipient != null) {
            currentUser.transfer(amount, recipient);
        } else {
            System.out.println("Recipient user ID not found.");
        }
    }
}

// Class representing a user of the ATM system
class User {
    private String userId;
    private String pin;
    private double balance;
    private ArrayList<Transaction> transactionHistory;

     // Constructor to initialize a user with a user ID and PIN
    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
 
    // Method to perform a withdrawal transaction
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Withdrawal(amount));
            System.out.println("Withdrawal successful. New balance: " + balance);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

    // Method to perform a deposit transaction
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add(new Deposit(amount));
            System.out.println("Deposit successful. New balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Method to perform a transfer transaction
    public void transfer(double amount, User recipient) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
            transactionHistory.add(new Transfer(amount, recipient.getUserId()));
            System.out.println("Transfer successful. New balance: " + balance);
        } else {
            System.out.println("Invalid transfer amount or insufficient funds.");
        }
    }
}

// Abstract class representing a transaction
abstract class Transaction {
    protected double amount;

    // Constructor to initialize a transaction with an amount
    public Transaction(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transaction: " + getClass().getSimpleName() + ", Amount: " + amount;
    }
}

// Class representing a withdrawal transaction
class Withdrawal extends Transaction {
    public Withdrawal(double amount) {
        super(amount);
    }
}
// Class representing a deposit transaction
class Deposit extends Transaction {
    public Deposit(double amount) {
        super(amount);
    }
}

// Class representing a transfer transaction
class Transfer extends Transaction {
    private String recipientUserId;

    // Constructor to initialize a transfer transaction with an amount and recipient user ID
    public Transfer(double amount, String recipientUserId) {
        super(amount);
        this.recipientUserId = recipientUserId;
    }

    @Override
    public String toString() {
        return super.toString() + ", Recipient: " + recipientUserId;
    }
}
// Main class to run the ATM system
public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
