package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName
import dev.maxim_v.weather_app.data.WeatherCode

data class CurrentDto(
    @SerializedName("temperature_2m")
    val temperature2m: Double,
    @SerializedName("time")
    val time: Long,
    @SerializedName("weather_code")
    val weatherCode: WeatherCode
)