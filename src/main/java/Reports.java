import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Reports {
    BufferedReader reader = null;
    Scanner scanner = new Scanner(System.in);


    public static void statement(Scanner scanner) {
        System.out.print("Please enter a Valid File Path to Create the File: ");
        String filePath = scanner.nextLine();

        System.out.print("\nPlease Enter Customer Name: ");
        String custName = scanner.nextLine();

        String currDate = LocalDateTime.now().toString();

        //API CONNECTION
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        Connection connect = null;
        BufferedWriter writer = null;
        try {
            connect = DriverManager.getConnection(url);

            // GETTING CustomerID
            PreparedStatement statement = connect.prepareStatement("select ID from Customer where Name = (?)");
            statement.setString(1, custName);
            ResultSet rs = statement.executeQuery();
            String custID = rs.getString("ID");


            // GETTING ACCOUNTID FROM CustomerID
            statement = connect.prepareStatement("select ID, Name, Balance from Account where CustomerID = (?)");
            statement.setString(1, custID);
            ResultSet custrs = statement.executeQuery();
            String accID = custrs.getString("ID");
            String accName = custrs.getString("Name");
//            String accBalance = custrs.getString("Balance");

            // GETTING TRANSACTIONS FROM AccountID
            statement = connect.prepareStatement("select * from Trans where AccountID = (?)");
            statement.setString(1, accID);
            ResultSet transrs = statement.executeQuery();
//            String transFullBalance = transrs.getString("Balance"); // make it to add total of accs


            // JAVA FILE
            File bankStatement = new File(filePath + custName + ".txt");
            writer = new BufferedWriter(new FileWriter(bankStatement));
            writer.write("MCC Code School Bank Statement\n");
            writer.write("\nStatement for " + custName);
            writer.write("Statement Date: " + currDate + "\n");
            writer.write("\n" + accName + "\n");
            writer.write("Transactions:\n");
            writer.write(custrs.getString("Balance")); // needs to be from ACCOUNT
            while (transrs.next()) {
                writer.write(transrs.getString("TransTime"));
                writer.write(transrs.getString("Type"));
                writer.write(transrs.getString("Amount"));
                writer.write(transrs.getString("MerchantName"));
            }

        } catch (IOException e) {
            System.out.println("error 1");
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
    }
}
