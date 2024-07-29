package dev.maxim_v.weather_app.presentation.ui.screens.forecastScreen.chartCoordinateProvider

import dev.maxim_v.weather_app.domain.entity.HourlyForecast
import dev.maxim_v.weather_app.presentation.ui.screens.forecastScreen.chartCoordinateProvider.ChartCoordinate.HourlyChartCoordinate

class HourlyChartCoordinateProvider(
    private val data: List<HourlyForecast>,
    private val yMin: Float,
    private val yMax: Float,
    private val horizontalPadding: Float,
    private val xStep: Float
) : BaseWeatherChartCoordinateProvider<HourlyChartCoordinate>() {

    private val weightedData = calcListWeight(data.map { it.temp })
    private val maxValue = weightedData.maxOrNull() ?: 0f
    private val minValue = weightedData.minOrNull() ?: 0f

    override val innerList = List(data.size) { index ->
            HourlyChartCoordinate(
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
}