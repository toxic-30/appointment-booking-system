FROM openjdk:21-jdk-slim

# Set timezone first
ENV TZ=Asia/Kolkata
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Copy JAR
COPY target/SmartAppointmentBookingSystem-0.0.1-SNAPSHOT.jar SmartAppointmentBookingSystem-0.0.1-SNAPSHOT.jar

# Expose port
EXPOSE 8085

# Run application
ENTRYPOINT ["java", "-Duser.timezone=Asia/Kolkata", "-jar", "SmartAppointmentBookingSystem-0.0.1-SNAPSHOT.jar"]



