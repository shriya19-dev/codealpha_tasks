
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.io.*;

class Room {
    private int roomNumber;
    private String category;
    private double price;

    public Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public void displayRoom() {
        System.out.printf("%-10d %-15s Rs. %.2f per night%n",
                roomNumber, category, price);
    }
}

class Reservation {
    private int bookingId;
    private String guestName;
    private int roomNumber;
    private String category;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalAmount;
    private String status;

    public Reservation(int bookingId, String guestName, int roomNumber,
                       String category, LocalDate checkIn,
                       LocalDate checkOut, double totalAmount,
                       String status) {

        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public String getStatus() {
        return status;
    }

    public void cancel() {
        status = "CANCELLED";
    }

    public void displayDetails() {
        System.out.println("\n----------- BOOKING DETAILS -----------");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Guest Name: " + guestName);
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Room Category: " + category);
        System.out.println("Check-in: " + checkIn);
        System.out.println("Check-out: " + checkOut);
        System.out.printf("Total Amount: Rs. %.2f%n", totalAmount);
        System.out.println("Payment/Booking Status: " + status);
        System.out.println("---------------------------------------");
    }

    public String toFileString() {
        return bookingId + "|" + guestName + "|" + roomNumber + "|"
                + category + "|" + checkIn + "|" + checkOut + "|"
                + totalAmount + "|" + status;
    }
}

public class HotelReservationSystem {

    private static Scanner sc = new Scanner(System.in);

    private static ArrayList<Room> rooms = new ArrayList<>();
    private static ArrayList<Reservation> reservations = new ArrayList<>();

    private static final String FILE_NAME = "hotel_bookings.txt";

    private static int nextBookingId = 1001;

    public static void main(String[] args) {

        initializeRooms();
        loadReservations();

        int choice;

        System.out.println("========================================");
        System.out.println("       HOTEL RESERVATION SYSTEM");
        System.out.println("========================================");

        do {
            System.out.println("\n1. View All Rooms");
            System.out.println("2. Search Available Rooms");
            System.out.println("3. Book a Room");
            System.out.println("4. View Booking Details");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Exit");

            System.out.print("\nEnter your choice: ");
            choice = readInt();

            switch (choice) {
                case 1:
                    displayRooms();
                    break;

                case 2:
                    searchRooms();
                    break;

                case 3:
                    bookRoom();
                    break;

                case 4:
                    viewBooking();
                    break;

                case 5:
                    cancelBooking();
                    break;

                case 6:
                    saveReservations();
                    System.out.println("Thank you for using our hotel system!");
                    break;

                default:
                    System.out.println("Invalid choice! Select 1-6.");
            }

        } while (choice != 6);

        sc.close();
    }

    private static void initializeRooms() {

        rooms.add(new Room(101, "Standard", 1500));
        rooms.add(new Room(102, "Standard", 1500));
        rooms.add(new Room(103, "Standard", 1500));

        rooms.add(new Room(201, "Deluxe", 2500));
        rooms.add(new Room(202, "Deluxe", 2500));
        rooms.add(new Room(203, "Deluxe", 2500));

        rooms.add(new Room(301, "Suite", 4500));
        rooms.add(new Room(302, "Suite", 4500));
    }

    private static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input! Enter a whole number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    private static LocalDate readDate(String message) {

        while (true) {
            System.out.print(message);
            String input = sc.next();

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Use YYYY-MM-DD format.");
            }
        }
    }

    private static void displayRooms() {

        System.out.println("\n------------- HOTEL ROOMS -------------");
        System.out.printf("%-10s %-15s %s%n",
                "Room No.", "Category", "Price");

        for (Room room : rooms) {
            room.displayRoom();
        }
    }

    private static boolean isAvailable(Room room,
                                       LocalDate checkIn,
                                       LocalDate checkOut) {

        for (Reservation reservation : reservations) {

            if (reservation.getRoomNumber() == room.getRoomNumber()
                    && !reservation.getStatus().equals("CANCELLED")) {

                boolean overlap =
                        checkIn.isBefore(reservation.getCheckOut())
                                && checkOut.isAfter(reservation.getCheckIn());

                if (overlap) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void searchRooms() {

        LocalDate checkIn = readDate("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkOut = readDate("Enter check-out date (YYYY-MM-DD): ");

        if (!validDates(checkIn, checkOut)) {
            return;
        }

        System.out.println("\nAvailable Rooms:");

        boolean found = false;

        for (Room room : rooms) {

            if (isAvailable(room, checkIn, checkOut)) {
                room.displayRoom();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available for these dates.");
        }
    }

    private static boolean validDates(LocalDate checkIn,
                                      LocalDate checkOut) {

        if (checkIn.isBefore(LocalDate.now())) {
            System.out.println("Check-in date cannot be in the past.");
            return false;
        }

        if (!checkOut.isAfter(checkIn)) {
            System.out.println("Check-out must be after check-in.");
            return false;
        }

        return true;
    }

    private static void bookRoom() {

        LocalDate checkIn = readDate("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkOut = readDate("Enter check-out date (YYYY-MM-DD): ");

        if (!validDates(checkIn, checkOut)) {
            return;
        }

        searchAndBook(checkIn, checkOut);
    }

    private static void searchAndBook(LocalDate checkIn,
                                      LocalDate checkOut) {

        ArrayList<Room> availableRooms = new ArrayList<>();

        System.out.println("\nAvailable Rooms:");

        for (Room room : rooms) {

            if (isAvailable(room, checkIn, checkOut)) {
                room.displayRoom();
                availableRooms.add(room);
            }
        }

        if (availableRooms.isEmpty()) {
            System.out.println("Sorry! No rooms available.");
            return;
        }

        System.out.print("\nEnter room number to book: ");
        int roomNumber = readInt();

        Room selectedRoom = null;

        for (Room room : availableRooms) {
            if (room.getRoomNumber() == roomNumber) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Invalid room selection.");
            return;
        }

        sc.nextLine();

        System.out.print("Enter guest name: ");
        String guestName = sc.nextLine().trim();

        if (guestName.isEmpty() || guestName.contains("|")) {
            System.out.println("Invalid guest name.");
            return;
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        double totalAmount = nights * selectedRoom.getPrice();

        System.out.println("\n----------- PAYMENT DETAILS -----------");
        System.out.println("Guest: " + guestName);
        System.out.println("Room: " + selectedRoom.getRoomNumber());
        System.out.println("Number of nights: " + nights);
        System.out.printf("Total payment: Rs. %.2f%n", totalAmount);

        System.out.print("Confirm simulated payment? (yes/no): ");
        String payment = sc.next();

        if (!payment.equalsIgnoreCase("yes")) {
            System.out.println("Payment cancelled. Booking not created.");
            return;
        }

        Reservation reservation = new Reservation(
                nextBookingId,
                guestName,
                selectedRoom.getRoomNumber(),
                selectedRoom.getCategory(),
                checkIn,
                checkOut,
                totalAmount,
                "CONFIRMED - PAID"
        );

        reservations.add(reservation);
        nextBookingId++;

        saveReservations();

        System.out.println("\nBooking successful!");
        reservation.displayDetails();
    }

    private static void viewBooking() {

        System.out.print("Enter Booking ID: ");
        int id = readInt();

        Reservation reservation = findReservation(id);

        if (reservation == null) {
            System.out.println("Booking not found.");
        } else {
            reservation.displayDetails();
        }
    }

    private static void cancelBooking() {

        System.out.print("Enter Booking ID to cancel: ");
        int id = readInt();

        Reservation reservation = findReservation(id);

        if (reservation == null) {
            System.out.println("Booking not found.");
            return;
        }

        if (reservation.getStatus().equals("CANCELLED")) {
            System.out.println("This booking is already cancelled.");
            return;
        }

        reservation.cancel();
        saveReservations();

        System.out.println("Reservation cancelled successfully.");
    }

    private static Reservation findReservation(int id) {

        for (Reservation reservation : reservations) {

            if (reservation.getBookingId() == id) {
                return reservation;
            }
        }

        return null;
    }

    private static void saveReservations() {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Reservation reservation : reservations) {
                writer.write(reservation.toFileString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving booking records: " + e.getMessage());
        }
    }

    private static void loadReservations() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length != 8) {
                    System.out.println("Skipping invalid booking record.");
                    continue;
                }

                try {
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    int roomNumber = Integer.parseInt(data[2]);
                    String category = data[3];
                    LocalDate checkIn = LocalDate.parse(data[4]);
                    LocalDate checkOut = LocalDate.parse(data[5]);
                    double amount = Double.parseDouble(data[6]);
                    String status = data[7];

                    reservations.add(new Reservation(
                            id, name, roomNumber, category,
                            checkIn, checkOut, amount, status
                    ));

                    nextBookingId = Math.max(nextBookingId, id + 1);

                } catch (RuntimeException e) {
                    System.out.println("Skipping invalid booking record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
    }
}
