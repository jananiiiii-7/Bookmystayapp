import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public Map<String, Integer> getAll() {
        return availability;
    }

    public void set(String type, int value) {
        availability.put(type, value);
    }
}

class FilePersistenceService {

    public void saveInventory(RoomInventory inventory, String filePath) {

        try {
            FileWriter writer = new FileWriter(filePath);

            for (Map.Entry<String, Integer> entry : inventory.getAll().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }

            writer.close();

            System.out.println("Inventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Failed to save inventory.");
        }
    }

    public void loadInventory(RoomInventory inventory, String filePath) {

        try {

            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("No valid inventory data found. Starting fresh.");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("=");

                if (parts.length == 2) {
                    inventory.set(parts[0], Integer.parseInt(parts[1]));
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("No valid inventory data found. Starting fresh.");
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("System Recovery");

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistence = new FilePersistenceService();

        String filePath = "inventory.txt";

        persistence.loadInventory(inventory, filePath);

        System.out.println();
        System.out.println("Current Inventory:");

        Map<String, Integer> data = inventory.getAll();

        System.out.println("Single: " + data.get("Single"));
        System.out.println("Double: " + data.get("Double"));
        System.out.println("Suite: " + data.get("Suite"));

        persistence.saveInventory(inventory, filePath);
    }
}