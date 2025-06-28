package BookMyShow;

import java.sql.*;

public class ShowMovies {

    private Connection connection;

    public ShowMovies(Connection connection) {
        this.connection = connection;
    }

    public void showMoviesList() {
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
}
