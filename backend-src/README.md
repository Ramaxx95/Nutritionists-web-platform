# iNutri - Backend

iNutri is a platform for nutrition professionals to create and manage personalized meal plans. This repository contains the backend service developed in Java using Spring Boot.

## 🚀 Key Features

- 📋 Patient management
- 🥦 Food catalog with nutritional information
- 🍽️ Personalized meal plan generator
- 🧠 AI-assisted food recommendations (optional)
- 🕒 Appointment scheduling and follow-up tracking
- 📊 VCT and IPC calculations based on patient data

## 🛠️ Technologies Used

- **Java 21+**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **MySQL**
- **Lombok**
- **Swagger / OpenAPI** for API documentation
- **JWT** for secure authentication
- **Flyway**

## 📦 Project Structure

```
inutri-backend/
├── src/
│   ├── main/
│   │   ├── java/com/inutri/
│   │   │   ├── config/
│   │   │   ├── controlador/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── modelo/
│   │   │   ├── repositorio/
│   │   │   ├── seguridad/
│   │   │   └── servicio/
│   │   │       └── impl/
│   │   └── resources/
│   │       ├── db/
│   │       │   ├── migration/
│   │       │   └── reference/
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       ├── java/com/inutri/cucumber
│       │   ├── steps/
│       │   └── support/
│       └── resources/
│           ├── features/
│           └── application-test.properties
└── README.md
```

## ⚙️ Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/inutri-backend.git
   cd inutri-backend
   ```

2. Configure environment variables for database connection:
   ```properties
   DB_USERNAME=mi_usuario
   DB_PASSWORD=mi_contraseña
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=[dev|prod]'
   ```

## 📚 API Documentation

Once the app is running, Swagger UI is available at:

```
http://localhost:8081/swagger-ui/
```

## 🧪 Running Tests

```bash
./mvnw test
```

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

## 📄 License

This project is licensed under the MIT License. See the `LICENSE` file for details.