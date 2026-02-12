package com.isaiahplanner.persistence;

import com.isaiahplanner.model.Appointment;
import com.isaiahplanner.model.PlannerItem;
import com.isaiahplanner.model.Task;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads/saves PlannerItem objects to a local delimited text file.
 * Matches UML 1:1 (fields + method names).
 *
 * File format (one item per line):
 * TASK|id|title|description|date|priority|status|completed
 * APPOINTMENT|id|title|description|date|priority|status|startTime|endTime|location
 *
 * Delimiter is configurable (default example: "|").
 */
public class FileStorageManager {

    private String delimiter;

    public FileStorageManager(String delimiter) {
        if (delimiter == null || delimiter.isEmpty()) {
            throw new IllegalArgumentException("delimiter is required");
        }
        this.delimiter = delimiter;
    }

    public ArrayList<PlannerItem> loadItems(String filePath) {
        validateFilePath(filePath);
        Path path = Path.of(filePath);

        ArrayList<PlannerItem> items = new ArrayList<>();
        if (!Files.exists(path)) {
            return items; // empty if file not present
        }

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) continue;
                items.add(parseLine(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load items: " + e.getMessage(), e);
        }

        return items;
    }

    public void saveItems(String filePath, List<PlannerItem> items) {
        validateFilePath(filePath);
        if (items == null) {
            throw new IllegalArgumentException("items is required");
        }

        Path path = Path.of(filePath);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (PlannerItem item : items) {
                    writer.write(serializeItem(item));
                    writer.newLine();
                }
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
        if (parts.length == 0) {
            throw new IllegalArgumentException("Invalid line");
        }

        String type = parts[0].trim().toUpperCase();

        if ("TASK".equals(type)) {
            // TASK|id|title|description|date|priority|status|completed
            if (parts.length != 8) {
                throw new IllegalArgumentException("Invalid TASK line (expected 8 fields)");
            }
            String id = parts[1];
            String title = parts[2];
            String description = parts[3];
            LocalDate date = LocalDate.parse(parts[4]);
            String priority = parts[5];
            String status = parts[6];
            boolean completed = Boolean.parseBoolean(parts[7]);

            return new Task(id, title, description, date, priority, status, completed);
        }

        if ("APPOINTMENT".equals(type)) {
            // APPOINTMENT|id|title|description|date|priority|status|startTime|endTime|location
            if (parts.length != 10) {
                throw new IllegalArgumentException("Invalid APPOINTMENT line (expected 10 fields)");
            }
            String id = parts[1];
            String title = parts[2];
            String description = parts[3];
            LocalDate date = LocalDate.parse(parts[4]);
            String priority = parts[5];
            String status = parts[6];
            LocalTime start = LocalTime.parse(parts[7]);
            LocalTime end = LocalTime.parse(parts[8]);
            String location = parts[9];

            return new Appointment(id, title, description, date, priority, status, start, end, location);
        }

        throw new IllegalArgumentException("Unknown item type: " + type);
    }

    public String serializeItem(PlannerItem item) {
        if (item == null) {
            throw new IllegalArgumentException("item is required");
        }

        if (item instanceof Task task) {
            return String.join(delimiter,
                    "TASK",
                    safe(task.getId()),
                    safe(task.getTitle()),
                    safe(task.getDescription()),
                    task.getDate().toString(),
                    safe(task.getPriority()),
                    safe(task.getStatus()),
                    Boolean.toString(task.isCompleted())
            );
        }

        if (item instanceof Appointment appt) {
            return String.join(delimiter,
                    "APPOINTMENT",
                    safe(appt.getId()),
                    safe(appt.getTitle()),
                    safe(appt.getDescription()),
                    appt.getDate().toString(),
                    safe(appt.getPriority()),
                    safe(appt.getStatus()),
                    appt.getStartTime().toString(),
                    appt.getEndTime().toString(),
                    safe(appt.getLocation())
            );
        }

        throw new IllegalArgumentException("Unsupported PlannerItem type: " + item.getClass().getName());
    }

    // UML specifies this as private
    private void validateFilePath(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath is required");
        }
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}
