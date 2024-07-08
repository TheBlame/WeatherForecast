package dev.maxim_v.weather_app.domain.entity

import dev.maxim_v.weather_app.domain.entity.WeatherType.CLEAR

sealed class WeatherModel {

    data class CurrentSample(
        val location: String = "",
        val time: String = "",
        val temp: String = "",
        val weatherType: WeatherType = CLEAR
    ) : WeatherModel()

    data class DailySample(
        val sample: List<DailyForecast> = listOf()
    ) : WeatherModel()

    data class HourlySample(
        val sample: List<HourlyForecast> = listOf()
    ) : WeatherModel()
}