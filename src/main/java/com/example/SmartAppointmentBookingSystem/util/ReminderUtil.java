package com.example.SmartAppointmentBookingSystem.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReminderUtil {
    
    @Value("${appointment.reminder.offset-hours:4}")
    private int reminderOffsetHours;

    public int getReminderOffsetHours() {
        return reminderOffsetHours;
    }
}
