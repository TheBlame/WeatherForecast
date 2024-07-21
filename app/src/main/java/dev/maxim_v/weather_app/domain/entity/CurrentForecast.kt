package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.enums.WeatherType
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit
import dev.maxim_v.weather_app.util.Direction

@Immutable
data class CurrentForecast(
    val temp: String,
    val apparentTemp: String,
    val humidity: String,
    val windSpeed: String,
    val temperatureUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
    val windDirection: Direction,
    val precipitation: String,
    val weatherType: WeatherType
)