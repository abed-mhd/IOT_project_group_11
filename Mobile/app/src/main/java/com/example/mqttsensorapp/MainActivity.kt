package com.example.mqttsensorapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    data class MonitoringDevice(
        val id: String,
        val name: String
    )

    private lateinit var mqttClient: Mqtt5AsyncClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isDeviceListVisible by mutableStateOf(false)
        var showAddDeviceForm by mutableStateOf(false)

        // States for real-time data
        val connectionStatus = mutableStateOf("Disconnected")
        val temperature = mutableStateOf("-- °C")
        val humidity = mutableStateOf("-- %")
        val pressure = mutableStateOf("-- hPa")
        val luminosity = mutableStateOf("-- lx")

        setContent {
            val devices = remember {
                mutableStateListOf<MonitoringDevice>().apply {
                    addAll(loadDevicesFromPreferences(this@MainActivity))
                }
            }

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isDeviceListVisible) {
                        DeviceListScreen(
                            devices = devices,
                            onBack = { isDeviceListVisible = false },
                            onDeleteDevice = { device ->
                                devices.remove(device)
                                saveDevicesToPreferences(this@MainActivity, devices)
                            }
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            // Title
                            Text(text = "Sensor App", style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.height(16.dp))

                            // Real-time data display
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "Status: ${connectionStatus.value}")
                                    Text(text = "Temperature: ${temperature.value}")
                                    Text(text = "Humidity: ${humidity.value}")
                                    Text(text = "Pressure: ${pressure.value}")
                                    Text(text = "Luminosity: ${luminosity.value}")
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Buttons for actions
                            Button(onClick = { connectToMqtt(connectionStatus, temperature, humidity, pressure, luminosity) }) {
                                Text("Connect to MQTT")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { isDeviceListVisible = true }) {
                                Text("View Devices")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { showAddDeviceForm = true }) {
                                Text("Add Monitoring Device")
                            }

                            // Add Device Form
                            if (showAddDeviceForm) {
                                AddDeviceForm(
                                    onAddDevice = { id, name ->
                                        val newDevice = MonitoringDevice(id = id, name = name)
                                        devices.add(newDevice)
                                        saveDevicesToPreferences(this@MainActivity, devices)
                                        showAddDeviceForm = false
                                    },
                                    onCancel = { showAddDeviceForm = false }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun connectToMqtt(
        connectionStatus: MutableState<String>,
        temperature: MutableState<String>,
        humidity: MutableState<String>,
        pressure: MutableState<String>,
        luminosity: MutableState<String>
    ) {
        mqttClient = Mqtt5Client.builder()
            .identifier("AndroidAppClient")
            .serverHost("594bf801d8c342e993ed74b68dbbc232.s1.eu.hivemq.cloud")
            .serverPort(8883)
            .sslWithDefaultConfig()
            .buildAsync()

        mqttClient.connectWith()
            .simpleAuth()
            .username("mobile_client")
            .password("Mobile32_client_pwd".toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { _, throwable ->
                if (throwable == null) {
                    connectionStatus.value = "Connected"
                    subscribeToTopic(temperature, humidity, pressure, luminosity)
                } else {
                    connectionStatus.value = "Connection Failed"
                }
            }
    }

    private fun subscribeToTopic(
        temperature: MutableState<String>,
        humidity: MutableState<String>,
        pressure: MutableState<String>,
        luminosity: MutableState<String>
    ) {
        mqttClient.subscribeWith()
            .topicFilter("rooms/sensors/data")
            .qos(MqttQos.AT_LEAST_ONCE)
            .send()
            .whenComplete { _, throwable ->
                if (throwable == null) {
                    mqttClient.toAsync().publishes(MqttGlobalPublishFilter.ALL) { publish ->
                        val payload = String(publish.payloadAsBytes, StandardCharsets.UTF_8)
                        parsePayload(payload, temperature, humidity, pressure, luminosity)
                    }
                }
            }
    }

    private fun parsePayload(
        payload: String,
        temperature: MutableState<String>,
        humidity: MutableState<String>,
        pressure: MutableState<String>,
        luminosity: MutableState<String>
    ) {
        try {
            val jsonObject = Gson().fromJson(payload, JsonObject::class.java)
            val temperatureValue = jsonObject["temperature"].asDouble
            val humidityValue = jsonObject["humidity"].asDouble
            val pressureValue = jsonObject["pressure"].asDouble
            val luminosityValue = jsonObject["luminosity"].asDouble

            temperature.value = "$temperatureValue °C"
            humidity.value = "$humidityValue %"
            pressure.value = "$pressureValue hPa"
            luminosity.value = "$luminosityValue lx"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Composable
    fun AddDeviceForm(onAddDevice: (String, String) -> Unit, onCancel: () -> Unit) {
        var deviceId by remember { mutableStateOf("") }
        var deviceName by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") } // For error messages

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Device ID Field
            TextField(
                value = deviceId,
                onValueChange = {
                    deviceId = it
                    errorMessage = ""
                },
                label = { Text("Device ID (number)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Device Name Field
            TextField(
                value = deviceName,
                onValueChange = {
                    deviceName = it
                    errorMessage = ""
                },
                label = { Text("Device Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Error Message
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Action Buttons
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { onCancel() }) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    when {
                        deviceId.isBlank() -> {
                            errorMessage = "Device ID is required."
                        }
                        deviceName.isBlank() -> {
                            errorMessage = "Device Name is required."
                        }
                        !isValidFloat(deviceId) -> {
                            errorMessage = "Device ID must be a valid number."
                        }
                        else -> {
                            onAddDevice(deviceId, deviceName)
                        }
                    }
                }) {
                    Text("Add")
                }
            }
        }
    }

    private fun isValidFloat(input: String): Boolean {
        return try {
            input.toFloat()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    @Composable
    fun DeviceListScreen(
        devices: MutableList<MonitoringDevice>,
        onBack: () -> Unit,
        onDeleteDevice: (MonitoringDevice) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Devices List", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(devices) { device ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "ID: ${device.id}")
                                Text(text = "Name: ${device.name}")
                            }
                            Button(
                                onClick = { onDeleteDevice(device) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onBack() }) {
                Text("Back")
            }
        }
    }

    private fun saveDevicesToPreferences(context: Context, devices: List<MonitoringDevice>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("devices_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(devices)
        editor.putString("devices_list", json)
        editor.apply()
    }

    private fun loadDevicesFromPreferences(context: Context): MutableList<MonitoringDevice> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("devices_prefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("devices_list", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<MonitoringDevice>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}
