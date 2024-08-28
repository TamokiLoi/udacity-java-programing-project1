package collection;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class HotelCollection {
    public static volatile Map<String, Customer> customers = new HashMap<>();
    public static volatile Set<IRoom> rooms = new HashSet<>();
    public static volatile Map<String, Collection<Reservation>> reservations = new HashMap<>();
}
