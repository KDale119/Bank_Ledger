import java.util.Scanner;

public class Ledger {
    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Code School Bank of Awesomeness");
        System.out.println("----------------------------------------------");

        while (true) {
            printMenu();
            System.out.print("Selection: ");
            String input = scanner.nextLine();
            System.out.println();

            if (input.equalsIgnoreCase("x")) {
                break;
            } else if (input.equals("1")) {
                Reports.statement(scanner);
            } else if (input.equals("2")) {
                //method call here
            } else if (input.equals("3")) {
                Transactions.simulate(scanner);
            } else if (input.equals("4")) {
                Customers.createCust(scanner);
            } else if (input.equals("5")) {
                Accounts.opts(scanner);
            } else if (input.equals("6")) {
                Transactions.opts(scanner);
            }
        }
    }

    public static void printMenu(){
        System.out.println("\nSelect from the following:");
        System.out.println("1. Generate a Statement");
        System.out.println("2. Generate Accounting Reports");
        System.out.println("3. Simulate a Transaction for Account");
        System.out.println("4. Create a Customer");
        System.out.println("5. CRUD a Customer Account");
        System.out.println("6. Deposit/Withdrawal Funds");
        System.out.println("X. Exit");
    }
}
