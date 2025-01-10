# IoT Monitoring System

## Overview

The IoT Monitoring System is a comprehensive solution designed to track environmental data—such as temperature, humidity, luminosity, and pressure—across various rooms. It comprises three main components:

1. **Embedded System**: Collects environmental data and displays it on an OLED screen.
2. **Web Application**: Built using the Spring Boot framework with Java and Gradle, it provides a user interface for monitoring data and managing devices.
3. **Mobile Application**: Developed with Kotlin in Android Studio, it displays real-time data and allows control over monitoring devices.

The project also utilizes **HiveMQ** as an MQTT cloud platform for efficient data transmission between components.

## Course Information

**Course Name**: ToolBox3 - Web, Mobile, and Embedded

**Group Members**:
- Abdelaziz Mohamad
- Ouadie Boussetta
- Fatima-Zahra Tahiri

## Features

### Embedded System
- **Data Collection**: Gathers temperature, humidity, luminosity, and pressure readings.
- **Data Display**: Shows real-time data on an OLED screen.
- **Data Transmission**: Publishes collected data to the web and mobile interfaces using HiveMQ.
- **Command Reception**: Accepts commands from the web application.

### Web Application
- **Dashboard**: Visualizes environmental data in real-time.
- **Room Management**: Allows creation and management of rooms.
- **Messaging**: Enables sending messages to the embedded system.
- **Authentication**: Secures access to the application.

### Mobile Application
- **Real-Time Data**: Displays live environmental data.
- **Device Management**: Facilitates adding, editing, or removing monitoring devices.
- **Device Control**: Sends actions to control devices remotely.

## Pre-requisites

### Embedded System
- **Hardware**:
  - ESP32 microcontroller
  - Sensors: DHT11/22 (temperature and humidity), LDR (luminosity), BMP180/280 (pressure)
  - OLED Display
  - Power supply

### Web Application
- **Software**:
  - Java Development Kit (JDK)
  - Gradle
  - Vue.js (for frontend)

### Mobile Application
- **Software**:
  - Android Studio
  - Kotlin configured in Android Studio

## Installation Guide

### Embedded System
1. **Hardware Setup**: Connect the sensors and OLED display to the ESP32 as per the circuit diagram.
2. **Firmware Configuration**: Update the WiFi SSID and password in the firmware code.
3. **Flashing**: Flash the configured firmware to the ESP32.

### Web Application
1. **Clone Repository**: Clone the project repository.
2. **Backend Setup**:
   - Navigate to the `web-app` directory.
   - Open the project in an IDE (e.g., IntelliJ IDEA or Eclipse).
   - Configure the H2 database settings in the `application.properties` file.
   - Build the project: `./gradlew build`
   - Run the backend server: `./gradlew bootRun`
3. **Frontend Setup**:
   - Navigate to the `frontend` directory.
   - Install Vue CLI globally: `npm install -g @vue/cli`
   - Install dependencies: `npm install`
   - Install Axios: `npm install axios`
   - Run the frontend server: `npm run serve`

### Mobile Application
1. **Clone Repository**: Clone the project repository.
2. **Open Project**: Open the project in Android Studio.
3. **Install Dependencies**: Use Gradle to install necessary dependencies.
4. **Run Application**: Deploy the app on an emulator or physical device using Android Studio's run configuration.

## Future Enhancements

- **Enhanced Authentication**: Implement robust authentication mechanisms to improve security.
- **Data Analytics**: Introduce data analytics features for trend analysis and predictive insights.
- **Alert System**: Develop an alert system to notify users of critical environmental changes.
- **Remote Firmware Updates**: Enable over-the-air updates for the embedded system to facilitate easy maintenance.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

We extend our gratitude to our course instructors and peers for their support and guidance throughout this project.

---

*This project was developed as part of the ToolBox3 course, focusing on web, mobile, and embedded systems integration.*
