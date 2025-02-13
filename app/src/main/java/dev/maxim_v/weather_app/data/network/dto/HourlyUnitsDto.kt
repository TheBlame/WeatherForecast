package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName

data class HourlyUnitsDto(
    @SerializedName("temperature_2m")
    val temperature2m: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("weather_code")
    val weatherCode: String
)