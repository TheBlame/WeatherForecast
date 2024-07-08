package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName
import dev.maxim_v.weather_app.data.WeatherCode

data class DailyDto(
    @SerializedName("temperature_2m_max")
    val temperature2mMax: List<Double>,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: List<Double>,
    @SerializedName("time")
    val time: List<Long>,
    @SerializedName("weather_code")
    val weatherCode: List<WeatherCode>
)