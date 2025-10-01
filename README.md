## appointment-booking-system
Smart Appointment Booking System built with Java &amp; Spring Boot, featuring CRUD for users, appointments, and services, role-based access, email confirmations, async reminders, and API testing via Swagger.

## Prerequisites
Java 21
Maven 3.9+
Docker Desktop (optional if using Docker)
Git

## Running Locally
# Option 1 – Using Maven
1. Clone the repository:
   git clone https://github.com/your-username/SmartAppointmentBookingSystem.git
   cd SmartAppointmentBookingSystem
3. Start dependencies (MySQL + RabbitMQ) using Docker Compose:
   docker-compose up -d
4. Build & run the application:
   mvn clean install
   mvn spring-boot:run
6. Access the application:
    APIs: http://localhost:8085
    Swagger UI: http://localhost:8085/swagger-ui/index.html
    RabbitMQ Management: http://localhost:15672 (user: guest, password: guest)

# Option 2 – Using Docker Only
1. Build the Docker image:
   docker build -t smartappointmentbookingsystem-image .
3. Start dependencies (MySQL + RabbitMQ):
   docker-compose up -d
5. Run the application container:
   docker run -d --name smartappointmentbookingsystem-app \
   --network=smartappointmentbookingsystem_default \
   -p 8085:8085 smartappointmentbookingsystem-image
7. Access the same URLs as above. Replace smartappointmentbookingsystem_default with the actual Docker network created by Docker Compose.

## Configuration
# Check src/main/resources/application.properties for database and RabbitMQ settings:
spring.datasource.url=jdbc:mysql://localhost:3306/appointmentbookingsystem
spring.datasource.username=root
spring.datasource.password=root
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
server.port=8085
Modify if needed.

## Stopping the Application
docker stop smartappointmentbookingsystem-app
docker rm smartappointmentbookingsystem-app
docker-compose down


RabbitMQ default credentials: guest / guest
