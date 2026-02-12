package com.isaiahplanner;

import com.isaiahplanner.model.Task;
import org.junit.Test;

import java.time.LocalDate;

public class PlannerItemValidationTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalidPriorityThrows() {
        new Task("1", "Title", "", LocalDate.of(2026, 2, 12), "Urgent", "Pending", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidStatusThrows() {
        new Task("1", "Title", "", LocalDate.of(2026, 2, 12), "Low", "Done", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void blankTitleThrows() {
        new Task("1", "   ", "", LocalDate.of(2026, 2, 12), "Low", "Pending", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateThrows() {
        new Task("1", "Title", "", null, "Low", "Pending", false);
    }
}
