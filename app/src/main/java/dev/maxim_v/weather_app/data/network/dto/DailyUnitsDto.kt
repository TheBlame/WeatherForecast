package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName

data class DailyUnitsDto(
    @SerializedName("temperature_2m_max")
    val temperature2mMax: String,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("weather_code")
    val weatherCode: String
)