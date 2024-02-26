

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.UUID;

public class Transactions {
    public static void opts(Scanner scanner) {
        System.out.print("\nD - Deposit Funds\nW - Withdrawal Fund\n");
        System.out.print("Selection: ");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("d")) {
            deposit(scanner);
        } else if (answer.equalsIgnoreCase("w")) {
            withdrawal(scanner);
        }
    }

    // deposits
    public static void deposit(Scanner scanner) {
        System.out.print("\nPlease Enter Account Number: ");
        String accNum = scanner.nextLine();
        System.out.print("Please Enter the Amount to Deposit: ");
        Double dep = scanner.nextDouble();
        scanner.nextLine();
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(url);
            PreparedStatement statement = connect.prepareStatement("update Account set Balance =  Balance + (?) where AccountNumber = (?)");
            statement.setDouble(1, dep);
            statement.setString(2, accNum);
            statement.execute();


            statement = connect.prepareStatement("select ID from Account where AccountNumber = (?)");
            statement.setString(1, accNum);
            ResultSet rs = statement.executeQuery();
            String transID = String.valueOf(UUID.randomUUID());
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy-HH:mm:ss:ns"));
            String date = currentDate.split("-")[0];
            String time = currentDate.split("-")[1];
            String transAccID = rs.getString("ID");

            statement = connect.prepareStatement("insert into Trans (AccountID, ID, Type, Amount, TransTime) values ((?), (?), (?), (?), (?))");
            statement.setString(1, transAccID);
            statement.setString(2, transID);
            statement.setString(3, "C");
            statement.setDouble(4, dep);
            statement.setString(5,date + " " + time);


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
    //                         WITHDRAWALS
    public static void withdrawal(Scanner scanner) {
        System.out.print("\nPlease Enter Account Number: ");
        String accNum = scanner.nextLine();
        System.out.print("Please Enter the Amount to Withdrawal: ");
        Double withdrawal = scanner.nextDouble();
        scanner.nextLine();
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(url);
            PreparedStatement statement = connect.prepareStatement("update Account set Balance =  Balance - (?) where AccountNumber = (?)");
            statement.setDouble(1, withdrawal);
            statement.setString(2, accNum);
            statement.execute();

            statement = connect.prepareStatement("select ID from Account where AccountNumber = (?)");
            statement.setString(1, accNum);
            ResultSet rs = statement.executeQuery();
            String transID = String.valueOf(UUID.randomUUID());
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy-HH:mm:ss:ns"));
            String date = currentDate.split("-")[0];
            String time = currentDate.split("-")[1];
            String transAccID = rs.getString("ID");
            statement = connect.prepareStatement("insert into Trans (AccountID, ID, Type, Amount, TransTime) values ((?), (?), (?), (?), (?))");
            statement.setString(1, transAccID);
            statement.setString(2, transID);
            statement.setString(3, "D");
            statement.setDouble(4, withdrawal);
            statement.setString(5,date + " " + time);

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
        System.out.println("\n");
    }
    public static void simulate(Scanner scanner) {
        System.out.print("Please Enter Customer Account Number: ");
        String accNum = scanner.nextLine();


        //           SQL CONNECTION
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        Connection connect = null;
        String custID = null;
        try {
            connect = DriverManager.getConnection(url);
            PreparedStatement statement = connect.prepareStatement("select CustomerID from Account where AccountNumber = (?)");
            statement.setString(1, accNum);
            ResultSet rs = statement.executeQuery();
            custID = rs.getString("CustomerID");
        } catch (SQLException e) {
            System.out.println("cust id error");
            e.printStackTrace();
        }
        finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                System.out.println("close error");
            }
        }

        //           API CONNECTION
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://mcc-code-school-transaction-simulator.us-east-2.elasticbeanstalk.com/transaction/" + custID))
                .build();
        HttpResponse<String> response = null;


        try {
            //     API
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Simulate simulate = new Gson().fromJson(response.body(), Simulate.class);

            //    SQL
            connect = DriverManager.getConnection(url);
            PreparedStatement statement = connect.prepareStatement("select ID from Account where AccountNumber = (?)");
            statement.setString(1, accNum);
            ResultSet rs = statement.executeQuery();

            String transAccID = rs.getString("ID");

            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy-HH:mm:ss:ns"));
            String date = currentDate.split("-")[0];
            String time = currentDate.split("-")[1];
            statement = connect.prepareStatement("insert into Trans (ID, AccountID, Amount, Type, MerchantName, MerchantType, TransTime) values ((?), (?), (?), (?), (?), (?), (?))");
            statement.setString(1, simulate.getTransactionId());
            statement.setString(2, transAccID);
            statement.setDouble(3, simulate.getAmount());
            statement.setString(4, simulate.getTransactionType());
            statement.setString(5, simulate.getRecipient().getMerchantName());
            statement.setString(6, simulate.getRecipient().getMerchantType());
            statement.setString(7, date + " " + time);

            statement.execute();


        } catch (IOException e) {
            System.out.println("error");
        } catch (InterruptedException e) {
            System.out.println("error");
        } catch (SQLException e) {
            System.out.println("SQL ERROR");
            e.printStackTrace();
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }
        System.out.println("Fetching Data...");
        System.out.println("Transaction Simulated");
    }
}
