import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

public class Reports {
    BufferedReader reader = null;
    Scanner scanner = new Scanner(System.in);


    public static void statement(Scanner scanner) {
        System.out.print("Please enter a Valid File Path to Create the File: ");
        String filePath = scanner.nextLine();
        if (filePath.isEmpty() || !Files.exists(Paths.get(filePath))) {
            System.out.print("Invalid Directory, Try Again: ");
            filePath = scanner.nextLine();
        }
        System.out.print("\nPlease Enter Customer Name: ");
        String custName = scanner.nextLine();
        String custNameTrim = custName.replaceAll("\\s", "");

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));



        //API CONNECTION
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        Connection connect = null;
        BufferedWriter writer = null;
        try {
            connect = DriverManager.getConnection(url);

            // GETTING CustomerID
            PreparedStatement custStatement = connect.prepareStatement("select ID from Customer where Name = (?)");
            custStatement.setString(1, custName);
            ResultSet rs = custStatement.executeQuery();
            String custID = rs.getString("ID");

            // for loops for each acc here
            // GETTING ACCOUNTID FROM CustomerID
            PreparedStatement accountID = connect.prepareStatement("select ID, Name, Balance from Account where CustomerID = (?)");
            accountID.setString(1, custID);
            ResultSet accRS = accountID.executeQuery();
            String accID = accRS.getString("ID");
            String accName = accRS.getString("Name");
            String accBalance = accRS.getString("Balance");

            // GETTING TRANSACTIONS FROM AccountID
            PreparedStatement transStatement = connect.prepareStatement("select * from Trans where AccountID = (?)");
            transStatement.setString(1, accID);
            ResultSet transrs = transStatement.executeQuery();
//            String transFullBalance = transrs.getString("Balance"); // make it to add total of accs


            // JAVA FILE
            File bankStatement = new File(filePath + "\\" + custNameTrim + ".txt");
            writer = new BufferedWriter(new FileWriter(bankStatement));
            writer.write("MCC Code School Bank Statement\n");

            writer.write("\nStatement for " + custName + "\n");
            writer.write("Statement Date: " + currentDate + "\n");

            //Current total balance here

            writer.write("\n" + accName + " - $" + accBalance + " Balance\n"); // needs to be from ACCOUNT
            writer.write("Transactions:\n");

            while (transrs.next()) {
                String dateAndTime = transrs.getString("TransTime");
                String date = dateAndTime.split(" ")[0];
                String type = transrs.getString("Type");
                String amount = transrs.getString("Amount");
                String merchantName = transrs.getString("MerchantName");
                if (transrs.getString("MerchantName") != null) {
                    writer.write(date + " ");
                    writer.write(merchantName + " ");
                    if (type.equalsIgnoreCase("debit")) {
                        writer.write("-$");
                    } else {
                        writer.write("+$");
                    }
                    writer.write(amount + "\n");
                } else {
                    if (type.equals("C")) {
                        writer.write(date + " ");
                        writer.write("Deposit ");
                        writer.write("+$" + amount + "\n");
                    } else {
                        writer.write(date + " ");
                        writer.write("Withdrawl ");
                        writer.write("-$" + amount + "\n");
                    }
                }

            }

        } catch (IOException e) {
            System.out.println("error 1");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("error 2");
            e.printStackTrace();
        } finally {
            try {
                if (connect != null && writer != null) {
                    connect.close();
                    writer.close();
                }
            } catch (SQLException e) {
                System.out.println("error 3");
            } catch (IOException e) {
                System.out.println("error 4");
            }
        }
        System.out.println("\nGenerated Statement for " + custName);
    }
}
