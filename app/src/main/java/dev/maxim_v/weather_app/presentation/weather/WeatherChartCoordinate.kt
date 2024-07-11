package dev.maxim_v.weather_app.presentation.weather

import dev.maxim_v.weather_app.domain.entity.WeatherType

data class WeatherChartCoordinate(
    val x: Float,
    val y: Float,
    val xValue: String,
    val yValue: String,
    val weatherType: WeatherType
)
