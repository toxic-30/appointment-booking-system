package com.example.SmartAppointmentBookingSystem.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private static final ZoneId IST_ZONE = ZoneId.of("Asia/Kolkata");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Get current IST LocalDateTime
    public static LocalDateTime now() {
        return ZonedDateTime.now(IST_ZONE).toLocalDateTime();
    }

    // Format LocalDateTime into readable string (IST)
    public static String format(LocalDateTime time) {
        return time.atZone(IST_ZONE).format(FORMATTER);
    }

    // Parse string back to LocalDateTime in IST
    public static LocalDateTime parse(String timeString) {
        return LocalDateTime.parse(timeString, FORMATTER);
    }

    // Calculate delay in ms between now and targetTime
    public static long millisUntil(LocalDateTime targetTime) {
        ZonedDateTime now = ZonedDateTime.now(IST_ZONE);
        ZonedDateTime target = targetTime.atZone(IST_ZONE);
        return Duration.between(now, target).toMillis();
    }
}

