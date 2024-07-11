package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.ui.geometry.Offset
import dev.maxim_v.weather_app.domain.entity.HourlyForecast

class WeatherChartCoordinateProvider(
    data: List<HourlyForecast>,
    yMin: Float,
    yMax: Float,
    horizontalPadding: Float,
    xStep: Float
) : BaseWeatherChartCoordinateProvider(
    data = data,
    yMin = yMin,
    yMax = yMax,
    horizontalPadding = horizontalPadding,
    xStep = xStep,
    weightedData = calcListWeight(data.map { it.temp })
) {

    operator fun get(index: Int): Offset {
        return this[index]
    }
}