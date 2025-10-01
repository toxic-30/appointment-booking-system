FROM openjdk:21-jdk-slim
COPY target/SmartAppointmentBookingSystem-0.0.1-SNAPSHOT.jar SmartAppointmentBookingSystem-0.0.1-SNAPSHOT.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "SmartAppointmentBookingSystem-0.0.1-SNAPSHOT.jar"]

