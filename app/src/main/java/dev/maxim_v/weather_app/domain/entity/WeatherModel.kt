package dev.maxim_v.weather_app.domain.entity

sealed class WeatherModel {

    data class CurrentSample(
        val location: String,
        val time: String,
        val temp: String,
        val weatherType: WeatherType
    ) : WeatherModel()

    data class DailySample(
        val sample: List<Daily>
    ) : WeatherModel()

    data class HourlySample(
        val sample: List<Hourly>
    ) : WeatherModel()
}