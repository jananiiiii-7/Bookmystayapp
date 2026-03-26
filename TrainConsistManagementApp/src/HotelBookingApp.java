import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {

    private HashMap<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public boolean hasRoomType(String type) {
        return availability.containsKey(type);
    }
}

class BookingRequestQueue {

    private Queue<String> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(String request) {
        queue.offer(request);
    }
}

class ReservationValidator {

    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.hasRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {

            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            validator.validate(guestName, roomType, inventory);

            bookingQueue.addRequest(guestName + "-" + roomType);

            System.out.println("Booking request accepted.");

        } catch (InvalidBookingException e) {

            System.out.println("Booking failed: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}