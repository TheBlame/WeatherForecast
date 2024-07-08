package dev.maxim_v.weather_app.domain.entity

data class HourlyForecast(
    val time: String,
    val temp: String,
    val weatherType: WeatherType
)
