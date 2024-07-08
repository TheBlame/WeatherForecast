package dev.maxim_v.weather_app.data.network.api

import dev.maxim_v.weather_app.data.network.queryparams.Current
import dev.maxim_v.weather_app.data.network.queryparams.Daily
import dev.maxim_v.weather_app.data.network.queryparams.Hourly
import dev.maxim_v.weather_app.data.network.queryparams.TemperatureUnit

data class ForecastRequest(
    val latitude: Double,
    val longitude: Double,
    val currentArgs: List<Current>?,
    val hourlyArgs: List<Hourly>?,
    val dailyArgs: List<Daily>?,
    val unit: TemperatureUnit
)
