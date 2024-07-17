package dev.maxim_v.weather_app.domain.entity

data class UserSettings (
    val location: String,
    val tempUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
    val theme: ThemeType
)