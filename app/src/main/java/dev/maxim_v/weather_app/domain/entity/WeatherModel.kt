package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.WeatherType.CLEAR

@Immutable
sealed class WeatherModel {

    data class CurrentSample(
        val location: String = "",
        val time: String = "",
        val temp: String = "",
        val apparentTemp: String = "",
        val humidity: String = "",
        val windSpeed: String = "",
        val windDirection: String = "",
        val precipitation: String = "",
        val weatherType: WeatherType = CLEAR
    ) : WeatherModel()

    data class DailySample(
        val data: List<DailyForecast> = listOf()
    ) : WeatherModel()

    data class HourlySample(
        val data: List<HourlyForecast> = listOf()
    ) : WeatherModel()
}