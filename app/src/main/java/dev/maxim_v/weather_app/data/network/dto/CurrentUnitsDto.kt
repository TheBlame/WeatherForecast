package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName

data class CurrentUnitsDto(
    @SerializedName("interval")
    val interval: String,
    @SerializedName("temperature_2m")
    val temperature2m: Temperature2UnitDto,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: WindSpeedUnitDto,
    @SerializedName("time")
    val time: String,
    @SerializedName("weather_code")
    val weatherCode: String
)