package BookMyShow;

import java.sql.*;
import java.util.Scanner;

public class TicketBooking {

    private Connection connection;

    public TicketBooking(Connection connection) {
        this.connection = connection;
    }

    /**
     * Method to book a ticket and display the booking details
     */
    public void bookTicket(int userId) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Movie ID: ");
        int movieId = scanner.nextInt();
        System.out.print("Enter Theatre ID: ");
        int theatreId = scanner.nextInt();

        displayShowTimings(movieId, theatreId);

        System.out.print("Enter Show Timing ID: ");
        int showTimingId = scanner.nextInt();
        System.out.print("Enter Number of Tickets: ");
        int numTickets = scanner.nextInt();

        double pricePerTicket = getTicketPrice(movieId);
        double gstAmount = pricePerTicket * numTickets * 0.18;
        double totalPrice = (pricePerTicket * numTickets) + gstAmount;

        String movieName = getMovieName(movieId);
        String theatreName = getTheatreName(theatreId);
        String showTime = getShowTime(showTimingId);
        int screenNumber = getScreenNumber(showTimingId);

        System.out.println("=====================================");
        System.out.println("\n********* BOOKING DETAILS **********");
        System.out.println("=====================================");
        System.out.println("Movie Name: " + movieName);
        System.out.println("Theatre Name: " + theatreName);
        System.out.println("Screen Number: " + screenNumber);
        System.out.println("Show Time: " + showTime);
        System.out.println("Number of Tickets: " + numTickets);
        System.out.println("Price per Ticket: Rs. " + pricePerTicket);
        System.out.println("GST (18%): Rs. " + gstAmount);
        System.out.println("=====================================");
        System.out.println("Total Price: Rs. " + totalPrice);
        System.out.println("=====================================");

        System.out.println("\n========== Payment Options ==========");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit Card");
        System.out.println("3. PayPal");
        System.out.println("4. UPI");
        System.out.print("Select Payment Method (1/2/3/4): ");
        int paymentMethod = getValidPaymentMethod(scanner);

        boolean paymentStatus = processPayment(paymentMethod, totalPrice); // Process Payment

        if (paymentStatus) {
            System.out.println("Payment Successful!");

            // Insert Ticket Booking into the tickets table
            int ticketId = insertTicketBooking(userId, movieId, theatreId, showTimingId, numTickets, pricePerTicket, gstAmount, totalPrice, "Paid");

            // Insert Payment into the payments table using the generated ticketId
            insertPaymentDetails(ticketId, paymentMethod, totalPrice);

            // Update the Theatre Capacity
            updateTheatreCapacity(theatreId, numTickets);

            // Print the Ticket
            printTicket(ticketId, movieName, theatreName, showTime, numTickets, pricePerTicket, gstAmount, totalPrice);
        } else {
            System.out.println("Payment Failed. Please try again.");
        }
    }

    private int getValidPaymentMethod(Scanner scanner) {
        while (true) {
            int method = scanner.nextInt();
            if (method >= 1 && method <= 4) {
                return method;
            }
            System.out.print("Invalid option. Select Payment Method (1/2/3/4): ");
        }
    }

    private boolean processPayment(int paymentMethod, double totalPrice) {
        String paymentMethodString;
        switch (paymentMethod) {
            case 1 -> paymentMethodString = "Credit Card";
            case 2 -> paymentMethodString = "Debit Card";
            case 3 -> paymentMethodString = "PayPal";
            case 4 -> paymentMethodString = "UPI";
            default -> {
                System.out.println("Invalid payment method.");
                return false;
            }
        }

        // Simulating the payment process
        System.out.println("Processing " + paymentMethodString + " payment...");

        // Assuming the payment is successful
        return true;
    }

    private int insertTicketBooking(int userId, int movieId, int theatreId, int showTimingId, int numTickets,
                                     double pricePerTicket, double gstAmount, double totalPrice, String paymentStatus) {
        String query = "INSERT INTO tickets (user_id, movie_id, theatre_id, show_time_id, num_tickets, price_per_ticket, gst_amount, total_price, payment_status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);
            stmt.setInt(3, theatreId);
            stmt.setInt(4, showTimingId);
            stmt.setInt(5, numTickets);
            stmt.setDouble(6, pricePerTicket);
            stmt.setDouble(7, gstAmount);
            stmt.setDouble(8, totalPrice);
            stmt.setString(9, paymentStatus);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);  // Returning the generated ticket ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void insertPaymentDetails(int ticketId, int paymentMethod, double totalPrice) {
        String paymentMethodString;
        String transactionId = generateTransactionId(); // Simulated transaction ID generation

        switch (paymentMethod) {
            case 1 -> paymentMethodString = "Credit Card";
            case 2 -> paymentMethodString = "Debit Card";
            case 3 -> paymentMethodString = "PayPal";
            case 4 -> paymentMethodString = "UPI";
            default -> paymentMethodString = "Unknown";
        }

        String query = "INSERT INTO payments (ticket_id, payment_method, transaction_id, amount, payment_status, payment_date) " +
                       "VALUES (?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ticketId);
            stmt.setString(2, paymentMethodString);
            stmt.setString(3, transactionId);
            stmt.setDouble(4, totalPrice);
            stmt.setString(5, "Success");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTheatreCapacity(int theatreId, int numTickets) {
        String query = "UPDATE theatres SET capacity = capacity - ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, numTickets);
            stmt.setInt(2, theatreId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printTicket(int ticketId, String movieName, String theatreName, String showTime, int numTickets,
                              double pricePerTicket, double gstAmount, double totalPrice) {
        System.out.println("=====================================");
        System.out.println("\n********** TICKET DETAILS **********");
        System.out.println("=====================================");
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("Movie Name: " + movieName);
        System.out.println("Theatre Name: " + theatreName);
        System.out.println("Show Time: " + showTime);
        System.out.println("Number of Tickets: " + numTickets);
        System.out.println("Price per Ticket: Rs. " + pricePerTicket);
        System.out.println("GST (18%): Rs. " + gstAmount);
        System.out.println("=====================================");
        System.out.println("Total Price: Rs. " + totalPrice);
        System.out.println("=====================================");
    }

    private void displayShowTimings(int movieId, int theatreId) {
        String query = "SELECT st.id, st.show_time, s.screen_number FROM show_timings st " +
                       "JOIN screens s ON st.screen_id = s.id WHERE st.movie_id = ? AND st.theatre_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.setInt(2, theatreId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nAvailable Show Timings:");
            while (rs.next()) {
                System.out.println("Show Time ID: " + rs.getInt("id") + " | Show Time: " + rs.getString("show_time") + " | Screen: " + rs.getInt("screen_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getMovieName(int movieId) {
        String query = "SELECT name FROM movies WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getTheatreName(int theatreId) {
        String query = "SELECT name FROM theatres WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, theatreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getShowTime(int showTimingId) {
        String query = "SELECT show_time FROM show_timings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, showTimingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("show_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int getScreenNumber(int showTimingId) {
        String query = "SELECT s.screen_number FROM show_timings st JOIN screens s ON st.screen_id = s.id WHERE st.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, showTimingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("screen_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private double getTicketPrice(int movieId) {
        String query = "SELECT price FROM movies WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private String generateTransactionId() {
        // Simple mock-up of transaction ID generation
        return "TXN" + System.currentTimeMillis();
    }
}
