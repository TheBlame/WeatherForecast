package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit

@Immutable
data class UserSettings (
    val tempUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
    val theme: ThemeType,
    val notification: Boolean
)