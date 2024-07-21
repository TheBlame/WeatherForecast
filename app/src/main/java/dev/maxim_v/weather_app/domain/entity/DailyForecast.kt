package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.enums.WeatherType

@Immutable
data class DailyForecast(
    val date: String,
    val minTemp: Int,
    val maxTemp: Int,
    val weatherType: WeatherType
)
