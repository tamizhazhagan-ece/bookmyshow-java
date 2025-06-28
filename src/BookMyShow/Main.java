package BookMyShow;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Database connection
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookmyshow", "root", "Tamil@2000");
            System.out.println("Database connected.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Main menu options
        System.out.println("=====================================");
        System.out.println("         Welcome to BookMyShow!       ");
        System.out.println("=====================================");
        System.out.println("1. Admin Login");
        System.out.println("2. User Login");
        System.out.println("3. Register");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
        
        int choice = getValidChoice(scanner);

        switch (choice) {
            case 1:
                Admin admin = new Admin(connection);
                if (admin.login()) {
                    admin.showAdminOptions();
                }
                break;
            case 2:
                User user = new User(connection);
                if (user.login()) {
                    user.userOptions();
                }
                break;
            case 3:
                User newUser = new User(connection);
                newUser.register();  // User Registration
                break;
            case 4:
                System.out.println("Exiting application...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Exiting application...");
                System.exit(1);
        }
    }

    /**
     * Validates the choice input from the user.
     * @param scanner Scanner object to read user input
     * @return Valid choice input as an integer
     */
    private static int getValidChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input, please enter a valid number.");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextInt();
    }
}
