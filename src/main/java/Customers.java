import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class Customers {
    public static void createCust(Scanner scanner) {
        System.out.print("Please Enter Customer Name: ");
        String custID = String.valueOf(UUID.randomUUID());
        String custName = scanner.nextLine();
        System.out.print("Please Enter Customer DOB: ");
        String dob = scanner.nextLine();
        System.out.print("Please Enter Customer Phone Number: ");
        String phoneNum = scanner.nextLine();
        System.out.print("Please Enter Customer Address: ");
        String custAddress = scanner.nextLine();
        System.out.print("Please Enter Customer City: ");
        String custCity = scanner.nextLine();
        System.out.print("Please Enter Customer State: ");
        String custState = scanner.nextLine();
        System.out.print("Please Enter Customer Zip: ");
        int zipCode = scanner.nextInt();
        scanner.nextLine();
        String currDate = LocalDateTime.now().toString();
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";

        Connection connect = null;
            try {
                connect = DriverManager.getConnection(url);
                PreparedStatement statement = connect.prepareStatement(
                "insert into Customer (ID, Name, DOB, PhoneNumber, StreetAddress, City, State, ZipCode, CreatedDate) values ((?),(?),(?), (?), (?), (?), (?), (?), (?));");
                statement.setString(1, custID);
                statement.setString(2, custName);
                statement.setString(3, dob);
                statement.setString(4, phoneNum);
                statement.setString(5, custAddress);
                statement.setString(6, custCity);
                statement.setString(7, custState);
                statement.setInt(8, zipCode);
                statement.setString(9, currDate);
                statement.execute();
            } catch (SQLException e) {
                System.out.println("SQL error");
            }
            finally {
                if (connect != null) {
                    try {
                        connect.close();
                    } catch (SQLException e) {
                        System.out.println("close error");
                    }
                }
            }
        System.out.println("\nCustomer Created with ID " + custID);
    }
}
