package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class FullForecast(
    val location: String,
    val currentForecast: CurrentForecast,
    val hourlyForecast: List<HourlyForecast>,
    val dailyForecast: List<DailyForecast>
)