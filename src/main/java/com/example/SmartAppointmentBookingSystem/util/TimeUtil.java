package com.example.SmartAppointmentBookingSystem.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Kolkata");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Get current IST timestamp
    public static LocalDateTime now() {
        return ZonedDateTime.now(ZONE_ID).toLocalDateTime();
    }

    // Format LocalDateTime to readable string
    public static String format(LocalDateTime time) {
        return time.format(FORMATTER);
    }
}


