package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName

data class CurrentDto(
    @SerializedName("temperature_2m")
    val temperature2m: Double,
    @SerializedName("apparent_temperature")
    val apparentTemperature: Double,
    @SerializedName("precipitation")
    val precipitation: Int,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: Int,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: Double,
    @SerializedName("wind_direction_10m")
    val windDirection10m: Int,
    @SerializedName("time")
    val time: Long,
    @SerializedName("weather_code")
    val weatherCodeDto: WeatherCodeDto
)