package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName

data class CurrentDto(
    @SerializedName("temperature_2m")
    val temperature2m: Double,
    @SerializedName("time")
    val time: Int,
    @SerializedName("weather_code")
    val weatherCode: Int
)