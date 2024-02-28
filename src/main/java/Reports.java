import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Reports {
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


            // GETTING ACCOUNTID FROM CustomerID
            PreparedStatement accountID = connect.prepareStatement("select ID, Name, Balance from Account where CustomerID = (?)");
            accountID.setString(1, custID);
            ResultSet accRS = accountID.executeQuery();

            //GETTING BALANCE BASED OF CUST ID FROM ACCOUNTS
            PreparedStatement fullBalance = connect.prepareStatement("select Balance from Account where CustomerID = (?)");
            fullBalance.setString(1, custID);
            // GETTING FULL BALANCE OF ALL ACCOUNTS
            ResultSet rsFullBalance = fullBalance.executeQuery();
            List<Double> custBalance = new ArrayList<>();
            while (rsFullBalance.next()) {
                String totalBalance = rsFullBalance.getString("Balance");
                custBalance.add(Double.valueOf(totalBalance));
            }
            Double sum = 0.0;
            for (Double cust : custBalance) {
                sum += cust;
            }


            // WRITING JAVA FILE FOR STATEMENT
            File bankStatement = new File(filePath + "\\" + custNameTrim + ".txt");
            writer = new BufferedWriter(new FileWriter(bankStatement));
            writer.write("MCC Code School Bank Statement\n");
            writer.write("\nStatement for " + custName + "\n");
            writer.write("Statement Date: " + currentDate + "\n");
            writer.write("Current Total Balance: $" + sum + "\n");

            while (accRS.next()) {
                String accID = accRS.getString("ID");
                String accName = accRS.getString("Name");
                String accBalance = accRS.getString("Balance");


                // GETTING TRANSACTIONS FROM AccountID
                PreparedStatement transStatement = connect.prepareStatement("select * from Trans where AccountID = (?)");
                transStatement.setString(1, accID);
                ResultSet transrs = transStatement.executeQuery();

                writer.write("\n" + accName);
                writer.write(" - $" + accBalance);
                writer.write(" Balance\n");
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

    public static void accountingReport(Scanner scanner) {
        System.out.print("Please enter a Valid File Path to Create the File: ");
        String filePath = scanner.nextLine();
        if (filePath.isEmpty() || !Files.exists(Paths.get(filePath))) {
            System.out.print("Invalid Directory, Try Again: ");
            filePath = scanner.nextLine();
        }
        System.out.println("\nGenerated Accounting Report");

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        File bankStatement = new File(filePath + "\\accountingReport.txt");
        BufferedWriter writer = null;
        String url = "jdbc:sqlite:C:/Users/bta96367/QTR2/bank_ledger_project/BankingLedger.db";
        Connection connect = null;
        try {
            //API CONNECTION
            // CUSTOMER COUNT
            connect = DriverManager.getConnection(url);
            PreparedStatement custCount = connect.prepareStatement("select count(*) from Customer");
            ResultSet rs = custCount.executeQuery();
            String totalCustomers = rs.getString("count(*)");

            // ACCOUNT COUNT
            PreparedStatement accountCount = connect.prepareStatement("select count(*) from Account");
            ResultSet resultSet = accountCount.executeQuery();
            String totalAccounts = resultSet.getString("count(*)");


            PreparedStatement getBankBalance = connect.prepareStatement("select Balance from Account");
            ResultSet rsBalance = getBankBalance.executeQuery();
            List<Double> balances = new ArrayList<>();
            while (rsBalance.next()) {
                String bankBalance = rsBalance.getString("Balance");
                balances.add(Double.valueOf(bankBalance));
            }
            Double sum = 0.0;
            for (Double balance : balances) {
                sum += balance;
            }

            Map<String, String> customerIDs = new HashMap<>();
            PreparedStatement getCustomerNames = connect.prepareStatement("select Name, ID from Customer");
            ResultSet rsCustomer = getCustomerNames.executeQuery();

            String names = null;
            while (rsCustomer.next()) {
                names = rsCustomer.getString("Name");
                String id = rsCustomer.getString("ID");
                customerIDs.put(id, names);
            }

            PreparedStatement getCustomerID = connect.prepareStatement("select ID from Customer where Name = (?)");
            getCustomerID.setString(1, names);
            rsCustomer = getCustomerID.executeQuery();
            String id = rsCustomer.getString("ID");




            //Writing Accounting report file
            writer = new BufferedWriter(new FileWriter(bankStatement));
            writer.write("MCC Code School Bank Accounting Report\n");
            writer.write("\nReport Date: " + currentDate + "\n");
            writer.write("\nTotal Customers: " + totalCustomers + "\n");
            writer.write("Total Accounts: " + totalAccounts + "\n");
            writer.write("Total Balance: $" + sum + "\n");


            for (String customerID : customerIDs.keySet()) {
                writer.write("\n" + customerIDs.get(customerID) + ":");
                PreparedStatement getCustomerAccountNames = connect.prepareStatement("select Name, Balance from Account where CustomerID = (?)");
                getCustomerAccountNames.setString(1, customerID);
                rsCustomer = getCustomerAccountNames.executeQuery();
                while (rsCustomer.next()) {
                    String accNames = rsCustomer.getString("Name");
                    String accBalances = rsCustomer.getString("Balance");
                    writer.write("\n     " + accNames + " - " + "$" + accBalances);
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL error");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("file error");
        } finally {
            try {
                if (connect != null && writer != null) {
                    connect.close();
                    writer.close();
                }
            } catch (SQLException e) {
                System.out.println("closing error");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
