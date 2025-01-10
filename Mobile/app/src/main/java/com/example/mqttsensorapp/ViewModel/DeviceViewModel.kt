package com.example.mqttsensorapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttsensorapp.network.ApiService
import com.example.mqttsensorapp.network.MonitoringDevice
import com.example.mqttsensorapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DeviceViewModel : ViewModel() {

    private val api = RetrofitClient.instance.create(ApiService::class.java)
    val devices = mutableListOf<MonitoringDevice>()

    // Charger les appareils depuis l'API
    fun loadDevices(onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getDevices().execute()
                if (response.isSuccessful) {
                    devices.clear()
                    devices.addAll(response.body() ?: emptyList())
                } else {
                    onError("Failed to load devices: ${response.message()}")
                }
            } catch (e: HttpException) {
                onError("HTTP Error: ${e.message}")
            } catch (e: Exception) {
                onError("Unexpected Error: ${e.message}")
            }
        }
    }

    // Ajouter un appareil via l'API
    fun addDevice(device: MonitoringDevice, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.addDevice(device).execute()
                if (response.isSuccessful) {
                    devices.add(device)
                    onSuccess()
                } else {
                    onError("Failed to add device: ${response.message()}")
                }
            } catch (e: HttpException) {
                onError("HTTP Error: ${e.message}")
            } catch (e: Exception) {
                onError("Unexpected Error: ${e.message}")
            }
        }
    }
}
