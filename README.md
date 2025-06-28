# 🎟️ BookMyShow Ticket Booking System (Console-Based Java Project)

This is a **console-based movie ticket booking system** written in Java. It simulates key features of a platform like BookMyShow, allowing users to browse movies, select theaters and screens, and book tickets from the command line.

---

## 🚀 Features

* 👤 User and Admin login system
* 🎬 View available movies
* 🏛️ View available theaters and screens
* 🪑 Book seats for selected shows
* 📋 View booking history
* 🛠 Admin can manage movie listings and theaters
* 🗄️ MySQL database integration for persistent storage

---

## 🧰 Tech Stack

* **Language:** Java
* **IDE:** Any Java IDE (IntelliJ IDEA, Eclipse, NetBeans, etc.)
* **Database:** MySQL
* **Execution:** Command Line / Terminal

---

## 📁 Project Structure

tamizhazhagan/
├── src/
│   └── BookMyShow/
│       ├── Main.java
│       ├── Admin.java
│       ├── User.java
│       ├── ShowMovies.java
│       ├── ShowTheater.java
│       ├── SelectScreen.java
│       └── TicketBooking.java
├── database.sql
└── README.md

---

## 🧪 How to Run

### Prerequisites

* Java JDK 8 or above
* MySQL installed and running
* A terminal or Java-supported IDE

### Run via Terminal

```bash
cd tamizhazhagan/src
javac BookMyShow/Main.java
java BookMyShow.Main
```

### Run via IDE

1. Open the project in your IDE.
2. Navigate to `Main.java`.
3. Right-click and select **Run**.

---

## 🗄️ Database Setup (MySQL)

1. Make sure MySQL is installed and running.
2. Open your MySQL client (e.g., MySQL Workbench or terminal).
3. Run the provided SQL file:

```bash
mysql -u root -p < database.sql
```

4. Update your Java code with the correct database connection URL, username, and password.

---

## 📄 SQL Schema Preview

```sql
CREATE DATABASE IF NOT EXISTS BookMyShow;

-- Use the created database
USE BookMyShow;

-- Create 'users' table (Stores user information)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'user'
);

-- Insert sample users
INSERT INTO users (username, password, role) VALUES 
('admin', 'adminpassword', 'admin'),
('user1', 'user123', 'user'),
('user2', 'user123', 'user'),
('tamil','tamil@123','user');

-- Create 'movies' table (Stores movie information)
CREATE TABLE IF NOT EXISTS movies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    language VARCHAR(50),
    duration INT NOT NULL,
    genre VARCHAR(100),
    price DECIMAL(10, 2) NOT NULL DEFAULT 200.00
);

-- Insert sample movies
INSERT INTO movies (name, language, duration, genre, price) VALUES 
('Kaithi', 'Tamil', 150, 'Action', 250.00),
('Master', 'Tamil', 179, 'Action', 300.00),
('Vikram', 'Tamil', 168, 'Action', 350.00);

-- Create 'theatres' table (Stores theatre information)
CREATE TABLE IF NOT EXISTS theatres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    capacity INT NOT NULL
);

-- Insert sample theatres
INSERT INTO theatres (name, location, capacity) VALUES 
('ARRS Multiplex', 'Salem', 300),
('Rohini Theatres', 'Chennai', 350),
('Udhayam Theatres', 'Madurai', 250);

-- Create 'screens' table (Stores screen information in each theatre)
CREATE TABLE IF NOT EXISTS screens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    theatre_id INT NOT NULL,
    screen_number INT NOT NULL,
    FOREIGN KEY (theatre_id) REFERENCES theatres(id)
);

-- Insert sample screens
INSERT INTO screens (theatre_id, screen_number) VALUES 
(1, 1), (1, 2), 
(2, 1), (2, 2), 
(3, 1);

-- Create 'show_timings' table (Stores show timings for each movie in a theatre)
CREATE TABLE IF NOT EXISTS show_timings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT NOT NULL,
    theatre_id INT NOT NULL,
    screen_id INT NOT NULL,
    show_time TIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (theatre_id) REFERENCES theatres(id),
    FOREIGN KEY (screen_id) REFERENCES screens(id)
);

-- Insert sample show timings
INSERT INTO show_timings (movie_id, theatre_id, screen_id, show_time) VALUES 
(1, 1, 1, '10:00:00'),
(1, 1, 2, '01:00:00'),
(2, 2, 1, '11:30:00'),
(2, 2, 2, '02:00:00'),
(3, 3, 1, '05:00:00');

-- Create 'tickets' table (Stores ticket booking information)
CREATE TABLE IF NOT EXISTS tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    theatre_id INT NOT NULL,
    show_time_id INT NOT NULL,
    num_tickets INT NOT NULL,
    price_per_ticket DECIMAL(10, 2) NOT NULL,
    gst_amount DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (theatre_id) REFERENCES theatres(id),
    FOREIGN KEY (show_time_id) REFERENCES show_timings(id)
);

-- Insert sample ticket bookings into tickets table
INSERT INTO tickets (user_id, movie_id, theatre_id, show_time_id, num_tickets, price_per_ticket, gst_amount, total_price, payment_status)
VALUES 
(2, 1, 1, 1, 2, 250.00, 90.00, 590.00, 'Paid'),   -- ticket_id will be auto-generated
(3, 2, 2, 4, 3, 300.00, 162.00, 1062.00, 'Paid'),  -- ticket_id will be auto-generated
(1, 3, 3, 5, 1, 350.00, 63.00, 413.00, 'Paid');    -- ticket_id will be auto-generated

-- Create 'payments' table (Stores payment details for each booking)
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE
);

-- Insert sample payment data for each ticket
INSERT INTO payments (ticket_id, payment_method, transaction_id, amount, payment_status)
VALUES
(1, 'Credit Card', 'TXN1234567890', 500.00, 'Success'),
(2, 'Debit Card', 'TXN1234567891', 750.00, 'Success'),
(3, 'PayPal', 'TXN1234567892', 413.00, 'Success');

-- Check tickets table
SELECT * FROM tickets;

-- Check payments table
SELECT * FROM payments;
```

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

## 👤 Author

* Tamizhazhagan S ([GitHub Profile](https://github.com/tamizhazhagan-ece))
