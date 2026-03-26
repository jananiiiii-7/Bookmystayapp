import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

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

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasRequests() {
        return !queue.isEmpty();
    }
}

class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public int getAvailable(String type) {
        return availability.get(type);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public Map<String, Integer> getAll() {
        return availability;
    }
}

class RoomAllocationService {

    private Map<String, Integer> counters;

    public RoomAllocationService() {
        counters = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String type = reservation.getRoomType();

        if (inventory.getAvailable(type) <= 0) {
            return;
        }

        int id = counters.getOrDefault(type, 0) + 1;
        counters.put(type, id);

        inventory.decrement(type);

        System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() + ", Room ID: " + type + "-" + id);
    }
}

class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService
    ) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    public void run() {

        while (true) {

            Reservation reservation;

            synchronized (bookingQueue) {

                if (!bookingQueue.hasRequests()) {
                    return;
                }

                reservation = bookingQueue.getNextRequest();
            }

            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));

        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService
                )
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService
                )
        );

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        System.out.println();
        System.out.println("Remaining Inventory:");

        Map<String, Integer> remaining = inventory.getAll();

        System.out.println("Single: " + remaining.get("Single"));
        System.out.println("Double: " + remaining.get("Double"));
        System.out.println("Suite: " + remaining.get("Suite"));
    }
}