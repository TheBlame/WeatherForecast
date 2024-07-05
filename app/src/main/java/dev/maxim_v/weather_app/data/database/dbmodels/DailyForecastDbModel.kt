package dev.maxim_v.weather_app.data.database.dbmodels

import dev.maxim_v.weather_app.domain.entity.WeatherType

data class DailyForecastDbModel(
    val minTemp: Double,
    val maxTemp: Double,
    val weatherType: WeatherType,
    val timestamp: Long
)
