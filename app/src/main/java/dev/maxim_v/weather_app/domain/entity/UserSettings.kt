package dev.maxim_v.weather_app.domain.entity

import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit

data class UserSettings (
    val location: String,
    val tempUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
    val theme: ThemeType
)