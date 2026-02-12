package com.isaiahplanner;

import com.isaiahplanner.model.PlannerItem;
import com.isaiahplanner.model.Task;
import com.isaiahplanner.persistence.FileStorageManager;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FileStorageManagerTest {

    @Test
    public void serializeThenParseRoundTripTask() {
        FileStorageManager fsm = new FileStorageManager("|");
        Task t = new Task("9", "Study", "Ch 15", LocalDate.of(2026, 2, 12), "High", "Pending", false);

        String line = fsm.serializeItem(t);
        PlannerItem parsed = fsm.parseLine(line);

        assertTrue(parsed instanceof Task);
        Task t2 = (Task) parsed;
        assertEquals(t.getId(), t2.getId());
        assertEquals(t.getTitle(), t2.getTitle());
        assertEquals(t.getDate(), t2.getDate());
        assertEquals(t.getPriority(), t2.getPriority());
        assertEquals(t.getStatus(), t2.getStatus());
        assertEquals(t.isCompleted(), t2.isCompleted());
    }

    @Test
    public void saveAndLoadWorks() throws Exception {
        FileStorageManager fsm = new FileStorageManager("|");
        List<PlannerItem> items = new ArrayList<>();
        items.add(new Task("1", "HW", "", LocalDate.of(2026, 2, 12), "Low", "Pending", false));

        Path temp = Files.createTempFile("planner", ".txt");
        fsm.saveItems(temp.toString(), items);

        ArrayList<PlannerItem> loaded = fsm.loadItems(temp.toString());
        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0) instanceof Task);
    }
}
