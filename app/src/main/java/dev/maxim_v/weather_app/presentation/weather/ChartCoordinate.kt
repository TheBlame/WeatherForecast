package dev.maxim_v.weather_app.presentation.weather

import dev.maxim_v.weather_app.domain.entity.enums.WeatherType

sealed class ChartCoordinate {

    data class HourlyChartCoordinate(
        val x: Float,
        val y: Float,
        val xValue: String,
        val yValue: String,
        val weatherType: WeatherType
    ) : ChartCoordinate()

    data class DailyChartCoordinate(
        val x: Float,
        val y: Float,
        val xValue: String,
        val minXValue: String,
        val maxXValue: String,
        val weatherType: WeatherType
    ) : ChartCoordinate(
    )
}