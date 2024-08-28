package UI;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.Collections;
import java.util.Scanner;

public class AdminMenu {
    private final AdminResource adminResource = new AdminResource();
    private final Scanner scanner = new Scanner(System.in);
    private static AdminMenu instance = null;

    private AdminMenu() {
    }

    public static AdminMenu getInstance() {
        if (instance == null) {
            instance = new AdminMenu();
        }
        return instance;
    }

    public void displayMenu() {
        int choice = 0;

        do {
            System.out.println("\n***** ADMIN MENU *****");
            System.out.println("1. See all Customers");
            System.out.println("2. See all Rooms");
            System.out.println("3. See all Reservations");
            System.out.println("4. Add a Room");
            System.out.println("5. Back to Main Menu");

            System.out.print("Please select an option: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    displayAllCustomers();
                    break;
                case 2:
                    displayAllRooms();
                    break;
                case 3:
                    displayAllReservations();
                    break;
                case 4:
                    addRoom();
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        } while (choice != 5);
    }

    private void displayAllCustomers() {
        System.out.println("\n***** ALL CUSTOMERS *****");
        for (Customer customer : adminResource.getAllCustomers()) {
            System.out.println(customer);
        }
    }

    private void displayAllRooms() {
        System.out.println("\n***** ALL ROOMS *****");
        for (IRoom room : adminResource.getAllRooms()) {
            System.out.println(room);
        }
    }

    private void displayAllReservations() {
        System.out.println("\n***** ALL RESERVATIONS *****");
        adminResource.displayAllReservations();
    }

    private void addRoom() {
        try {
            System.out.print("Enter room number: ");
            int roomNo = Integer.parseInt(scanner.nextLine());
            String roomNumber = String.valueOf(roomNo);

            System.out.print("Enter price per night: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter room type (S for single bed, D for double bed): ");
            String roomType = scanner.nextLine().toUpperCase();
            if (roomType.equals("S")) {
                roomType = "SINGLE";
            } else {
                roomType = "DOUBLE";
            }
            Room room = new Room(roomNumber, price, RoomType.valueOf(roomType));
            adminResource.addRoom(Collections.singletonList(room));

            System.out.println("Room added successfully!");
        } catch (Exception ex) {
            System.out.println("Input error: Please enter valid numerical values.\n");
            addRoom();
        }
    }
}
