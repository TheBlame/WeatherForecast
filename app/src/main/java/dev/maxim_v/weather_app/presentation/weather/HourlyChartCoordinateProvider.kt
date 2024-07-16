package dev.maxim_v.weather_app.presentation.weather

import dev.maxim_v.weather_app.domain.entity.HourlyForecast

class HourlyChartCoordinateProvider(
    data: List<HourlyForecast>,
    yMin: Float,
    yMax: Float,
    horizontalPadding: Float,
    xStep: Float
) : BaseWeatherChartCoordinateProvider(
    innerList = List(data.size) { index ->
        val weightedData = calcListWeight(data.map { it.temp })
        val maxValue = weightedData.maxOrNull() ?: 0f
        val minValue = weightedData.minOrNull() ?: 0f

        WeatherChartCoordinate(
            x = horizontalPadding + index * xStep,
            y = calcCoordinate(
                yMin,
                yMax,
                minValue,
                maxValue,
                weightedData[index]
            ),
            xValue = data[index].time,
            yValue = data[index].temp.toString(),
            weatherType = data[index].weatherType
        )
    }
)