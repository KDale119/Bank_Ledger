import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Accounts {
    Scanner scanner = new Scanner(System.in);

    public static void opts(Scanner scanner) {
        System.out.print("\nWould you like to:\nC - Create an account\nU - Update an account\nD - Delete an account \n\nSelection: ");
        String answer = scanner.nextLine();
//        while (!answer.equalsIgnoreCase("c") || !answer.equalsIgnoreCase("u") || !answer.equalsIgnoreCase("d")) {
//            System.out.println("Invalid Selection, going back to main menu.");
//            Ledger.printMenu();
//        }
        if (answer.equalsIgnoreCase("c")) {
            createAccount(scanner);
        } else if (answer.equalsIgnoreCase("u")) {
            updateAccount(scanner);
        } else {
            deleteAcc(scanner);
        }
    }

    public static void createAccount(Scanner scanner) {
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(url);
            PreparedStatement statement = connect.prepareStatement("select ID from Customer where Name = (?)");
            System.out.print("PLease Enter Customer Name: ");
            String name = scanner.nextLine();
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();


            String customerID = rs.getString("ID"); // assigning ID from Customers to Accounts
            System.out.print("Please Enter Account Name: ");
            String accName = scanner.nextLine();
            String accID = String.valueOf(UUID.randomUUID());

            //accnumber generator
            long smallest = 1000_0000_0000_0000L;
            long biggest = 9999_9999_9999_9999L;
            long accNum = ThreadLocalRandom.current().nextLong(smallest, biggest);

            statement = connect.prepareStatement("insert into Account (CustomerID, Name, Balance, AccountNumber, ID) values((?), (?), (?), (?), (?))");
            statement.setString(1, customerID);
            statement.setString(2, accName);
            statement.setDouble(3, 0.0);
            statement.setLong(4, accNum);
            statement.setString(5, accID);
            statement.execute();
            System.out.println("\nAccount created with account number: " + accNum);

        } catch (SQLException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                System.out.println("error");
            }
        }
    }

    public static void updateAccount(Scanner scanner) {
        System.out.print("Please Enter Account Number: ");
        String accNum = scanner.nextLine();
        System.out.print("Please Enter New Account Name: ");
        String newAccName = scanner.nextLine();
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(url);
            PreparedStatement statement = connect.prepareStatement("update Account set Name = (?) where AccountNumber= (?)");
            statement.setString(1, newAccName);
            statement.setString(2, accNum);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("error");
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                System.out.println("error");
            }
        }
    }

    public static void deleteAcc(Scanner scanner) {
        System.out.print("Please Enter Account Number: ");
        String accNum = scanner.nextLine();
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(url);
            PreparedStatement statement = connect.prepareStatement("select ID from Account where AccountNumber = (?)");
            statement.setString(1, accNum);
            ResultSet rs = statement.executeQuery();

            String transAccID = rs.getString("ID");

            statement = connect.prepareStatement("delete from Account where AccountNumber = (?)");
            statement.setString(1, accNum);
            statement.execute();

            statement = connect.prepareStatement("delete from Trans where AccountID = (?)");
            statement.setString(1, transAccID);
            statement.execute();

        } catch (SQLException e) {
            System.out.println("error");
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                System.out.println("error");
            }
        }
        System.out.println("Account Closed");
    }
}
