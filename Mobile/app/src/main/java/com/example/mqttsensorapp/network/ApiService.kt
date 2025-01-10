package com.example.mqttsensorapp.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

data class MonitoringDevice(
    val id: String,
    val name: String
)

interface ApiService {
    @POST("devices")
    fun addDevice(@Body device: MonitoringDevice): Call<Unit>

    @GET("devices")
    fun getDevices(): Call<List<MonitoringDevice>>
}
