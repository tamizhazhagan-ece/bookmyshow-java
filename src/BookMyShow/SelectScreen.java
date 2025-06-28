package BookMyShow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SelectScreen {
    private Connection connection;

    public SelectScreen(Connection connection) {
        this.connection = connection;
    }

    public int selectScreenForTheater(int theaterId) {
        String query = "SELECT id, screen_number FROM screens WHERE theater_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, theaterId);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Available Screens for the selected Theater:");
            while (rs.next()) {
                System.out.println("Screen ID: " + rs.getInt("id") + ", Screen Number: " + rs.getInt("screen_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Screen ID to select: ");
        return scanner.nextInt();
    }
}
