package dev.maxim_v.weather_app.presentation.weather

import dev.maxim_v.weather_app.domain.entity.DailyForecast
import dev.maxim_v.weather_app.presentation.weather.ChartCoordinate.DailyChartCoordinate

class DailyChartCoordinateProvider(
    private val data: List<DailyForecast>,
    private val yMin: Float,
    private val yMax: Float,
    private val horizontalPadding: Float,
    private val xStep: Float
) :
    BaseWeatherChartCoordinateProvider<DailyChartCoordinate>() {

    private val weightedData = calcListWeight(data.map { it.maxTemp })
    private val maxValue = weightedData.maxOrNull() ?: 0f
    private val minValue = weightedData.minOrNull() ?: 0f

    override val innerList: List<DailyChartCoordinate> = List(data.size) { index ->
        DailyChartCoordinate(
            x = horizontalPadding + index * xStep,
            y = calcCoordinate(
                yMin,
                yMax,
                minValue,
                maxValue,
                weightedData[index]
            ),
            xValue = data[index].date,
            minXValue = data[index].minTemp.toString(),
            maxXValue = data[index].maxTemp.toString(),
            weatherType = data[index].weatherType
        )
    }
}

fun main() {

}