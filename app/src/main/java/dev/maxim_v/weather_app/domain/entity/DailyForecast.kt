package dev.maxim_v.weather_app.domain.entity

data class DailyForecast(
    val date: String = "",
    val minTemp: Int = 0,
    val maxTemp: Int = 0,
    val weatherType: WeatherType = WeatherType.CLEAR
)
