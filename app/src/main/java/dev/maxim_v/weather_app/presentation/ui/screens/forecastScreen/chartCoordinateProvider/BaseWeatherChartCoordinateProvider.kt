package dev.maxim_v.weather_app.presentation.ui.screens.forecastScreen.chartCoordinateProvider

import kotlin.math.abs

abstract class BaseWeatherChartCoordinateProvider<T : ChartCoordinate> : Collection<T> {

    abstract val innerList: List<T>

    operator fun get(index: Int): T {
        return innerList[index]
    }

    override val size: Int
        get() = innerList.size

    override fun isEmpty(): Boolean {
        return innerList.isEmpty()
    }

    override fun iterator(): Iterator<T> {
        return innerList.iterator()
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return innerList.containsAll(elements)
    }

    override fun contains(element: T): Boolean {
        return innerList.contains(element)
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