package service;

import collection.HotelCollection;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService {
    private static Collection<Reservation> reservations = new ArrayList<>();
    private static ReservationService instance = new ReservationService();

    public static ReservationService getSingleton() {
        return instance;
    }

    public static void addRoom(IRoom room) {
        HotelCollection.rooms.add(room);
    }

    public static IRoom getARoom(String roomId) {
        for (IRoom room : HotelCollection.rooms) {
            if (room.getRoomNumber().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    public Collection<IRoom> getAllRooms() {
        return HotelCollection.rooms;
    }

    public static Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        HotelCollection.reservations.put(customer.getEmail(), reservations);
        return reservation;
    }

    public Collection<IRoom> findRecommendRooms(Date checkIn, Date checkOut) {
        return findRooms(checkIn, checkOut);
    }

    public static Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> availableRooms = new ArrayList<>();
        for (IRoom room : HotelCollection.rooms) {
            boolean isRoomAvailable = true;
            if (reservations != null) {
                for (Reservation reservation : reservations) {
                    boolean isEqualsRoom = reservation.getRoom().equals(room);
                    boolean isCheckInDate = checkInDate.before(reservation.getCheckOutDate());
                    boolean isCheckOutDate = checkOutDate.after(reservation.getCheckInDate());
                    if (isEqualsRoom && isCheckInDate && isCheckOutDate) {
                        isRoomAvailable = false;
                        break;
                    }
                }
            }

            if (isRoomAvailable) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public static Collection<Reservation> getCustomersReservation(Customer customer) {
        return HotelCollection.reservations.getOrDefault(customer.getEmail(), Collections.emptyList());
    }

    public static void printAllReservations() {
        for (Collection<Reservation> reservations : HotelCollection.reservations.values()) {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }


    public static Map<String, Collection<Reservation>> getAllReservations() {
        return Collections.unmodifiableMap(HotelCollection.reservations);
    }
}
