# IoT Monitoring System

## Tutorial Introduction

This tutorial will help you set up and understand an IoT Monitoring System designed to track environmental data such as temperature, humidity, luminosity, and pressure. By following this guide, you will learn how to set up the embedded system, web application, and mobile application to monitor and manage devices effectively.

The system consists of three main components:

1. **Embedded System**: Collects environmental data and displays it on an OLED screen.
2. **Web Application**: Built using the Spring Boot framework with Java and Gradle, it provides a user interface for monitoring data and managing devices.
3. **Mobile Application**: Developed with Kotlin in Android Studio, it displays real-time data and allows control over monitoring devices.

---

## Features

### Embedded System
- Collects and displays environmental data: temperature, humidity, luminosity, and pressure.
- Publishes data to the web and mobile interfaces.
- Accepts commands from the web application.

### Web Application
- Dashboard to visualize environmental data.
- Room creation and management.
- Send messages to the embedded system.
- Authentication system.

### Mobile Application
- Real-time data visualization.
- Add, edit, or remove monitoring devices.
- Send actions to control devices.

---

## Architecture

The system uses the following technologies:

1. **Embedded System**:
   - Microcontroller: ESP32
   - Sensors: DHT11/22 (temperature and humidity), LDR (luminosity), BMP180/280 (pressure)
   - OLED Screen for data display

2. **Web Application**:
   - Framework: Spring Boot
   - Language: Java
   - Build Tool: Gradle
   - Database: H2 (in-memory database)

3. **Mobile Application**:
   - Language: Kotlin
   - Development Environment: Android Studio

---

## Pre-requisites

- For the **Embedded System**:
  - ESP32 microcontroller
  - Sensors: DHT11/22, LDR, BMP180/280
  - OLED Display
  - Power supply
  
- For the **Web Application**:
  - Java Development Kit (JDK) installed
  - Gradle installed

- For the **Mobile Application**:
  - Android Studio installed
  - Kotlin configured in Android Studio

---

## Installation Guide

### Embedded System
1. Connect the sensors and OLED display to the ESP32 as per the circuit diagram.
2. Flash the provided firmware to the ESP32.
3. Update the WiFi SSID and password in the firmware code.

### Web Application
1. Clone the repository.
2. Navigate to the `web-app` directory.
3. Open the project in an IDE (e.g., IntelliJ IDEA or Eclipse).
4. Run `gradle build` to build the project.
5. Configure the H2 database settings in the `application.properties` file.
6. Start the server using `./gradlew bootRun`.

### Frontend Setup
1. Navigate to the `frontend` directory.
2. Install Vue CLI globally using:
   ```
   npm install -g @vue/cli
   ```
3. Install Axios for API requests:
   ```
   npm install axios
   ```
4. Install project dependencies:
   ```
   npm install
   ```
5. Start the development server:
   ```
   npm run serve
   ```

### Mobile Application
1. Clone the repository.
2. Open the project in Android Studio.
3. Install dependencies using the Gradle build system in Android Studio.
4. Run the app on an emulator or physical device using `Run` in Android Studio.

---

## User Guide

### Embedded System
- Connect the embedded system to a power supply.
- Ensure the WiFi SSID and password in the firmware match your network.
- The OLED display will show the collected data: temperature, humidity, luminosity, and pressure.

### Web Application
1. Open the application in a web browser.
2. Authenticate yourself using the following credentials:
   - **Login**: `admin`
   - **Password**: `password`
3. After logging in, you will see an empty dashboard.
4. Create a room:
   - The room number starts at 1 and auto-increments for subsequent rooms.
   - Note: The embedded system publishes data for room number 2 by default. This can be changed in the code.
5. Once the room is created, you will see the data published from the embedded system.
6. To send a message to the embedded system:
   - Navigate to the room.
   - Send a message, which will be displayed on the embedded system.

### Mobile Application
1. Open the mobile app.
2. View real-time environmental data published by the embedded system.
3. Add, edit, or remove monitoring devices as needed.
---
