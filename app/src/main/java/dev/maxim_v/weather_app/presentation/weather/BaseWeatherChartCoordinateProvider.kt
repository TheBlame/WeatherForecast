package dev.maxim_v.weather_app.presentation.weather

import dev.maxim_v.weather_app.domain.entity.HourlyForecast
import kotlin.math.abs

abstract class BaseWeatherChartCoordinateProvider(
    data: List<HourlyForecast>,
    yMin: Float,
    yMax: Float,
    horizontalPadding: Float,
    xStep: Float,
    weightedData: List<Float>,
    innerList: Collection<WeatherChartCoordinate> = List(data.size) { index ->
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
) : Collection<WeatherChartCoordinate> by innerList {

    protected companion object {

        fun calcListWeight(list: List<Int>): List<Float> {
            val offset =
                if ((list.minOrNull() ?: 0) < 0) abs((list.minOrNull() ?: 0).toFloat()) else 0f
            val sum = list
                .map { it + offset }
                .sum()
            val onePercent = sum / 100
            return buildList {
                for (item in list) {
                    add(((item + offset) / onePercent))
                }
            }
        }

        fun calcCoordinate(
            minCoordinate: Float,
            maxCoordinate: Float,
            minNumber: Float,
            maxNumber: Float,
            desiredNumber: Float
        ): Float {
            if (desiredNumber == maxNumber) return minCoordinate
            if (desiredNumber == minNumber && desiredNumber <= 0) return maxCoordinate
            val size = maxCoordinate - minCoordinate
            return maxCoordinate - size / maxNumber * desiredNumber
        }
    }
}