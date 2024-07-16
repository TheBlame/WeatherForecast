package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

abstract class BaseWeatherChartCoordinateProvider<T : ChartCoordinate>(
    innerList: Collection<T>
) : Collection<T> by innerList {

    operator fun get(index: Int): Offset {
        return this[index]
    }

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