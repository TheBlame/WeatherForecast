package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.enums.WeatherType

@Immutable
data class HourlyForecast(
    val time: String,
    val temp: Int,
    val weatherType: WeatherType
)
