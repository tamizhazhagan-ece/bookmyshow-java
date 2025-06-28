package BookMyShow;

import java.sql.*;
import java.util.Scanner;

public class Admin {

    private Connection connection;

    public Admin(Connection connection) {
        this.connection = connection;
    }

    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        String query = "SELECT password FROM users WHERE username = ? AND role = 'admin'";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (password.equals(rs.getString("password"))) {
                    System.out.println("Admin login successful.");
                    return true;
                } else {
                    System.out.println("Invalid password.");
                }
            } else {
                System.out.println("Admin not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void showAdminOptions() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n========= Admin Menu =========");
            System.out.println("1. Add Movie");
            System.out.println("2. Add Theatre");
            System.out.println("3. Add Screen");
            System.out.println("4. Add Show Timing");
            System.out.println("5. View Movies");
            System.out.println("6. View Theatres");
            System.out.println("7. View Show Timings");
            System.out.println("8. View Payments");
            System.out.println("9. Logout");
            System.out.println("===============================");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    addTheatre();
                    break;
                case 3:
                    addScreen();
                    break;
                case 4:
                    addShowTiming();
                    break;
                case 5:
                    viewMovies();
                    break;
                case 6:
                    viewTheatres();
                    break;
                case 7:
                    viewShowTimings();
                    break;
                case 8:
                    viewPayments();
                    break;
                case 9:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);
    }

    public void addMovie() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Movie Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Language: ");
        String language = scanner.nextLine();
        System.out.print("Enter Duration (minutes): ");
        int duration = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        String query = "INSERT INTO movies (name, language, duration, genre) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, language);
            stmt.setInt(3, duration);
            stmt.setString(4, genre);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Movie added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTheatre() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Theatre Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter Capacity: ");
        int capacity = scanner.nextInt();

        String query = "INSERT INTO theatres (name, location, capacity) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setInt(3, capacity);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Theatre added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addScreen() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Theatre ID: ");
        int theatreId = scanner.nextInt();
        System.out.print("Enter Screen Number: ");
        int screenNumber = scanner.nextInt();

        String query = "INSERT INTO screens (theatre_id, screen_number) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, theatreId);
            stmt.setInt(2, screenNumber);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Screen added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addShowTiming() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Movie ID: ");
        int movieId = scanner.nextInt();
        System.out.print("Enter Theatre ID: ");
        int theatreId = scanner.nextInt();
        System.out.print("Enter Screen ID: ");
        int screenId = scanner.nextInt();
        System.out.print("Enter Show Time (HH:MM:SS): ");
        String showTime = scanner.next();

        String query = "INSERT INTO show_timings (movie_id, theatre_id, screen_id, show_time) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.setInt(2, theatreId);
            stmt.setInt(3, screenId);
            stmt.setString(4, showTime);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Show timing added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewMovies() {
        String query = "SELECT * FROM movies";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Movies:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") +
                        ", Language: " + rs.getString("language") + ", Duration: " + rs.getInt("duration") +
                        ", Genre: " + rs.getString("genre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewTheatres() {
        String query = "SELECT * FROM theatres";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Theatres:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") +
                        ", Location: " + rs.getString("location") + ", Capacity: " + rs.getInt("capacity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewShowTimings() {
        String query = "SELECT st.id, m.name AS movie_name, th.name AS theatre_name, s.screen_number, st.show_time " +
                       "FROM show_timings st " +
                       "JOIN movies m ON st.movie_id = m.id " +
                       "JOIN theatres th ON st.theatre_id = th.id " +
                       "JOIN screens s ON st.screen_id = s.id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Show Timings:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Movie: " + rs.getString("movie_name") +
                        ", Theatre: " + rs.getString("theatre_name") + ", Screen: " + rs.getInt("screen_number") +
                        ", Time: " + rs.getString("show_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPayments() {
        String query = "SELECT * FROM payments";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n========= Payments =========");
            while (rs.next()) {
                System.out.println("Payment ID: " + rs.getInt("id") + 
                        ", Amount: " + rs.getDouble("amount") + ", Date: " + rs.getDate("payment_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
