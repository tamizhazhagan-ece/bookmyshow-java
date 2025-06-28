package BookMyShow;

import java.sql.*;
import java.util.Scanner;

public class User {

    private Connection connection;

    public User(Connection connection) {
        this.connection = connection;
    }

    /**
     * Displays the user options (view movies, theatres, book tickets).
     */
    public void userOptions() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n========= User Menu =========");
            System.out.println("1. View Movies");
            System.out.println("2. View Theatres");
            System.out.println("3. Book Ticket");
            System.out.println("4. Exit");
            System.out.println("=============================");
            System.out.print("Enter your choice: ");
            choice = getValidChoice(scanner);

            switch (choice) {
                case 1:
                    ShowMovies showMovies = new ShowMovies(connection);
                    showMovies.showMoviesList();
                    break;
                case 2:
                    ShowTheater showTheater = new ShowTheater(connection);
                    showTheater.showTheatresList();
                    break;
                case 3:
                    TicketBooking ticketBooking = new TicketBooking(connection);
                    ticketBooking.bookTicket(choice);
                    break;
                    
                
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    /**
     * Method to register a new user.
     */
    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();  // Storing plain text password

        // SQL query to insert the user into the database
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);  // Storing plain text password
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("User registered successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to validate the user login (plain text password comparison).
     * @return boolean indicating if login was successful
     */
    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();  // Plain text password comparison

        // Fetch the stored password from the database
        String query = "SELECT password FROM users WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Compare the entered password with the stored password
                String storedPassword = rs.getString("password");
                if (password.equals(storedPassword)) {
                    System.out.println("Login successful!");
                    return true;
                } else {
                    System.out.println("Invalid credentials.");
                    return false;
                }
            } else {
                System.out.println("User not found.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Validates the choice input from the user.
     * @param scanner Scanner object to read user input
     * @return Valid choice input as an integer
     */
    private static int getValidChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input, please enter a number.");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextInt();
    }
}
