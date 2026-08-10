# GreenHouse Management System

A JavaFX-based smart greenhouse simulation and management application built using modern Object-Oriented Programming (OOP) principles, multithreading, and file-based data persistence.

## 🌟 Features
* **Role-Based Access Control**: Secure login distinguishing between Admin (full control) and Staff (monitoring only) roles.
* **Live Dashboard Simulation**: A multi-threaded simulation engine that generates live environmental data (temperature, humidity, soil moisture).
* **Automated Irrigation**: Background threads actively monitor soil moisture and consume water resources to keep plants healthy.
* **Plant Growth Engine**: A complex algorithm calculates daily plant growth and health decay based on strict environmental tolerances.
* **Real-time Alerting**: Alerts are automatically generated and displayed when critical thresholds are breached.
* **Data Persistence**: State is safely saved and loaded from local CSV files ensuring progress is never lost.

## 🛠️ Technology Stack
* **Language**: Java 17
* **GUI Framework**: JavaFX (with FXML)
* **Build Tool**: Maven
* **Testing**: JUnit 5 Jupiter
* **Architecture**: MVC (Model-View-Controller) / Service Layer Pattern

## 🚀 How to Build and Run

### Prerequisites
- JDK 17 installed and configured on your system path.
- Apache Maven installed.

### Running the Application
1. Clone the repository and navigate into the root directory.
2. Run the application via the terminal using the Maven JavaFX plugin:
```bash
mvn clean javafx:run
```

### Running the Tests
To execute the automated JUnit 5 test suite:
```bash
mvn clean test
```

## 📸 Screenshots

- ![Login](https://raw.githubusercontent.com/MFaiqAbdullah/GreenHouse-Management-System/assets/screenshots/login.png)
- ![Dashboard](https://raw.githubusercontent.com/MFaiqAbdullah/GreenHouse-Management-System/assets/screenshots/dashboard.png)
- ![Plants View](https://raw.githubusercontent.com/MFaiqAbdullah/GreenHouse-Management-System/assets/screenshots/plants.png)
- ![Alerts](https://raw.githubusercontent.com/MFaiqAbdullah/GreenHouse-Management-System/assets/screenshots/alerts.png)
- ![Zones](https://raw.githubusercontent.com/MFaiqAbdullah/GreenHouse-Management-System/assets/screenshots/zones.png)
- ![Resources](https://raw.githubusercontent.com/MFaiqAbdullah/GreenHouse-Management-System/assets/screenshots/resources.png)


