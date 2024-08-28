package UI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.Scanner;
import java.util.regex.Pattern;

public class MainMenu {
    private final static HotelResource hotelResource = HotelResource.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    public void displayMainMenu() throws ParseException {
        int choice = 0;

        do {
            System.out.println("\n***** MAIN MENU *****");
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");

            System.out.print("Please select an option: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    findAndReserveRoom();
                    break;
                case 2:
                    getCustomersReservations();
                    break;
                case 3:
                    createCustomerAccount();
                    break;
                case 4:
                    AdminMenu.getInstance().displayMenu();
                    break;
                case 5:
                    System.out.println("Hotel Reservation");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        } while (choice != 5);
    }

    private void findAndReserveRoom() throws ParseException {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter check-in date yyyy/MM/dd: ");
            Date checkIn = new SimpleDateFormat("yyyy/MM/dd").parse(scanner.nextLine());
            if (checkIn.before(new Date())) {
                System.out.println("Check-in date must be in the future. Please try again!");
                displayMainMenu();
            }
            System.out.println("Enter check-out date yyyy/MM/dd: ");
            Date checkOut = new SimpleDateFormat("yyyy/MM/dd").parse(scanner.nextLine());

            if (checkOut != null) {
                if (checkOut.before(checkIn)) {
                    System.out.println("Check-out date must be later than check-in date. Please try again.");
                    displayMainMenu();
                }
                Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

                if (!availableRooms.isEmpty()) {
                    reserveRoom(scanner, checkIn, checkOut, availableRooms);
                } else {
                    Date newCheckInDate = addPlusDays(checkIn);
                    Date newCheckOutDate = addPlusDays(checkOut);
                    Collection<IRoom> recommendedRooms = hotelResource.findRecommendRooms(newCheckInDate, newCheckOutDate);
                    if (!recommendedRooms.isEmpty()) {
                        DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
                        String formattedCheckInDate = dateFormatter.format(newCheckInDate);
                        String formattedCheckOutDate = dateFormatter.format(newCheckOutDate);

                        System.out.println("We recommend available rooms for alternative dates: \n  Check-in date: " + formattedCheckInDate + "\n  Check-out date: " + formattedCheckOutDate);
                        reserveRoom(scanner, newCheckInDate, newCheckOutDate, recommendedRooms);
                    } else {
                        System.out.println("No available rooms found.");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Invalid date format. Please try again.");
        } finally {
            displayMainMenu();
        }
    }

    private static Date addPlusDays(Date checkInDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }

    private void reserveRoom(Scanner scanner, Date checkIn, Date checkOut, Collection<IRoom> rooms) throws ParseException {
        System.out.println("Do you want to proceed with booking? Type Y for Yes, N for No: Y/N");
        final String confirmation = scanner.nextLine();

        if ("y".equalsIgnoreCase(confirmation)) {
            System.out.println("Please enter your email:");
            String email = scanner.nextLine();

            Customer customer = hotelResource.getCustomer(email);
            if (customer == null) {
                System.out.println("No account found with this email. Would you like to create an account? Y/N");
                if ("y".equalsIgnoreCase(scanner.nextLine())) {
                    createCustomerAccount();
                } else {
                    displayMainMenu();
                }
            } else {
                System.out.println("\n***** AVAILABLE ROOMS *****");
                for (IRoom room : rooms) {
                    System.out.println(room);
                }
                System.out.println("Enter the room number to reserve:");
                String roomNumber = scanner.nextLine();

                if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
                    final IRoom room = hotelResource.getRoom(roomNumber);

                    Reservation reservation = hotelResource.bookARoom(email, room, checkIn, checkOut);
                    System.out.println("Room booked successfully!");
                    System.out.println(reservation);
                } else {
                    System.out.println("Room number does not exist. Please try again.");
                    displayMainMenu();
                }
            }
        } else {
            displayMainMenu();
        }
    }

    private void getCustomersReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }
    }

    private void createCustomerAccount() {
        System.out.println("Enter your first name:");
        String firstName = scanner.nextLine();

        System.out.println("Enter your last name:");
        String lastName = scanner.nextLine();

        System.out.println("Enter your email (e.g., abc@domain.com):");
        String email = scanner.nextLine().trim();
        if (!checkEmail(email)) {
            System.out.println("Invalid email format. Please try again.");
            createCustomerAccount();
            return;
        }
        try {
            Customer customer = hotelResource.getCustomer(email.trim());
            if (customer != null) {
                System.out.println("Account already exists.");
                displayMainMenu();
            } else {
                hotelResource.createACustomer(firstName, lastName, email);
                System.out.println("Account successfully created.");
                displayMainMenu();
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Unable to create account. Please try again.");
            createCustomerAccount();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkEmail(String emailAddress) {
        String emailPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(emailPattern)
                .matcher(emailAddress)
                .matches();
    }
}
