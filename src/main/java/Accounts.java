import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Accounts {
    Scanner scanner = new Scanner(System.in);

    public static void opts(Scanner scanner){
        System.out.println("Would you like to:\nC - Create an account\nU - Update an account\nD - Delete an account");
        String answer = scanner.nextLine();
        while (!answer.equalsIgnoreCase("c") || !answer.equalsIgnoreCase("u") || !answer.equalsIgnoreCase("d")) {
            System.out.println("Invalid Selection, going back to main menu.");
            Ledger.printMenu();
        }
        if (answer.equalsIgnoreCase("c")) {
            //run method
        } else if (answer.equalsIgnoreCase("u")) {
            // run method
        } else {
            //run method
        }
    }
    public static void createAccount(Scanner scanner){
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        try {
            Connection connect = DriverManager.getConnection(url);
            PreparedStatement statement = connect.prepareStatement("select ID from Customer where Name = (?)");
            String name = scanner.nextLine();
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            int accountID = rs.getInt("ID");
            statement = connect.prepareStatement("insert into Account(ID) values((?))");
            statement.setInt(1, accountID);
            statement.execute();

            System.out.println("Please Enter Account Name: ");
            String accName = scanner.nextLine();
            statement = connect.prepareStatement("insert into Customer (Name) values ((?))");
            statement.setString(1, accName);
            statement.execute();

            Random random = new Random();
//            random.nextInt()   how to do??? ask Evan


        } catch (SQLException e) {
            System.out.println("ERROR");
        }
        finally {
            //close
        }
    }
}
