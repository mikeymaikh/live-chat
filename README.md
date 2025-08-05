# live-chat

A simple Spring Boot chat application with a PostgreSQL backend and simple HTML/JavaScript frontend for testing.  
Run the app with Maven (`mvnw.cmd spring-boot:run`) or at LiveChatApplication.java, and access the chat UI at `http://localhost:8080/chat.html`.  
Messages are stored in PostgreSQL and displayed in real time between selected users.

## How to create a PostgreSQL Docker container for the server

1. Make sure Docker is installed and running.
2. Run this command in your powershell terminal:
   docker run --name live-chat-postgres -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=chat_db -p 5432:5432 -d postgres
3. The Spring Boot app will connect to this database using the settings in `application.properties


