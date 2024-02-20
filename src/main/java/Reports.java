import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Reports {
//    BufferedReader reader = null;
//    Scanner scanner = new Scanner(System.in);
//
//
//    public static void statement(Scanner scanner){
//        System.out.println("Please enter a Valid File Path to Create the File: ");
//        String filePath = scanner.nextLine();
//
//        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
//        Connection connect = null;
//        BufferedWriter writer = null;
//        try {
//            connect = DriverManager.getConnection(url);
//            PreparedStatement statement = connect.prepareStatement("select ID from Account where AccountNumber = (?)");
//            File bankStatement = new File(filePath);
//            writer = new BufferedWriter(new FileWriter(bankStatement));
//
//            statement.execute();
//
//        } catch (SQLException | IOException e) {
//            System.out.println("error");
//        } finally {
//            try {
//                if (connect != null) {
//                    connect.close();
//                }
//            } catch (SQLException e) {
//                System.out.println("error");
//            }
//        }

//        System.out.println("\nStatement for " + custName);
//        System.out.println("Statement Date: " + currDate);
//        System.out.println("Current Total Balance: " + Balance);
//    }
}
