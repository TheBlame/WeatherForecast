package dev.maxim_v.weather_app.data.network.api

import dev.maxim_v.weather_app.data.network.queryparams.CurrentParams
import dev.maxim_v.weather_app.data.network.queryparams.DailyParams
import dev.maxim_v.weather_app.data.network.queryparams.HourlyParams
import dev.maxim_v.weather_app.data.network.queryparams.TemperatureUnitParams
import dev.maxim_v.weather_app.data.network.queryparams.WindSpeedUnitParams

data class ForecastRequest(
    val latitude: Double,
    val longitude: Double,
    val currentParams: List<CurrentParams>?,
    val hourlyParams: List<HourlyParams>?,
    val dailyParams: List<DailyParams>?,
    val temperatureUnitParam: TemperatureUnitParams,
    val windSpeedUnitParam: WindSpeedUnitParams,
    val days: Int
)
