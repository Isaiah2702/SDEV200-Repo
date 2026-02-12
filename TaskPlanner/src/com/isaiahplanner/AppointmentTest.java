package com.isaiahplanner;

import com.isaiahplanner.model.Appointment;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class AppointmentTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalidTimeRangeThrows() {
        new Appointment("1", "Meet", "", LocalDate.of(2026, 2, 12),
                "Medium", "Pending",
                LocalTime.of(10, 0), LocalTime.of(10, 0), "Office");
    }

    @Test
    public void validAppointmentCreates() {
        Appointment a = new Appointment("2", "Meet", "Standup", LocalDate.of(2026, 2, 12),
                "Medium", "Pending",
                LocalTime.of(10, 0), LocalTime.of(10, 30), "Office");
        assertEquals(LocalTime.of(10, 30), a.getEndTime());
        assertEquals("Office", a.getLocation());
    }
}
