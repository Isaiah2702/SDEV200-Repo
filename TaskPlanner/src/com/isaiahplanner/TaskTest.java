package com.isaiahplanner;

import com.isaiahplanner.model.Task;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class TaskTest {

    @Test
    public void markCompleteSetsCompletedAndStatus() {
        Task t = new Task("1", "Homework", "Math", LocalDate.of(2026, 2, 12), "High", "Pending", false);
        t.markComplete();
        assertTrue(t.isCompleted());
        assertEquals("Completed", t.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void completedAndStatusMustMatch() {
        new Task("2", "X", "", LocalDate.of(2026, 2, 12), "Low", "Completed", false);
    }
}
