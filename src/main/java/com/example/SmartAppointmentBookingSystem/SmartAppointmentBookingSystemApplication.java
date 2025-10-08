package com.example.SmartAppointmentBookingSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartAppointmentBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartAppointmentBookingSystemApplication.class, args);
	}
}
