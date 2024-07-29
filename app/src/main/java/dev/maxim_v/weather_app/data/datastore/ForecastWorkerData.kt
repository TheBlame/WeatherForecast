package dev.maxim_v.weather_app.data.datastore

import dev.maxim_v.weather_app.domain.entity.enums.WeatherType
import kotlinx.serialization.Serializable

@Serializable
data class ForecastWorkerData(
    val temp: String = "",
    val weatherType:WeatherType = WeatherType.CLEAR,
    val timestamp: Long = 0L
)
