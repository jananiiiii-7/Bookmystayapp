import java.util.LinkedList;
import java.util.Queue;

/**
 * ==============================================================
 * Book My Stay App
 * UC5: Booking Request (First-Come-First-Served)
 * ==============================================================
 *
 * Demonstrates fair booking request handling using FIFO queue.
 *
 * @version 5.0
 */

// -------------------- RESERVATION CLASS --------------------
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- BOOKING REQUEST QUEUE --------------------
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

// -------------------- MAIN APPLICATION --------------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Request Queue\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Create booking requests
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // Add requests to queue
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Process requests in FIFO order
        while (bookingQueue.hasPendingRequests()) {

            Reservation current = bookingQueue.getNextRequest();

            System.out.println("Processing booking for Guest: "
                    + current.getGuestName()
                    + ", Room Type: "
                    + current.getRoomType());
        }
    }
}