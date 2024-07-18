package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class HourlyForecast(
    val time: String = "",
    val temp: Int = 0,
    val weatherType: WeatherType = WeatherType.CLEAR
)
