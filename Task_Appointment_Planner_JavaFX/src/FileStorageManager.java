import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Loads/saves PlannerItem objects using a delimiter-separated text format.
 *
 * Format (first field is type):
 * TASK|id|titleB64|descB64|date|priority|status|completed
 * APPT|id|titleB64|descB64|date|priority|status|startTime|endTime|locationB64
 */
public class FileStorageManager {
    private final String delimiter;

    public FileStorageManager(String delimiter) {
        if (delimiter == null || delimiter.isEmpty()) {
            throw new IllegalArgumentException("delimiter is required");
        }
        this.delimiter = delimiter;
    }

    public ArrayList<PlannerItem> loadItems(String filePath) {
        validateFilePath(filePath);

        ArrayList<PlannerItem> items = new ArrayList<>();
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return items;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                items.add(parseLine(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load items: " + e.getMessage(), e);
        }

        return items;
    }

    public void saveItems(String filePath, List<PlannerItem> items) {
        validateFilePath(filePath);
        if (items == null) throw new IllegalArgumentException("items is required");

        Path path = Path.of(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (PlannerItem item : items) {
                if (item == null) continue;
                writer.write(serializeItem(item));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save items: " + e.getMessage(), e);
        }
    }

    public PlannerItem parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("line is required");
        }

        String[] parts = line.split(java.util.regex.Pattern.quote(delimiter), -1);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid line format");
        }

        String type = parts[0].trim();
        if (type.equals("TASK")) {
            if (parts.length != 8) throw new IllegalArgumentException("Invalid TASK line field count");
            String id = parts[1];
            String title = b64decode(parts[2]);
            String desc = b64decode(parts[3]);
            LocalDate date = LocalDate.parse(parts[4]);
            String priority = parts[5];
            String status = parts[6];
            boolean completed = Boolean.parseBoolean(parts[7]);
            return new Task(id, title, desc, date, priority, status, completed);
        } else if (type.equals("APPT")) {
            if (parts.length != 10) throw new IllegalArgumentException("Invalid APPT line field count");
            String id = parts[1];
            String title = b64decode(parts[2]);
            String desc = b64decode(parts[3]);
            LocalDate date = LocalDate.parse(parts[4]);
            String priority = parts[5];
            String status = parts[6];
            LocalTime start = LocalTime.parse(parts[7]);
            LocalTime end = LocalTime.parse(parts[8]);
            String location = b64decode(parts[9]);
            return new Appointment(id, title, desc, date, priority, status, start, end, location);
        } else {
            throw new IllegalArgumentException("Unknown item type: " + type);
        }
    }

    public String serializeItem(PlannerItem item) {
        if (item == null) throw new IllegalArgumentException("item is required");

        if (item instanceof Task) {
            Task t = (Task) item;
            return String.join(delimiter,
                    "TASK",
                    t.getId(),
                    b64encode(t.getTitle()),
                    b64encode(t.getDescription()),
                    t.getDate().toString(),
                    t.getPriority(),
                    t.getStatus(),
                    Boolean.toString(t.isCompleted())
            );
        } else if (item instanceof Appointment) {
            Appointment a = (Appointment) item;
            return String.join(delimiter,
                    "APPT",
                    a.getId(),
                    b64encode(a.getTitle()),
                    b64encode(a.getDescription()),
                    a.getDate().toString(),
                    a.getPriority(),
                    a.getStatus(),
                    a.getStartTime().toString(),
                    a.getEndTime().toString(),
                    b64encode(a.getLocation())
            );
        } else {
            throw new IllegalArgumentException("Unsupported PlannerItem type: " + item.getClass().getName());
        }
    }

    private void validateFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("filePath is required");
        }
    }

    private String b64encode(String s) {
        if (s == null) s = "";
        return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private String b64decode(String b64) {
        if (b64 == null || b64.isEmpty()) return "";
        return new String(Base64.getDecoder().decode(b64), StandardCharsets.UTF_8);
    }
}
