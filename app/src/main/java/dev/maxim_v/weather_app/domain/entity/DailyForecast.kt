package dev.maxim_v.weather_app.domain.entity

data class DailyForecast(
    val date: String = "",
    val minTemp: String = "",
    val maxTemp: String = "",
    val weatherType: WeatherType = WeatherType.CLEAR
)
