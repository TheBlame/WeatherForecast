package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName
import dev.maxim_v.weather_app.data.WeatherCode

data class HourlyDto(
    @SerializedName("temperature_2m")
    val temperature2m: List<Double?>,
    @SerializedName("time")
    val time: List<Long>,
    @SerializedName("weather_code")
    val weatherCode: List<WeatherCode?>
)