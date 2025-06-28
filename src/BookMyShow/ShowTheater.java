package BookMyShow;

import java.sql.*;
import java.util.Scanner;

/**
 * ShowTheater class to display available theaters.
 */
public class ShowTheater {

    private Connection connection;

    /**
     * Constructor to initialize the connection.
     * @param connection Database connection
     */
    public ShowTheater(Connection connection) {
        this.connection = connection;
    }

    /**
     * Displays the list of available theatres from the database.
     */
    public void showTheatresList() {
        String query = "SELECT * FROM theatres";
        
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("========== Theatres ==========");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") +
                        ", Location: " + rs.getString("location") + ", Capacity: " + rs.getInt("capacity"));
            }
            System.out.println("===============================");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Selects a theatre based on user input for a specific movie.
     * @param movieId The ID of the movie to show theatres for
     * @return Theatre ID selected by the user
     */
    public int selectTheaterForMovie(int movieId) {
        Scanner scanner = new Scanner(System.in);

        String query = "SELECT t.id, t.name, t.location, t.capacity FROM theatres t " +
                       "JOIN show_timings st ON t.id = st.theatre_id " +
                       "WHERE st.movie_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n========= Available Theatres ==========");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") +
                        ", Location: " + rs.getString("location") + ", Capacity: " + rs.getInt("capacity"));
            }
            System.out.println("======================================");

            System.out.print("Enter Theatre ID to select: ");
            int theatreId = scanner.nextInt();
            return theatreId;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;  // Default return value if no theatre is selected
    }
}
